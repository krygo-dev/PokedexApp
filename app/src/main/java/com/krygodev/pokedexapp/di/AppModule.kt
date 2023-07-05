package com.krygodev.pokedexapp.di

import com.krygodev.pokedexapp.data.remote.PokemonApi
import com.krygodev.pokedexapp.data.repository.PokemonRepositoryImpl
import com.krygodev.pokedexapp.domain.repository.PokemonRepository
import com.krygodev.pokedexapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokemonApi
    ): PokemonRepository {
        return PokemonRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun providePokemonApi(): PokemonApi {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokemonApi::class.java)
    }
}