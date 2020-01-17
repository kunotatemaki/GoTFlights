package com.rookia.gotflights.framework.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import com.rookia.gotflights.data.preferences.PreferencesManager
import com.rookia.gotflights.utils.security.Encryption
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManagerImpl @Inject constructor(private var context: Context, private val encryption: Encryption) :
    PreferencesManager {

    override fun getIntFromPreferences(key: String): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(key, -1)
    }

    override fun getStringFromPreferences(key: String): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(key, "") ?: ""
    }

    override fun getEncryptedStringFromPreferences(key: String, alias: String): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val encrypted = prefs.getString(key, "") ?: ""
        return encryption.decryptString(encrypted, alias)
    }

    override fun getBooleanFromPreferences(key: String): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(key, false)
    }

    override fun getLongFromPreferences(key: String): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getLong(key, (-1).toLong())
    }

    override fun getFloatFromPreferences(key: String): Float {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getFloat(key, (-1).toFloat())
    }

    override fun setIntIntoPreferences(key: String, value: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putInt(key, value).apply()
    }

    override fun setStringIntoPreferences(key: String, value: String?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putString(key, value).apply()
    }

    override fun setEncryptedStringIntoPreferences(key: String, value: String, alias: String): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val encryptedText = encryption.encryptString(value, alias)
        return if (encryptedText.isNotBlank()) {
            prefs.edit().putString(key, encryptedText).apply()
            true
        } else {
            prefs.edit().putString(key, value).apply()
            false
        }
    }

    override fun setBooleanIntoPreferences(key: String, value: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean(key, value).apply()
    }

    override fun setLongIntoPreferences(key: String, value: Long) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putLong(key, value).apply()
    }

    override fun setFloatIntoPreferences(key: String, value: Float) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putFloat(key, value).apply()
    }

    override fun deleteVarFromSharedPreferences(key: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().remove(key).apply()
    }

    override fun containsKey(key: String): Boolean =
        PreferenceManager.getDefaultSharedPreferences(context).contains(key)

    override fun clearAll() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().clear().apply()
    }
}