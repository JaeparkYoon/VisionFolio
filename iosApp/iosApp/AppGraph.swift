import SwiftUI
import Combine
import Shared

/// Wraps `IosAppGraph` (KMP) for SwiftUI environment usage.
final class AppGraph: ObservableObject {
    let kmp: IosAppGraph

    init() {
        self.kmp = IosAppGraphKt.createIosAppGraph()
    }
}
