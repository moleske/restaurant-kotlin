package com.oleske.restaurant

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id

@Entity
data class Restaurant(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        val id: Long?,
        val name: String,
        val ownerName: String,
        val headChefName: String,
        val cuisineType: String,
        val shortDescription: String,
        val fullDescription: String,
        val websiteUrl: String,
        val rating: Int,
        val michelinStarRating: Int,
        val zagatRating: Int
)