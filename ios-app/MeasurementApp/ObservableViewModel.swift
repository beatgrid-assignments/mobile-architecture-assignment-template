import Combine
import MeasurementKit

/// Bridges a shared (Kotlin) view model to SwiftUI: publishes the view model's state flow and clears the view model
/// when the observable object is released (the iOS counterpart of `SharedViewModelHolder` on Android).
final class ObservableViewModel<ViewModel: MeasurementKitViewModel, State: AnyObject>: ObservableObject {

    let viewModel: ViewModel

    @Published private(set) var state: State

    private var registration: BeatKitRegistration?

    init(_ viewModel: ViewModel, watcher: (ViewModel) -> FlowWatcher<State>) {
        self.viewModel = viewModel
        let flowWatcher = watcher(viewModel)
        self.state = flowWatcher.value
        self.registration = flowWatcher.watch { [weak self] state in
            self?.state = state
        }
    }

    deinit {
        registration?.unregister()
        viewModel.onCleared()
    }
}
