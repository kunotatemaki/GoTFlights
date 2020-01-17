@file:Suppress("DEPRECATION")

package com.rookia.gotflights.framework.utils.security


import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import com.rookia.gotflights.utils.security.Encryption
import timber.log.Timber
import java.lang.Exception
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.interfaces.RSAPublicKey
import java.util.*
import javax.crypto.Cipher
import javax.inject.Inject
import javax.inject.Singleton
import javax.security.auth.x500.X500Principal

@Singleton
class EncryptionImpl @Inject constructor(private val context: Context): Encryption {

    companion object {
        private const val CIPHER_TYPE = "RSA/ECB/PKCS1Padding"
    }

    private fun getKeystoreInstance(): KeyStore {
        val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore
    }

    private fun createNewKeys(alias: String) {

        val keyStore = getKeystoreInstance()

        try {
            // Create new key if needed
            if (!keyStore.containsAlias(alias)) {
                val generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    val start = Calendar.getInstance()
                    val end = Calendar.getInstance()
                    end.add(Calendar.YEAR, 1)

                    @Suppress("DEPRECATION")
                    val spec = KeyPairGeneratorSpec.Builder(context)
                        .setAlias(alias)
                        .setSubject(X500Principal("CN=Sample Name, O=Android Authority"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.time)
                        .setEndDate(end.time)
                        .build()
                    generator.initialize(spec)
                } else {

                    generator.initialize(
                        KeyGenParameterSpec.Builder(
                            alias,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                            .build())

                }
                generator.generateKeyPair()
            }
        } catch (e: Exception) {
            Timber.e(Log.getStackTraceString(e))
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Suppress("unused")
    @Throws(KeyStoreException::class)
    private fun deleteKey(alias: String) {
        val keyStore = getKeystoreInstance()
        keyStore.deleteEntry(alias)
    }

    override fun encryptString(text: String?, alias: String): String {

        if(text.isNullOrBlank()) return ""

        val keyStore = getKeystoreInstance()
        var encryptedText = ""
        try {
            createNewKeys(alias)
            val privateKeyEntry = keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry
            val publicKey = privateKeyEntry.certificate.publicKey as RSAPublicKey


            val inCipher = Cipher.getInstance(CIPHER_TYPE)
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey)

            val bytes = inCipher.doFinal(text.toByteArray())
            encryptedText = Base64.encodeToString(bytes, Base64.DEFAULT)


        } catch (e: Exception) {
            Timber.e(Log.getStackTraceString(e))
        }

        return encryptedText
    }

    override fun decryptString(text: String?, alias: String): String {

        if(text.isNullOrBlank()) return ""

        val keyStore = getKeystoreInstance()
        var decryptedText = ""
        try {
            val privateKeyEntry = keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry? ?: return ""
            //RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();
            val output = Cipher.getInstance(CIPHER_TYPE)
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)

            val encryptedData = Base64.decode(text, Base64.DEFAULT)
            val decodedData = output.doFinal(encryptedData)
            decryptedText = String(decodedData)

        } catch (e: Exception) {
            Timber.d(Log.getStackTraceString(e))
        }

        return decryptedText
    }



}