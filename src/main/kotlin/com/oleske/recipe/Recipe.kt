package com.oleske.recipe

import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id

@Entity
data class Recipe(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        val id: Long?,
        @ElementCollection
        val ingredients: List<Ingredient>,
        val chef: String
)