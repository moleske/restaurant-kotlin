package com.oleske.recipe

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RecipeController(val recipeRepository: RecipeRepository) {

    @PostMapping("/newRecipe")
    fun createRecipe(@RequestBody recipe: Recipe) =
            ResponseEntity(recipeRepository.save(recipe), HttpStatus.CREATED)
}