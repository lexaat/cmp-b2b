import SwiftUI
import FirebaseCore
import FirebaseMessaging
import shared

@main
struct iOSApp: App {
    
    // Для пушей необходим делегат
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate, MessagingDelegate, UNUserNotificationCenterDelegate {

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        FirebaseApp.configure()
        
        UNUserNotificationCenter.current().delegate = self
        Messaging.messaging().delegate = self
        
        // запрос разрешений на пуши
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { success, error in
            if success {
                print("✅ Пуши разрешены!")
            } else if let error = error {
                print("❌ Ошибка при запросе пушей: \(error.localizedDescription)")
            }
        }
        
        UIApplication.shared.registerForRemoteNotifications()
        
        return true
    }

    func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
        print("📨 APNs token зарегистрирован: \(deviceToken.map { String(format: "%02.2hhx", $0) }.joined())")
    }
    
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        if let token = fcmToken {
            print("📱 Получен FCM token: \(token)")
            // Сохраняем токен в UserDefaults, чтобы достать в Kotlin
            UserDefaults.standard.set(token, forKey: "fcm_token")
        } else {
            print("❌ FCM токен не получен")
        }
    }
    
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        let userInfo = response.notification.request.content.userInfo
        print("📬 Пользователь тапнул по пушу: \(userInfo)")
        
        // Здесь можно передать данные в SwiftUI или Kotlin
        completionHandler()
    }

    // Обработка пушей в активном режиме
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        print("📩 PUSH пришёл: \(notification.request.content.userInfo)")
        completionHandler([.badge, .sound, .banner])
    }
}
