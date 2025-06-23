package data.storage

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

actual class SecureStorageFactory(private val context: Context) {
    actual fun create(): SecureStorage = SecureStorageImpl(context)
}

private val Context.dataStore by preferencesDataStore(name = "secure_storage_and")

class SecureStorageImpl(private val context: Context) : SecureStorage {

    private val TAG = "SecureStorage"
    private val masterKey: SecretKey = getOrCreateSecretKey()

    private fun getOrCreateSecretKey(): SecretKey {
        val keyAlias = "secure_storage_key"
        val prefs = context.getSharedPreferences("crypto_keys", Context.MODE_PRIVATE)
        val saved = prefs.getString(keyAlias, null)

        return if (saved != null) {
            val decoded = Base64.decode(saved, Base64.DEFAULT)
            Log.d(TAG, "Loaded existing master key (length=${decoded.size})")
            SecretKeySpec(decoded, 0, decoded.size, "AES")
        } else {
            val keyGen = KeyGenerator.getInstance("AES")
            keyGen.init(256)
            val key = keyGen.generateKey()
            prefs.edit().putString(keyAlias, Base64.encodeToString(key.encoded, Base64.DEFAULT)).apply()
            Log.d(TAG, "Generated new master key (length=${key.encoded.size})")
            key
        }
    }

    override suspend fun put(key: String, value: String) {
        withContext(Dispatchers.IO) {
            try {
                val encrypted = encrypt(value)
                context.dataStore.edit {
                    it[stringPreferencesKey(key)] = encrypted
                }
                Log.d(TAG, "Saved key=$key, value=${value.take(20)}, encrypted=${encrypted.take(20)}...")
            } catch (e: Exception) {
                Log.e(TAG, "Error encrypting key=$key: ${e.message}", e)
            }
        }
    }

    override suspend fun get(key: String): String? = withContext(Dispatchers.IO) {
        val base64 = context.dataStore.data.first()[stringPreferencesKey(key)]
        Log.d(TAG, "Read key=$key, rawBase64=${base64?.take(30)}")
        if (base64 == null) {
            Log.d(TAG, "Key=$key not found in DataStore")
            return@withContext null
        }
        val decrypted = decrypt(base64)
        Log.d(TAG, "Decrypted key=$key, result=${decrypted?.take(30)}")
        decrypted
    }

    override suspend fun remove(key: String) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit {
                it.remove(stringPreferencesKey(key))
            }
            Log.d(TAG, "Removed key=$key")
        }
    }

    private fun encrypt(plainText: String): String {
        try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, masterKey)
            val iv = cipher.iv
            val encrypted = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
            val combined = iv + encrypted
            return Base64.encodeToString(combined, Base64.NO_WRAP)
        } catch (e: Exception) {
            Log.e(TAG, "Encryption error: ${e.message}", e)
            throw e
        }
    }

    private fun decrypt(base64: String): String? {
        return try {
            val bytes = Base64.decode(base64, Base64.NO_WRAP)
            val iv = bytes.copyOfRange(0, 12)
            val encrypted = bytes.copyOfRange(12, bytes.size)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, masterKey, spec)
            val decrypted = cipher.doFinal(encrypted)
            String(decrypted, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            Log.e(TAG, "Decryption error for base64='${base64.take(40)}...': ${e.message}", e)
            null
        }
    }
}
