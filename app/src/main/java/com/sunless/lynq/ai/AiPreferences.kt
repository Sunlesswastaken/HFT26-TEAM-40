package com.sunless.lynq.ai

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages persisted configuration for Featherless AI, including API key,
 * endpoint URL, LLM model choice, and preferred translation language.
 */
open class AiPreferences(context: Context) : AiConfigProvider {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _apiKey = MutableStateFlow(prefs.getString(KEY_API_KEY, "") ?: "")
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _baseUrl = MutableStateFlow(prefs.getString(KEY_BASE_URL, DEFAULT_BASE_URL) ?: DEFAULT_BASE_URL)
    val baseUrl: StateFlow<String> = _baseUrl.asStateFlow()

    private val _model = MutableStateFlow(prefs.getString(KEY_MODEL, DEFAULT_MODEL) ?: DEFAULT_MODEL)
    val model: StateFlow<String> = _model.asStateFlow()

    private val _targetLanguage = MutableStateFlow(prefs.getString(KEY_TARGET_LANG, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE)
    val targetLanguage: StateFlow<String> = _targetLanguage.asStateFlow()

    open fun setApiKey(key: String) {
        val trimmed = key.trim()
        prefs.edit().putString(KEY_API_KEY, trimmed).apply()
        _apiKey.value = trimmed
    }

    open fun setBaseUrl(url: String) {
        val trimmed = url.trim().removeSuffix("/")
        val finalUrl = if (trimmed.isBlank()) DEFAULT_BASE_URL else trimmed
        prefs.edit().putString(KEY_BASE_URL, finalUrl).apply()
        _baseUrl.value = finalUrl
    }

    open fun setModel(modelName: String) {
        val trimmed = modelName.trim()
        val finalModel = if (trimmed.isBlank()) DEFAULT_MODEL else trimmed
        prefs.edit().putString(KEY_MODEL, finalModel).apply()
        _model.value = finalModel
    }

    open fun setTargetLanguage(languageName: String) {
        prefs.edit().putString(KEY_TARGET_LANG, languageName).apply()
        _targetLanguage.value = languageName
    }

    override fun getConfig(): AiConfig {
        return AiConfig(
            apiKey = _apiKey.value,
            baseUrl = _baseUrl.value,
            model = _model.value,
            targetLanguage = _targetLanguage.value,
        )
    }

    override fun hasApiKey(): Boolean = _apiKey.value.isNotBlank()

    companion object {
        private const val PREFS_NAME = "lynq_ai_prefs"
        private const val KEY_API_KEY = "featherless_api_key"
        private const val KEY_BASE_URL = "featherless_base_url"
        private const val KEY_MODEL = "featherless_model"
        private const val KEY_TARGET_LANG = "featherless_target_lang"

        const val DEFAULT_BASE_URL = "https://api.featherless.ai/v1"
        const val DEFAULT_MODEL = "meta-llama/Meta-Llama-3.1-8B-Instruct"
        const val DEFAULT_LANGUAGE = "Hindi"

        val AVAILABLE_MODELS = listOf(
            "meta-llama/Meta-Llama-3.1-8B-Instruct",
            "mistralai/Mistral-7B-Instruct-v0.3",
            "Qwen/Qwen2.5-7B-Instruct",
            "google/gemma-2-9b-it",
        )
    }
}
