package com.oleske.recipe

import jakarta.persistence.Embeddable

@Embeddable
data class Ingredient(
        val name: String,
        val category: IngredientCategory?
)