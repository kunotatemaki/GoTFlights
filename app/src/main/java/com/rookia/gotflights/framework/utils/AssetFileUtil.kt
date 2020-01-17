package com.rookia.gotflights.framework.utils

import android.content.Context
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("unused")
@Singleton
class AssetFileUtil @Inject constructor(private val context: Context) {

    fun loadJSONFromAsset(filePath: String): String? {

        val json: String?
        try {
            val inputStream = context.assets.open(filePath)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json


    }
}