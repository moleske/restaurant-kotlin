package com.oleske.recipe

import javax.persistence.Embeddable

@Embeddable
data class Ingredient(
        val name: String = "",
        val category: IngredientCategory? = null
)