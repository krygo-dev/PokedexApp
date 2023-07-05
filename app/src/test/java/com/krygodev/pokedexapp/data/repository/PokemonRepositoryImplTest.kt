package com.krygodev.pokedexapp.data.repository

import com.google.common.truth.Truth.assertThat
import com.krygodev.pokedexapp.data.remote.PokemonApi
import com.krygodev.pokedexapp.data.repository.test_api_response.invalidPokemonDetailsResponse
import com.krygodev.pokedexapp.data.repository.test_api_response.invalidPokemonListResponse
import com.krygodev.pokedexapp.data.repository.test_api_response.validPokemonDetailsResponse
import com.krygodev.pokedexapp.data.repository.test_api_response.validPokemonListResponse
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class PokemonRepositoryImplTest {

    private lateinit var repository: PokemonRepositoryImpl
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: PokemonApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()
        api = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(PokemonApi::class.java)
        repository = PokemonRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Get Pokemon list, valid response, returns results`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validPokemonListResponse)
        )

        val result = repository.getPokemonList(limit = 20, offset = 0)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `Get Pokemon list, invalid response, returns failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setBody(validPokemonListResponse)
        )

        val result = repository.getPokemonList(limit = 20, offset = 0)

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Get Pokemon list, malformed response, returns failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(invalidPokemonListResponse)
        )

        val result = repository.getPokemonList(limit = 20, offset = 0)

        assertThat(result.isFailure).isTrue()
    }


    @Test
    fun `Get Pokemon details, valid response, returns results`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validPokemonDetailsResponse)
        )

        val testName = "pikachu"
        val result = repository.getPokemonDetails(name = testName)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `Get Pokemon details, invalid response, returns failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setBody(validPokemonDetailsResponse)
        )

        val testName = "pikachu"
        val result = repository.getPokemonDetails(name = testName)

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Get Pokemon details, malformed response, returns failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(invalidPokemonDetailsResponse)
        )

        val testName = "pikachu"
        val result = repository.getPokemonDetails(name = testName)

        assertThat(result.isFailure).isTrue()
    }
}