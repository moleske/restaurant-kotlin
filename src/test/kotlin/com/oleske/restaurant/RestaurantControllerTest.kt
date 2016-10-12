package com.oleske.restaurant

import com.fasterxml.jackson.databind.ObjectMapper
import io.damo.aspen.Test
import org.mockito.Matchers
import org.mockito.Mockito.*
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class RestaurantControllerTest : Test() {

    lateinit var mvc: MockMvc
    lateinit var mockRestaurantRepository: RestaurantRepository
    val objectMapper = ObjectMapper()

    init {
        before {
            mockRestaurantRepository = mock(RestaurantRepository::class.java)
            mvc = MockMvcBuilders.standaloneSetup(RestaurantController(mockRestaurantRepository)).build()
        }

        test("create returns 201") {
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
                    michelinStarRating = 1, zagatRating = 2
            )

            val restaurant = Restaurant(
                    id = 1L,
                    name = "name",
                    ownerName = "ownerName",
                    headChefName = "headChefName",
                    cuisineType = "cuisineType",
                    shortDescription = "shortDescription",
                    fullDescription = "fullDescription",
                    websiteUrl = "websiteUrl",
                    rating = 0,
                    michelinStarRating = 1, zagatRating = 2
            )

            `when`(mockRestaurantRepository.save(Matchers.any(Restaurant::class.java))).thenReturn(restaurant)

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
    }
}