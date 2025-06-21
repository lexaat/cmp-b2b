//
//  AppDelegate.swift
//  iosApp
//
//  Created by Татьяна Татаренко on 21/06/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import UIKit
import FirebaseCore
import FirebaseMessaging
import UserNotifications

class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {

    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        print("🟢 App запущен")
        FirebaseApp.configure()

        UNUserNotificationCenter.current().delegate = self
        Messaging.messaging().delegate = self

        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { granted, error in
            print("🔔 Разрешение на уведомления: \(granted)")
            if let error = error {
                print("❌ Ошибка запроса разрешений: \(error)")
            }
        }

        application.registerForRemoteNotifications()
        print("📡 Запрошена регистрация для remote notifications")

        return true
    }

    func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        let tokenString = deviceToken.map { String(format: "%02.2hhx", $0) }.joined()
        print("✅ APNs токен получен: \(tokenString)")
        Messaging.messaging().apnsToken = deviceToken
    }

    func application(_ application: UIApplication,
                     didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("❌ Ошибка регистрации remote notifications: \(error)")
    }

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("📲 Получен FCM-токен: \(fcmToken ?? "nil")")
    }

    // Пуш получен в foreground
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        print("📥 Пуш получен в foreground: \(notification.request.content.userInfo)")
        completionHandler([.sound, .badge])
    }

    // Пуш нажат/открыт
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse,
                                withCompletionHandler completionHandler: @escaping () -> Void) {
        print("📨 Пользователь взаимодействовал с пушем: \(response.notification.request.content.userInfo)")
        completionHandler()
    }
}
