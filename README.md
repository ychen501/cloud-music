# Cloud Music

A Spotify-style music streaming app: an Android client built with Jetpack Compose, backed by a lightweight Ktor server that serves the home feed, playlists, and MP3 streams.

## Features

- **Home feed** — sectioned album carousels ("Top mixes", "Made for you", ...) loaded from the backend
- **Playlist** — album details and track list, tap a song to play
- **Player** — streaming playback with ExoPlayer, play / pause and seek from a bottom player bar
- **Favorites** — save albums locally with Room; the list updates reactively via Flow

## Tech Stack

| | |
|---|---|
| **Client** | Kotlin, Jetpack Compose, MVVM (ViewModel), Hilt, Retrofit + Gson, Room, ExoPlayer, Coil, Navigation Component (Safe Args) |
| **Backend** | Kotlin, Ktor 2 (Netty), kotlinx.serialization |

## Project Structure

```
cloud-music/
├── backend/                      # Ktor server (port 8080)
│   └── src/main/
│       ├── kotlin/com/laioffer/Application.kt   # routes
│       └── resources/
│           ├── feed.json         # home feed data
│           ├── playlists.json    # playlists and songs
│           └── static/songs/     # MP3 files (not tracked, see below)
└── client/                       # Android app
    └── app/src/main/java/com/laioffer/spotify/
        ├── ui/                   # home, playlist, favorite screens + theme
        ├── player/               # ExoPlayer ViewModel and player bar
        ├── network/              # Retrofit API + Hilt module
        ├── database/             # Room database and DAO
        ├── repository/           # data layer
        └── datamodel/            # Album, Song, Playlist, Section
```

## API

| Method | Path | Description |
|---|---|---|
| GET | `/feed` | Home feed sections with albums |
| GET | `/playlists` | All playlists |
| GET | `/playlist/{id}` | A single playlist by album id |
| GET | `/songs/{file}` | Stream an MP3 file |

## Getting Started

### Prerequisites

- JDK 11+
- Android Studio (AGP 7.4) with an emulator running API 23+

### 1. Add the audio files

MP3 files are not included in this repository for copyright reasons. Put your own files in `backend/src/main/resources/static/songs/`, using the file names referenced in `backend/src/main/resources/playlists.json` (e.g. `solo.mp3`). Without them, the app still runs but songs won't play.

### 2. Start the backend

```bash
cd backend
./gradlew run
```

Check that it is up: http://localhost:8080/feed

### 3. Run the Android app

Open the `client/` folder in Android Studio and run the `app` configuration on an emulator.

The client reaches the backend at `http://10.0.2.2:8080/`, which is how the Android emulator addresses the host machine's `localhost`. To run on a physical device, change `BASE_URL` in `client/app/src/main/java/com/laioffer/spotify/network/NetworkModule.kt`, the song URLs in `playlists.json`, and the allowed domain in `client/app/src/main/res/xml/network_security_config.xml` to your computer's LAN IP.

## Acknowledgements

Built on the Spotify project template from the LaiOffer Android course. Album artwork is loaded from Wikipedia.
