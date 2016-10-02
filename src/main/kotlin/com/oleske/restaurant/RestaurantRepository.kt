package com.oleske.restaurant

import org.springframework.data.repository.CrudRepository

interface RestaurantRepository : CrudRepository<Restaurant, Long> {
}