package com.laioffer

import io.ktor.http.ContentType
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticBasePackage
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Serializable
data class Playlist (
    val id: Int,
    val songs: List<Song>
)

@Serializable
data class Song (
    val name: String,
    val lyric: String,
    val src: String,
    val length: String
)

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module) // :: means the reference of a function
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }
    // TODO: adding the routing configuration here
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // 0.0.0.0:8080/feed => Home
        get("/feed") {
            val jsonString: String? = this::class.java.classLoader.getResource("feed.json")?.readText()
            call.respondText(jsonString ?: "", ContentType.Application.Json) // ?: means fallback
        }

        // 0.0.0.0:8080/playlists => Playlist
        get("/playlists") {
            val jsonString: String? = this::class.java.classLoader.getResource("playlists.json")?.readText()
            call.respondText(jsonString ?: "", ContentType.Application.Json) // ?: means fallback
        }

        // 0.0.0.0:8080/playlist/{id}
        get("/playlist/{id}") {
            // jsonString -> List<Playlist> -> loop by playlist -> filter by playlist.id == {id} -> return playlist
//            val jsonString: String? = this::class.java.classLoader.getResource("playlists.json")?.readText()
//            if (jsonString != null) {
//                val playlists = Json.decodeFromString(ListSerializer(Playlist.serializer()), jsonString)
//                val id = call.parameters["id"]
//                val playlist = playlists.firstOrNull { //pl ->
//                    it.id.toString() == id
//                }
//                call.respondNullable(playlist)
//            } else {
//                call.respondText("null", ContentType.Application.Json)
//            }

            this::class.java.classLoader.getResource("playlists.json")?.readText()?.let { jsonString -> // ideal world: if (jsonString != null)
                val playlists = Json.decodeFromString(ListSerializer(Playlist.serializer()), jsonString)
                val id = call.parameters["id"]
                val playlist = playlists.firstOrNull { //pl ->
                    it.id.toString() == id
                }
                call.respondNullable(playlist)
            } ?: call.respondText("null", ContentType.Application.Json)

        }

        // 0.0.0.0:8080/songs/solo.mp3 => download/stream music
        static("/") {
            staticBasePackage = "static"
            static("songs") {
                resources("songs")
            }
        }
    }

    // Lambda argument should be moved out of parentheses
    myRouting {
        println("do something")

        myGet("/") {

        }
    }
}

// when lambda is the last parameter of a functin
fun myRouting(block: () -> Unit) {

}

fun myGet(path: String, block: () -> Unit) {

}