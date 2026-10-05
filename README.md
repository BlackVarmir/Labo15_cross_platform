# labo15_cross_platform

Kotlin Multiplatform (Compose Multiplatform) project for **Лабораторна робота №14**.
Continuation of Lab 13 — adds a **PUT request whose result is shown on screen as plain text**,
on top of the **Ktor** + **Koin** networking layer introduced in Lab 11.

Targets: **Android**, **Desktop (JVM)**, **iOS** (iOS builds require macOS + Xcode).

Test API: [jsonplaceholder.typicode.com](https://jsonplaceholder.typicode.com/).

## What Lab 14 adds

1. **Raw PUT call** — `ApiService.updatePostRawText(id, title, body)` performs `PUT /posts/{id}`
   with a JSON body and returns the response body verbatim via Ktor's `bodyAsText()`, without
   deserializing it.
2. **Repository method** — `PostRepository.updatePostText(id, title, body)` wraps that call in a
   `NetworkResult` so success and failure are handled uniformly.
3. **"PUT as text" screen** (`ui/puttext/`) — `PutTextPage` + `PutTextViewModel`. The screen
   sends the PUT automatically on first display (and again via the **PUT** button), shows a
   progress indicator while the call is in flight, and renders the raw response body on screen
   as plain text (or an error message on failure).
4. **Navigation** — new `Screen.PutText` route, a Home-screen button, and the matching
   `composable` entry in `AppNavHost`.

## Carried over from earlier labs

- The Lab 13 **"POST as text"** screen (`ui/posttext/`) and Lab 12 **"GET as text"** screen
  (`ui/gettext/`), Ktor client + kotlinx.serialization networking layer and `NetworkResult`
  (Lab 11), the GET/POST/PUT/DELETE test screen (`ui/network/`), custom theme (`ui/theme/`,
  `labo15Theme`), Koin DI, Multiplatform Settings (About-screen visit stats), SQLDelight
  reminders, Kermit logging, and `expect class Platform` for system info.

## Run

Android:

```
.\gradlew.bat :composeApp:assembleDebug
```

Desktop (JVM):

```
.\gradlew.bat :composeApp:run
```

iOS: open the project on macOS and run the `iosApp` target.

> Note: this project was set up on Windows. Android and Desktop targets build on Windows;
> the iOS target requires macOS. Android requires the `INTERNET` permission (already declared
> in the manifest).
