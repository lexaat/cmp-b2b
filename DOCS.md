# 📘 Документация проекта cmp-b2b

## 1. 📡 API-документация

### Сетевой стек

* Используется: **Ktor HTTP Client**
* Инициализация: `HttpClientFactory` в `commonMain/networking`
* JSON: `kotlinx.serialization.json`
* ContentNegotiation: установлен во всех платформах (OkHttp/Darwin)

```kotlin
val client = HttpClient(OkHttp) {
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    install(Logging) { level = LogLevel.ALL }
}
```

### Авторизация токеном

* Все API-запросы, кроме авторизации, используют JWT.
* Токен добавляется через кастомный `plugin` (не реализован в оригинале, рекомендован `RequestInterceptor`).
* Хранение токена: `PersistentTokenManager` (Multiplatform Settings)

### Обработка ошибок

* API возвращает `{"error": ..., "result": ...}`
* Ошибки отлавливаются в `try/catch` внутри ViewModel
* При ошибках можно диспатчить `SideEffect.ShowError(...)`

---

## 2. 🧭 Навигация

### Используется: [Voyager](https://github.com/adrielcafe/voyager)

* `Navigator` и `Screen` — базовые навигационные элементы
* Поддерживаются:

    * `.push(screen)`
    * `.pop()`
    * `BottomTabs`, `SheetNavigator`, `AnimatedTransition`

### Пример экрана

```kotlin
object LoginScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<AuthViewModel>()
        LoginScreenContent(viewModel)
    }
}
```

### Переход на экран клиента

```kotlin
navigator.push(ClientDetailScreen(clientId))
```

---

## 3. 🏛️ Архитектура приложения

### Подход: **MVI + Koin + Voyager**

#### Пример потока данных:

```
UI → Intent → ViewModel → Repository → API → State → UI
                   ↓
              SideEffect (ошибки, переходы)
```

### Слои:

* `presentation/` — `ViewModel`, `State`, `Intent`, `SideEffect`, `Screen`
* `domain/` — интерфейсы и модели
* `data/` — реализации (`Ktor`, `SQLDelight` и т.д.)
* `di/` — Koin-модули

### ViewModel (общее):

```kotlin
fun dispatch(intent: ...) {
    when (intent) {
        is Intent.Load -> load()
        is Intent.Retry -> retry()
    }
}
```

---

## 4. 🔐 Авторизация

### Особенности:

* Двухэтапная авторизация (логин + OTP)
* Поддержка смены пароля (новый пароль + подтверждение OTP)

### Архитектура:

* `AuthViewModel` — MVI, обрабатывает `SubmitCredentials`, `SubmitOtp`, `SubmitNewPassword`, `SubmitPasswordOtp`
* `AuthIntent`, `AuthState`, `AuthSideEffect` — всё как в MVI

### Состояния:

* `EnterCredentials`
* `WaitingForOtp`
* `RequirePasswordChange`
* `WaitingForPasswordOtp`
* `PasswordChanged`

### Работа с токеном:

* Сохраняется в `TokenManager` (Multiplatform Settings)
* Можно получать из ViewModel для авторизованных API-запросов

---

Если появятся refresh-токены, WebSocket, push-уведомления или offline-режим — их тоже можно встроить в эту архитектуру.

