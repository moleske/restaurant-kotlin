package com.oleske.restaurant

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RestaurantController(val restaurantRepository: RestaurantRepository) {

    @PostMapping("/newRestaurant")
    fun createRestaurant(@RequestBody restaurant: Restaurant) =
            ResponseEntity(restaurantRepository.save(restaurant), HttpStatus.CREATED)

    @GetMapping("restaurant")
    fun getRestaurants() = ResponseEntity(restaurantRepository.findAll(), HttpStatus.OK)
}