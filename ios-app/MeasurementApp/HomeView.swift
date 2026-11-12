import SwiftUI

struct HomeView: View {

    let onLogout: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            Text("You are logged in")
                .font(.title2)
            Button("Log out", action: onLogout)
                .buttonStyle(.bordered)
        }
    }
}
