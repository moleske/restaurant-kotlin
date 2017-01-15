package com.oleske.restaurant

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id

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