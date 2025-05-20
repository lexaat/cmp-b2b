# 📁 Структура проекта cmp-b2b (KMP + Compose Multiplatform)

## 🧱 Основные модули

- `composeApp/` — общий KMP-модуль (основная логика и UI)
- `iosApp/` — iOS-оболочка
- `androidMain/` — специфичный код для Android
- `commonMain/` — общая бизнес-логика, UI и архитектура

---

## 📁 Папки в `commonMain/kotlin`

### `features/`
Каждая фича разделена на подмодули по архитектуре MVI:

```
features/
├── auth/              // Авторизация (двухэтапная)
├── home/              // Главный экран со списком клиентов
└── client/            // Детали клиента (MVI)
```

#### Внутри каждой фичи:
- `presentation/` — ViewModel, Intent, State, SideEffect, UI
- `domain/` — модели и интерфейсы
- `data/` — реализации (например, `ClientRepositoryImpl.kt`)


### `di/`
Koin-модули:
- `AppModule.kt` — базовые зависимости
- `HomeModule.kt`, `ClientModule.kt`, `ConfigModule.kt` — по фичам
- `InitKoin.kt` (в `androidMain`) — вызов `startKoin()` с модулями


### `networking/`
- `HttpClientFactory.kt` — конфигурация Ktor клиента


### `config/`
- `AppConfig.kt`, `Config.kt` — конфигурации окружений (baseUrl и т.д.)


### `model/`
- Общие модели (`Client`, `Account`, и т.д.)


### `ui/`
- Переиспользуемые UI-компоненты (например, `ScreenWrapper.kt`)


### `App.kt`
- Точка входа в Compose-приложение (на уровне общего UI)

---

## ⚙️ Используемые технологии

- **Kotlin Multiplatform (KMP)** + **Compose Multiplatform** (Android + iOS)
- **Voyager** — навигация
- **Koin** — DI-фреймворк
- **Ktor** — HTTP-клиент
- **MVI архитектура** — Intent → State → SideEffect → UI

---

## 🔁 Пример фичи (ClientDetail)
```
features/client/
├── data/
│   └── ClientRepositoryImpl.kt
├── domain/
│   ├── model/ClientDetail.kt
│   └── repository/ClientRepository.kt
└── presentation/
    ├── ClientDetailIntent.kt
    ├── ClientDetailState.kt
    ├── ClientDetailViewModel.kt
    └── ClientDetailScreen.kt
```

---

## 🚀 Расширение
Для добавления новой фичи:
1. Создай `features/имя_фичи/{data, domain, presentation}`
2. Реализуй MVI (Intent, State, ViewModel, Screen)
3. Зарегистрируй ViewModel и Repository в `di/FeatureModule.kt`
4. Добавь модуль в `InitKoin.kt`
