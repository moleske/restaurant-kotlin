package com.oleske.recipe

import com.fasterxml.jackson.databind.ObjectMapper
import io.damo.aspen.Test
import org.hamcrest.Matchers.hasSize
import org.mockito.Matchers
import org.mockito.Mockito.*
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class RecipeControllerTest : Test() {

    lateinit var mvc: MockMvc
    lateinit var mockRecipeRepository: RecipeRepository
    val objectMapper = ObjectMapper()

    init {
        before {
            mockRecipeRepository = mock(RecipeRepository::class.java)
            mvc = MockMvcBuilders.standaloneSetup(RecipeController(mockRecipeRepository)).build()
        }

        test("create returns 201") {
            val request = Recipe(
                    id = null,
                    ingredients = listOf(Ingredient("Milk", IngredientCategory.DAIRY)),
                    chef = "Swedish Chef"
            )

            val recipe = Recipe(
                    id = 1L,
                    ingredients = listOf(Ingredient("Milk", IngredientCategory.DAIRY)),
                    chef = "Swedish Chef"
            )

            `when`(mockRecipeRepository.save(Matchers.any(Recipe::class.java))).thenReturn(recipe)

            mvc.perform(post("/newRecipe")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated)
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.ingredients", hasSize<Any>(1)))
                    .andExpect(jsonPath("$.ingredients[0].name").value("Milk"))
                    .andExpect(jsonPath("$.ingredients[0].category").value(IngredientCategory.DAIRY.toString()))
                    .andExpect(jsonPath("$.chef").value("Swedish Chef"))
            verify(mockRecipeRepository).save(request)
        }

        describe("recipeContainsSignificantAmountOfDairy when recipe has ") {
            test("is null returns false") {
                `when`(mockRecipeRepository.findOne(anyLong())).thenReturn(null)
                mvc.perform(get("/recipeHasDairy?id=1")).andExpect(status().isOk).andExpect(content().string("false"))
                verify(mockRecipeRepository).findOne(1L)
            }

            test("has dairy ingredient returns true") {
                val recipe = Recipe(
                        id = 1L,
                        ingredients = listOf(Ingredient("Milk", IngredientCategory.DAIRY)),
                        chef = "Swedish Chef")
                `when`(mockRecipeRepository.findOne(anyLong())).thenReturn(recipe)
                mvc.perform(get("/recipeHasDairy?id=1")).andExpect(status().isOk).andExpect(content().string("true"))
                verify(mockRecipeRepository).findOne(1L)
            }

            test("has no dairy ingredient returns false") {
                val recipe = Recipe(
                        id = 1L,
                        ingredients = listOf(Ingredient("Not Milk", IngredientCategory.NUT)),
                        chef = "Swedish Chef")
                `when`(mockRecipeRepository.findOne(anyLong())).thenReturn(recipe)
                mvc.perform(get("/recipeHasDairy?id=1")).andExpect(status().isOk).andExpect(content().string("false"))
                verify(mockRecipeRepository).findOne(1L)
            }

            test("has null ingredient returns false") {
                val recipe = Recipe(
                        id = 1L,
                        ingredients = listOf(Ingredient("Not Milk", null)),
                        chef = "Swedish Chef")
                `when`(mockRecipeRepository.findOne(anyLong())).thenReturn(recipe)
                mvc.perform(get("/recipeHasDairy?id=1")).andExpect(status().isOk).andExpect(content().string("false"))
                verify(mockRecipeRepository).findOne(1L)
            }
        }
    }
}