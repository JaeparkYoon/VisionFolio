import SwiftUI
import Combine
import Shared

/// Bridges KMP `MVIViewModel<I, S, E>` state to SwiftUI `@Published` property.
///
/// Usage:
/// ```swift
/// let vm = graph.kmp.createHomeViewModel()
/// let wrapper = MVIViewModelWrapper<HomeViewModel, HomeState>(vm)
/// // wrapper.state for current state (type-safe)
/// // wrapper.dispatch(HomeIntent.Refresh()) to send intents
/// ```
@MainActor
final class MVIViewModelWrapper<VM: AnyObject, S: AnyObject>: ObservableObject {
    let vm: VM
    @Published var state: S

    private let stateFlow: Kotlinx_coroutines_coreStateFlow
    private let dispatchFn: (Any) -> Void
    private let clearFn: () -> Void
    private var stateTask: Task<Void, Never>?

    init<I: AnyObject, E: AnyObject>(_ vm: VM) where VM: MVIViewModel<I, S, E> {
        self.vm = vm
        self.stateFlow = vm.state
        self.dispatchFn = { intent in vm.dispatch(intent: intent as! I) }
        self.clearFn = { vm.clear() }
        // swiftlint:disable:next force_cast
        self.state = vm.state.value as! S
        self.stateTask = nil
        observeState()
    }

    private func observeState() {
        stateTask = Task { [weak self] in
            guard let self else { return }
            do {
                for try await s in self.stateFlow.asAsyncSequence() {
                    guard !Task.isCancelled else { break }
                    guard let typed = s as? S else { continue }
                    self.state = typed
                }
            } catch {
                // Flow collection ended or was cancelled
            }
        }
    }

    /// Dispatches an intent to the MVIViewModel.
    func dispatch(_ intent: Any) {
        dispatchFn(intent)
    }

    deinit {
        stateTask?.cancel()
        clearFn()
    }
}
