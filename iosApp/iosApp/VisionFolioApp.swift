import SwiftUI
import Shared

@main
struct VisionFolioApp: App {
    @StateObject private var appGraph = AppGraph()

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(appGraph)
        }
    }
}
