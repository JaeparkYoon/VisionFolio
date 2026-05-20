import SwiftUI
import Shared

struct ContentView: View {
    @EnvironmentObject var graph: AppGraph

    var body: some View {
        TabView {
            Tab("Home", systemImage: "house") {
                NavigationStack {
                    HomeStubView()
                }
            }
            Tab("Trend", systemImage: "chart.line.uptrend.xyaxis") {
                NavigationStack {
                    TrendStubView()
                }
            }
            Tab("Dividend", systemImage: "chart.bar.xaxis") {
                NavigationStack {
                    DividendStubView()
                }
            }
            Tab("Settings", systemImage: "gearshape") {
                NavigationStack {
                    SettingsStubView()
                }
            }
        }
    }
}

// MARK: - Stub Views

struct HomeStubView: View {
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "chart.pie")
                .font(.system(size: 48))
                .foregroundStyle(.secondary)
            Text("Home")
                .font(.title2.bold())
            Text("Portfolio overview will appear here.")
                .foregroundStyle(.secondary)
        }
        .navigationTitle("VisionFolio")
    }
}

struct TrendStubView: View {
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "chart.line.uptrend.xyaxis")
                .font(.system(size: 48))
                .foregroundStyle(.secondary)
            Text("Trend")
                .font(.title2.bold())
            Text("Portfolio trend analysis will appear here.")
                .foregroundStyle(.secondary)
        }
        .navigationTitle("Trend")
    }
}

struct DividendStubView: View {
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "chart.bar.xaxis")
                .font(.system(size: 48))
                .foregroundStyle(.secondary)
            Text("Dividend")
                .font(.title2.bold())
            Text("Dividend information will appear here.")
                .foregroundStyle(.secondary)
        }
        .navigationTitle("Dividend")
    }
}

struct SettingsStubView: View {
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "gearshape")
                .font(.system(size: 48))
                .foregroundStyle(.secondary)
            Text("Settings")
                .font(.title2.bold())
            Text("App settings will appear here.")
                .foregroundStyle(.secondary)
        }
        .navigationTitle("Settings")
    }
}

#Preview {
    ContentView().environmentObject(AppGraph())
}
