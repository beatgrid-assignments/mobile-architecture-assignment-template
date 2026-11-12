package com.beatgridmedia.measurementkit.module.authentication

/*
 * TODO(assignment): login with phone number.
 *
 * Design the messages, plugins and providers that implement this feature. Things to think about:
 *
 * - Which messages does the app send (and what do they return)? What state does the middleware keep in between?
 * - Which parts are platform specific (e.g. reading the code from the incoming SMS, secure storage of credentials)
 *   and how are they hidden behind a provider so the plugins stay in common code?
 * - How does the runtime end up in `MeasurementKitState.LoggedIn`, and how is that state restored on the next app
 *   start?
 * - How do authenticated REST calls get their credentials (see `DefaultRestProvider.prepare`), and what happens when
 *   those expire?
 * - What happens when the user abandons the flow halfway (app killed, wrong number, code never arrives)?
 */
