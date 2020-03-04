package com.example.app.feature.startup

import com.example.app.http.createHttpClient
import io.ktor.client.request.get
import kotlinx.serialization.json.JsonElement

class StartupModel : StartupFeature.Model {

    private val http = createHttpClient()

    private val urls = listOf(
        "https://dummy.restapiexample.com/api/v1/employees",
        "https://jsonplaceholder.typicode.com/todos",
        "https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json",
        "https://www.govtrack.us/api/v2/role?current=true&role_type=senator",
        "https://data.parliament.scot/api/events",
        "https://collections.museumvictoria.com.au/api/search"
    )

    override suspend fun getDummyJson() =
        http.get<JsonElement>(urls.random())

}
