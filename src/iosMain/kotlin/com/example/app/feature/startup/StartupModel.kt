package com.example.app.feature.startup

import com.example.app.http.createHttpClient
import io.ktor.client.request.get

class StartupModel : StartupFeature.Model {

    private val http = createHttpClient()

    private val urls = listOf(
        "https://dummy.restapiexample.com/api/v1/employees",
        "https://jsonplaceholder.typicode.com/todos"
    )

    override suspend fun getDummyJson() =
        http.get<String>(urls.random())

}
