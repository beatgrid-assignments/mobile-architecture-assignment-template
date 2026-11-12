import SwiftUI
import MeasurementKit

/// TODO(assignment): login with phone number.
struct LoginView: View {

    @StateObject private var model: ObservableViewModel<LoginViewModel, LoginUiState>

    init(runtime: any BeatKitRuntime) {
        _model = StateObject(wrappedValue: ObservableViewModel(LoginViewModel(runtime: runtime)) { $0.stateWatcher })
    }

    var body: some View {
        VStack(spacing: 8) {
            Spacer()
            Text("Welcome")
                .font(.largeTitle)
            Text("Log in to continue")
                .font(.body)
            Spacer()
                .frame(height: 32)
            Button {
                model.viewModel.onLoginWithPhoneNumberClicked()
            } label: {
                Text("Login with phone number")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .controlSize(.large)
            .disabled(model.state.isBusy)
            Spacer()
            Text(model.state.serverStatus ?? "")
                .font(.caption2)
                .foregroundStyle(.secondary)
        }
        .padding(24)
        .alert(
            model.state.message ?? "",
            isPresented: Binding(
                get: { model.state.message != nil },
                set: { presented in if !presented { model.viewModel.onMessageShown() } }
            )
        ) {
            Button("OK", role: .cancel) {}
        }
    }
}
