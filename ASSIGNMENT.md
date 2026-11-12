# Assignment: login with phone number

Our panel apps (Android and iOS) let users log in with their phone number: the user enters their number, receives a
code by SMS, enters the code (or the app fills it in automatically) and is logged in. If there is no account for the
number yet, one is created.

This workspace is the starting point of that feature. It has the module structure of our real code base, a shared
Kotlin middleware, both apps with a login screen that has a single *Login with phone number* button, and a server with
a `User` entity and placeholder endpoints. Nothing of the login flow itself has been designed yet.

**Your job is to design the feature, end to end.** We will work through it together; we are more interested in how
you reason about the trade-offs than in a complete implementation. Writing code is welcome where it helps to make a
design concrete (interfaces, messages, endpoints, entities, a sequence diagram), but not required.

## What we'd like your design to cover

1. **Structure of the client.** How the feature fits into the plugin / provider architecture of the Kotlin middleware
   (see [docs/architecture.md](docs/architecture.md)), so that it is implemented once and behaves the same on Android
   and iOS. Which messages, plugins and providers are there, what is common code and what is platform specific?
   What do the screens / view models look like?
2. **The security protocol.** How the phone number is verified, what the server keeps track of, and how and when an
   account is created.
3. **Abuse.** Every SMS costs real money, and the endpoint that sends one is public. How do you prevent the feature
   from being abused (by bots, by attackers trying to make money off our SMS traffic, by brute forcing the code)?
4. **Abandoned logins.** Users close the app halfway, enter a wrong number, or never receive the SMS. What happens on
   the client and on the server?
5. **SMS auto-fill.** How do we make sure the code from the SMS is filled into the verification code field
   automatically, on both platforms?
6. **Access tokens.** What does the app receive after a successful login, how are requests authenticated, and how
   does the server validate that?
7. **Token refresh.** How does the session stay alive, how are tokens refreshed, and what happens when that fails?
8. **Infrastructure.** How do you set up the server side for this in production (we run on AWS): which service sends
   the SMS messages, what else is needed, and how is it configured?

## Deliverables

There is no fixed format. Things that tend to work well:

- A diagram of the components and a sequence diagram of the happy flow (Mermaid in a Markdown file is fine).
- An [architecture decision record](docs/adr/) for the most important decisions: what were the options, what did you
  choose and why.
- The skeleton of the design in code: message classes, plugin / provider interfaces, endpoints, DTOs, entities and
  migrations.

## Where to start

- [README.md](README.md): how to build and run everything.
- [docs/architecture.md](docs/architecture.md): how the middleware works, with a worked example.
- `grep -rn "TODO(assignment)" .` lists the placeholders, on the client and on the server.
