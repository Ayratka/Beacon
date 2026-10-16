# Beacon

Personal monitors for any web page or API. Create a beacon with a URL,
a value to extract, and a condition. Beacon watches it in the background
and notifies you when the condition fires.

## Status

Early development. Domain layer and project structure are in place.

## Architecture

Four Gradle modules with strict dependency rules.

```
:app            Android application, wires everything
:data           Ktor, Room, Hilt, repository implementations
:presentation   Compose, ViewModels, UI state
:domain         pure Kotlin, no framework dependencies
```

`:domain` has no Android dependencies. It compiles and tests on the JVM.

## Tech stack

Kotlin 2.4.21, Coroutines, Flow, Ktor, kotlinx.serialization,
Room, Hilt, Jetpack Compose, WorkManager.

## Roadmap

- [x] Multi-module structure
- [x] Core domain models
- [x] `Fetcher` interface
- [x] Ktor JSON fetcher
- [ ] Room persistence
- [ ] `CheckBeaconUseCase`
- [ ] Compose UI
- [ ] WorkManager scheduling
- [ ] Notifications

## Building

```bash
./gradlew build
```

Requires JDK 17 and Android SDK 36.

## License

MIT
