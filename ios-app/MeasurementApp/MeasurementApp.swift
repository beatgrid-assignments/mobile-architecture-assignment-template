import SwiftUI
import MeasurementKit

@main
struct MeasurementApp: App {

    private let runtime: any BeatKitRuntime

    init() {
        // Note: Kotlin/Native exports enum entries in lower case (ServiceBaseUrl -> servicebaseurl).
        runtime = MeasurementKitFactory.shared.createRuntime(
            platformContext: PlatformContext(),
            configuration: [
                // The iOS simulator shares the network of the host machine.
                MeasurementKitConfigurationKey.servicebaseurl.key: "http://localhost:8080",
                MeasurementKitConfigurationKey.clientversion.key:
                    Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? "0.0.0",
                MeasurementKitConfigurationKey.clientplatform.key: "ios",
            ],
            additionalProviders: [],
            debug: true
        )
        runtime.start()
    }

    var body: some Scene {
        WindowGroup {
            RootView(runtime: runtime)
        }
    }
}

/// Shows the login flow or the home screen depending on the runtime state.
struct RootView: View {

    private let runtime: any BeatKitRuntime
    @StateObject private var session: ObservableViewModel<SessionViewModel, MeasurementKitState>

    init(runtime: any BeatKitRuntime) {
        self.runtime = runtime
        _session = StateObject(wrappedValue: ObservableViewModel(SessionViewModel(runtime: runtime)) { $0.stateWatcher })
    }

    var body: some View {
        if session.state == MeasurementKitState.loggedin {
            HomeView(onLogout: { session.viewModel.logout() })
        } else {
            LoginView(runtime: runtime)
        }
    }
}
