package com.oleske.recipe

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
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
import java.util.*

@WebMvcTest(RecipeController::class)
internal class RecipeControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var mockRecipeRepository: RecipeRepository

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        objectMapper = ObjectMapper()
    }

    @Test
    internal fun `create returns 201`() {
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

        `when`(mockRecipeRepository.save(ArgumentMatchers.any(Recipe::class.java))).thenReturn(recipe)

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

    @Test
    internal fun `recipeContainsSignificantAmountOfDairy when recipe is null returns false`() {
        `when`(mockRecipeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null))

        mvc.perform(get("/recipeHasDairy?id=1"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("hasDairy").value("false"))

        verify(mockRecipeRepository).findById(1L)
    }

    @Test
    internal fun `recipeContainsSignificantAmountOfDairy when recipe has dairy ingredient returns true`() {
        val recipe = Recipe(
                id = 1L,
                ingredients = listOf(Ingredient("Milk", IngredientCategory.DAIRY)),
                chef = "Swedish Chef")
        `when`(mockRecipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe))

        mvc.perform(get("/recipeHasDairy?id=1"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("hasDairy").value("true"))

        verify(mockRecipeRepository).findById(1L)
    }

    @Test
    internal fun `recipeContainsSignificantAmountOfDairy when recipe has no dairy ingredient returns false`() {
        val recipe = Recipe(
                id = 1L,
                ingredients = listOf(Ingredient("Not Milk", IngredientCategory.NUT)),
                chef = "Swedish Chef")
        `when`(mockRecipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe))

        mvc.perform(get("/recipeHasDairy?id=1"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("hasDairy").value("false"))

        verify(mockRecipeRepository).findById(1L)
    }

    @Test
    internal fun `recipeContainsSignificantAmountOfDairy when recipe has null ingredient returns false`() {
        val recipe = Recipe(
                id = 1L,
                ingredients = listOf(Ingredient("Not Milk", null)),
                chef = "Swedish Chef")
        `when`(mockRecipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe))

        mvc.perform(get("/recipeHasDairy?id=1"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("hasDairy").value("false"))

        verify(mockRecipeRepository).findById(1L)
    }
}