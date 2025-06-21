package platform


class iOSPushService : PushService {

    override fun initialize() {
        // Пока ничего не делаем
        println("iOSPushService: initialize() заглушка")
    }

    override fun getToken(onToken: (String?) -> Unit) {
        // Возвращаем фиктивный токен
        onToken("mock_ios_push_token")
    }
}