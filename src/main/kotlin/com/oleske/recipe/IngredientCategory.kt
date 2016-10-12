package com.oleske.recipe

enum class IngredientCategory {
    DRY_GOOD, DAIRY, NUT;

    fun significantAmount(): Boolean = true
}