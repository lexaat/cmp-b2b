package data.storage

interface SecureStorage {
    suspend fun put(key: String, value: String)
    suspend fun get(key: String): String?
    suspend fun remove(key: String)
}

expect class SecureStorageFactory {
    fun create(): SecureStorage
}
