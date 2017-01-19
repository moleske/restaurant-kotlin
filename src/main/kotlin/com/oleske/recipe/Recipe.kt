package com.oleske.recipe

import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id

@Entity
data class Recipe(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        val id: Long?,
        @ElementCollection
        val ingredients: List<Ingredient>,
        val chef: String
)