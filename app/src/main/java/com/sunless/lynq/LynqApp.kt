package com.sunless.lynq

import android.app.Application
import com.sunless.lynq.crypto.CryptoManager
import com.sunless.lynq.data.storage.LynqDatabase
import com.sunless.lynq.domain.model.Peer
import com.sunless.lynq.engine.MessageRouter
import com.sunless.lynq.location.LocationProvider
import com.sunless.lynq.network.NearbyManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LynqApp : Application() {

    lateinit var database: LynqDatabase private set
    lateinit var cryptoManager: CryptoManager private set
    lateinit var nearbyManager: NearbyManager private set
    lateinit var router: MessageRouter private set
    lateinit var locationProvider: LocationProvider private set

    private val appScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = LynqDatabase(this)
        cryptoManager = CryptoManager(this)
        nearbyManager = NearbyManager(
            context = this,
            localDeviceId = cryptoManager.deviceId,
            localDeviceName = "Sunless",
        )
        router = MessageRouter(
            database = database,
            cryptoManager = cryptoManager,
            nearbyManager = nearbyManager,
            myName = "Sunless",
        )
        locationProvider = LocationProvider(this)

        // Pre-seed demo peers if database has no peers so graph and explorer are immediately rich
        appScope.launch {
            seedInitialPeersIfEmpty()
        }
    }

    private suspend fun seedInitialPeersIfEmpty() {
        if (database.peersFlow.value.isEmpty()) {
            val now = System.currentTimeMillis()
            val initialPeers = listOf(
                Peer(id = "2b7f9c41", name = "Aarav", connected = true, relaying = true, hops = 1, lastSeen = now - 4000),
                Peer(id = "5d80e2aa", name = "Meera", connected = true, relaying = true, hops = 1, lastSeen = now - 11000),
                Peer(id = "c14b76f3", name = "Kabir", connected = true, relaying = false, hops = 2, lastSeen = now - 38000),
                Peer(id = "8ae0d915", name = "Priya", connected = true, relaying = false, hops = 3, lastSeen = now - 96000),
            )
            for (p in initialPeers) {
                database.upsertPeer(p)
            }
        }
    }

    companion object {
        lateinit var instance: LynqApp private set
    }
}
