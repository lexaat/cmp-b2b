import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

func colorFromArgb(_ argb: Int32) -> Color {
    let a = Double((argb >> 24) & 0xFF) / 255.0
    let r = Double((argb >> 16) & 0xFF) / 255.0
    let g = Double((argb >> 8) & 0xFF) / 255.0
    let b = Double(argb & 0xFF) / 255.0

    return Color(.sRGB, red: r, green: g, blue: b, opacity: a)
}

struct ContentView: View {
    var body: some View {
        let argb = BridgeKt.getCurrentBackgroundColorArgb()
        let dynamicBackground = colorFromArgb(argb)

        ZStack {
            dynamicBackground.ignoresSafeArea()
            ComposeView().background(Color.clear)
        }
    }
}