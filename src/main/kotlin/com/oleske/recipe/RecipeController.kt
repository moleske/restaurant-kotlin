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
    fun recipeContainsSignificantAmountOfDairy(@RequestParam id: Long): ResponseEntity<Boolean> {
        val recipe = recipeRepository.findOne(id)
        val result = recipe?.ingredients?.any {
            ingredient: Ingredient -> ingredient.category == IngredientCategory.DAIRY
        } ?: false
        return ResponseEntity(result, HttpStatus.OK)
    }
}