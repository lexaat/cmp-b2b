package data.storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSUserDefaults

actual class SecureStorageFactory {
    actual fun create(): SecureStorage = IosSecureStorage()
}

class IosSecureStorage : SecureStorage {
    private val defaults = NSUserDefaults.standardUserDefaults()

    override suspend fun put(key: String, value: String) = withContext(Dispatchers.Default) {
        defaults.setObject(value, forKey = key)
    }

    override suspend fun get(key: String): String? = withContext(Dispatchers.Default) {
        defaults.stringForKey(key)
    }

    override suspend fun remove(key: String) = withContext(Dispatchers.Default) {
        defaults.removeObjectForKey(key)
    }
}
