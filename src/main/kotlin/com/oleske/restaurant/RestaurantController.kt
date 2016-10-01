package com.oleske.restaurant

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class RestaurantController constructor(val restaurantRepository: RestaurantRepository){

    @RequestMapping(value = "/newRestaurant", method = arrayOf(RequestMethod.POST))
    fun createRestaurant(@RequestBody restaurant: Restaurant) =
            ResponseEntity(restaurantRepository.save(restaurant), HttpStatus.CREATED)
}