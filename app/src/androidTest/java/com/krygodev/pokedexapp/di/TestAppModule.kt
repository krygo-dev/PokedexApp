package com.krygodev.pokedexapp.di

import com.krygodev.pokedexapp.domain.repository.PokemonRepository
import com.krygodev.pokedexapp.repository.FakePokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(): PokemonRepository {
        return FakePokemonRepository()
    }
}