package com.oleske.restaurant

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(RestaurantController::class)
internal class RestaurantControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var mockRestaurantRepository: RestaurantRepository

    private val objectMapper = ObjectMapper()
    private val restaurant = Restaurant(
            id = 1L,
            name = "name",
            ownerName = "ownerName",
            headChefName = "headChefName",
            cuisineType = "cuisineType",
            shortDescription = "shortDescription",
            fullDescription = "fullDescription",
            websiteUrl = "websiteUrl",
            rating = 0,
            michelinStarRating = 1,
            zagatRating = 2
    )

    @Test
    internal fun `create returns 201`() {
        val request = Restaurant(
                id = null,
                name = "name",
                ownerName = "ownerName",
                headChefName = "headChefName",
                cuisineType = "cuisineType",
                shortDescription = "shortDescription",
                fullDescription = "fullDescription",
                websiteUrl = "websiteUrl",
                rating = 0,
                michelinStarRating = 1,
                zagatRating = 2
        )

        `when`(mockRestaurantRepository.save(ArgumentMatchers.any(Restaurant::class.java))).thenReturn(restaurant)

        mvc.perform(post("/newRestaurant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value(request.name))
                .andExpect(jsonPath("$.ownerName").value(request.ownerName))
                .andExpect(jsonPath("$.headChefName").value(request.headChefName))
                .andExpect(jsonPath("$.cuisineType").value(request.cuisineType))
                .andExpect(jsonPath("$.shortDescription").value(request.shortDescription))
                .andExpect(jsonPath("$.fullDescription").value(request.fullDescription))
                .andExpect(jsonPath("$.websiteUrl").value(request.websiteUrl))
                .andExpect(jsonPath("$.rating").value(request.rating))
                .andExpect(jsonPath("$.michelinStarRating").value(request.michelinStarRating))
                .andExpect(jsonPath("$.zagatRating").value(request.zagatRating))

        verify(mockRestaurantRepository).save(request)
    }

    @Test
    internal fun `get returns data`() {
        `when`(mockRestaurantRepository.findAll()).thenReturn(mutableListOf(restaurant))

        mvc.perform(get("/restaurant"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", hasSize<Any>(1)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value(restaurant.name))
                .andExpect(jsonPath("$[0].ownerName").value(restaurant.ownerName))
                .andExpect(jsonPath("$[0].headChefName").value(restaurant.headChefName))
                .andExpect(jsonPath("$[0].cuisineType").value(restaurant.cuisineType))
                .andExpect(jsonPath("$[0].shortDescription").value(restaurant.shortDescription))
                .andExpect(jsonPath("$[0].fullDescription").value(restaurant.fullDescription))
                .andExpect(jsonPath("$[0].websiteUrl").value(restaurant.websiteUrl))
                .andExpect(jsonPath("$[0].rating").value(restaurant.rating))
                .andExpect(jsonPath("$[0].michelinStarRating").value(restaurant.michelinStarRating))
                .andExpect(jsonPath("$[0].zagatRating").value(restaurant.zagatRating))

        verify(mockRestaurantRepository).findAll()
    }
}