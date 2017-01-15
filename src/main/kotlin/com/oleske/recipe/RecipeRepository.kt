package com.oleske.recipe

import org.springframework.data.repository.CrudRepository

interface RecipeRepository : CrudRepository<Recipe, Long>