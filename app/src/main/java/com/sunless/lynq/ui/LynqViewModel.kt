package com.sunless.lynq.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sunless.lynq.LynqApp
import com.sunless.lynq.domain.model.DeliveryStatus
import com.sunless.lynq.domain.model.LynqMessage
import com.sunless.lynq.domain.model.MessageType
import com.sunless.lynq.domain.model.Peer
import com.sunless.lynq.domain.model.Priority
import com.sunless.lynq.service.LynqMeshService
import com.sunless.lynq.demo.DemoData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MeshViewMode {
    LIVE,   // Real physical devices discovered & connected over Nearby Connections
    DEMO,   // Simulated 4-node relay with Aarav, Kabir, Meera, Priya
}

class LynqViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as LynqApp
    private val database = app.database
    private val router = app.router
    private val crypto = app.cryptoManager
    private val locationProvider = app.locationProvider

    val myDeviceId: String = crypto.deviceId
    private val _myDeviceName = MutableStateFlow(router.myName)
    val myDeviceName: StateFlow<String> = _myDeviceName.asStateFlow()
    val publicKeyString: String = crypto.publicKeyString

    fun updateDeviceName(newName: String) {
        val clean = newName.trim()
        if (clean.isNotBlank()) {
            com.sunless.lynq.util.DeviceNameHelper.setCustomDeviceName(app, clean)
            router.updateMyName(clean)
            _myDeviceName.value = clean
        }
    }

    fun resetDeviceNameToSystem() {
        com.sunless.lynq.util.DeviceNameHelper.setCustomDeviceName(app, "")
        val sysName = com.sunless.lynq.util.DeviceNameHelper.getDeviceName(app)
        router.updateMyName(sysName)
        _myDeviceName.value = sysName
    }

    private val demoPeerIds = setOf("2b7f9c41", "5d80e2aa", "c14b76f3", "8ae0d915")
    private val demoNames = setOf("Aarav", "Meera", "Kabir", "Priya")

    private val _meshMode = MutableStateFlow(MeshViewMode.LIVE)
    val meshMode: StateFlow<MeshViewMode> = _meshMode.asStateFlow()

    fun setMeshMode(mode: MeshViewMode) {
        _meshMode.value = mode
    }

    // Live reactive streams from SQLite DB & Nearby Connections
    val peers: StateFlow<List<Peer>> = database.peersFlow
    val messages: StateFlow<List<LynqMessage>> = database.messagesFlow

    // Live real peers (excluding any residual demo fixtures or unverified phantom entries)
    val livePeers: StateFlow<List<Peer>> = peers.map { list ->
        list.filter {
            it.id !in demoPeerIds &&
            it.name !in demoNames &&
            !it.name.startsWith("Peer-", ignoreCase = true) &&
            it.name != "Peer" &&
            it.id.length > 4 &&
            it.connected
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active peers based on selected MeshViewMode (LIVE or DEMO)
    val activePeers: StateFlow<List<Peer>> = combine(meshMode, livePeers) { mode, realPeers ->
        when (mode) {
            MeshViewMode.LIVE -> realPeers
            MeshViewMode.DEMO -> DemoData.peers
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val alerts: StateFlow<List<LynqMessage>> = messages.map { list ->
        list.filter { it.type == MessageType.SOS }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingMessages: StateFlow<List<LynqMessage>> = messages.map { list ->
        list.filter { it.status == DeliveryStatus.PENDING }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reachableCount: StateFlow<Int> = livePeers.map { list ->
        list.count { it.connected }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val relayCount: StateFlow<Int> = livePeers.map { list ->
        list.count { it.relaying }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val activeReachableCount: StateFlow<Int> = combine(meshMode, reachableCount) { mode, realReachable ->
        when (mode) {
            MeshViewMode.LIVE -> realReachable
            MeshViewMode.DEMO -> DemoData.reachableCount
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val activeRelayCount: StateFlow<Int> = combine(meshMode, relayCount) { mode, realRelay ->
        when (mode) {
            MeshViewMode.LIVE -> realRelay
            MeshViewMode.DEMO -> DemoData.relayCount
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _isMeshActive = MutableStateFlow(true)
    val isMeshActive: StateFlow<Boolean> = _isMeshActive.asStateFlow()

    private val _activeSosBroadcasting = MutableStateFlow(false)
    val activeSosBroadcasting: StateFlow<Boolean> = _activeSosBroadcasting.asStateFlow()

    private val _lastSentSos = MutableStateFlow<LynqMessage?>(null)
    val lastSentSos: StateFlow<LynqMessage?> = _lastSentSos.asStateFlow()

    private val _openPeerId = MutableStateFlow<String?>(null)
    val openPeerId: StateFlow<String?> = _openPeerId.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun openThread(peerId: String?) {
        _openPeerId.value = peerId
    }

    fun clearAlertHistory() {
        viewModelScope.launch {
            database.clearAlertHistory()
            _lastSentSos.value = null
        }
    }

    init {
        // Start mesh routing and foreground service
        startMesh()
        clearAlertHistory()
    }

    fun startMesh() {
        _isMeshActive.value = true
        router.start()
        LynqMeshService.start(getApplication())
    }

    fun stopMesh() {
        _isMeshActive.value = false
        router.stop()
        LynqMeshService.stop(getApplication())
    }

    fun toggleMesh(enabled: Boolean) {
        if (enabled) startMesh() else stopMesh()
    }

    fun sendSos(category: String, note: String, shareLocation: Boolean) {
        viewModelScope.launch {
            _activeSosBroadcasting.value = true
            val coords = if (shareLocation) {
                locationProvider.getEmergencyCoordinates() ?: "28.6139° N, 77.2090° E (±12m)"
            } else {
                null
            }

            val sentMsg = router.sendSos(
                category = category,
                details = note,
                coordinates = coords,
            )
            _lastSentSos.value = sentMsg
        }
    }

    fun triggerSimulation(category: String = "Medical", note: String = "Trapped, need assistance") {
        viewModelScope.launch {
            _meshMode.value = MeshViewMode.DEMO
            _activeSosBroadcasting.value = true
            router.simulateMultiHopDemo(
                originName = "You",
                relayNodes = listOf("Aarav", "Kabir", "Priya"),
                category = category,
                notes = note,
            )
        }
    }

    fun cancelSos() {
        _activeSosBroadcasting.value = false
        _lastSentSos.value = null
    }

    fun sendChatMessage(recipient: Peer, text: String, priority: Priority = Priority.NORMAL) {
        viewModelScope.launch {
            router.sendChatMessage(recipient, text, priority)
        }
    }
}

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
}
