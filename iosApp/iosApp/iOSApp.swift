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

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        if let token = fcmToken {
            print("📱 Получен FCM token: \(token)")
            // Сохраняем токен в UserDefaults, чтобы достать в Kotlin
            UserDefaults.standard.set(token, forKey: "fcm_token")
        } else {
            print("❌ FCM токен не получен")
        }
    }

    // Обработка пушей в активном режиме
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        completionHandler([.alert, .sound])
    }
}
