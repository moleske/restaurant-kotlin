package com.oleske.recipe

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class RecipeController(val recipeRepository: RecipeRepository) {

    @PostMapping("/newRecipe")
    fun createRecipe(@RequestBody recipe: Recipe) =
            ResponseEntity(recipeRepository.save(recipe), HttpStatus.CREATED)

    @GetMapping("/recipeHasDairy")
    fun recipeContainsSignificantAmountOfDairy(@RequestParam id: Long): ResponseEntity<String> {
        val recipe = recipeRepository.findById(id).orElse(null)
        val result = recipe?.ingredients?.any { it.category == IngredientCategory.DAIRY } ?: false
        return ResponseEntity("{ \"hasDairy\":$result}", HttpStatus.OK)
    }
}