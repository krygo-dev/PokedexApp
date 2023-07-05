package com.krygodev.pokedexapp.data.model.pokemon_details

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)