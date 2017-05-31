package com.oleske

import com.fasterxml.jackson.databind.ObjectMapper
import com.oleske.recipe.Ingredient
import com.oleske.recipe.IngredientCategory
import com.oleske.recipe.Recipe
import com.oleske.restaurant.Restaurant
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.restdocs.snippet.Attributes.attributes
import org.springframework.restdocs.snippet.Attributes.key
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@RunWith(SpringRunner::class)
@SpringBootTest
class DocumentationJavaStyleTest {
    @get:Rule
    val restDocumentation = JUnitRestDocumentation("build/generated-snippets")

    @Autowired
    lateinit var context: WebApplicationContext

    lateinit var mockMvc: MockMvc
    val objectMapper = ObjectMapper()

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation)
                        .uris()
                        .withScheme("https")
                        .withHost("restaurant-kotlin.cfapps.pez.pivotal.io")
                        .withPort(443))
                .build()
    }

    @Test
    fun recipeController() {
        val request = Recipe(
                null,
                listOf(Ingredient("Milk", IngredientCategory.DAIRY)),
                "Swedish Chef"
        )

        val writeValueAsString = objectMapper.writeValueAsString(request)
        mockMvc.perform(post("/newRecipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsString))
                .andExpect(status().isCreated)
                .andDo(document("newRecipe",
                        requestFields(
                                attributes(key("title").value("Fields for a New Recipe")),
                                fieldWithPath("id").ignored(),
                                fieldWithPath("ingredients").description("List of ingredients"),
                                fieldWithPath("ingredients[].name").description("Name of ingredient"),
                                fieldWithPath("ingredients[].category").description("Category of ingredient"),
                                fieldWithPath("chef").description("Name of the chef")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of saved of recipe"),
                                fieldWithPath("ingredients").description("List of ingredients"),
                                fieldWithPath("ingredients[].name").description("Name of ingredient"),
                                fieldWithPath("ingredients[].category").description("Category of ingredient"),
                                fieldWithPath("chef").description("Name of the chef")
                        )
                ))

        mockMvc.perform(get("/recipeHasDairy?id=1"))
                .andExpect(status().isOk)
                .andDo(document("recipeHasDairy",
                        requestParameters(
                                parameterWithName("id").description("Id of recipe")
                        ),
                        responseFields(
                                fieldWithPath("hasDairy").description("Boolean if recipe of Id passed has dairy")
                        )
                ))
    }

    @Test
    fun restaurantController_post() {
        val request = Restaurant(
                id = null,
                name = "Swedish Food Company",
                ownerName = "Kermit the Frog",
                headChefName = "The Swedish Chef",
                cuisineType = "Swedish",
                shortDescription = "Zee best Svedeesh restoorunt! Bork Bork Bork!",
                fullDescription = "Ve-a serfe-a ell types ooff meetbells tu pleese-a yuoor pelete-a! Nutheeng is tuu guud fur yuoo! Bork Bork Bork!",
                websiteUrl = "https://swedishfoodcompany.com",
                rating = 90,
                michelinStarRating = 7,
                zagatRating = 5
        )

        val writeValueAsString = objectMapper.writeValueAsString(request)
        mockMvc.perform(post("/newRestaurant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsString))
                .andExpect(status().isCreated)
                .andDo(document("newRestaurant",
                        requestFields(
                                attributes(key("title").value("Fields for a New Restaurant")),
                                fieldWithPath("id").ignored(),
                                fieldWithPath("name").description("Name of Restaurant"),
                                fieldWithPath("ownerName").description("Name of Owner"),
                                fieldWithPath("headChefName").description("Name of Head Chef"),
                                fieldWithPath("cuisineType").description("Type of Cuisine Served"),
                                fieldWithPath("shortDescription").description("Short Description of Restaurant"),
                                fieldWithPath("fullDescription").description("Long Description of Restaurant"),
                                fieldWithPath("websiteUrl").description("Website of Restaurant"),
                                fieldWithPath("rating").description("Restaurant Rating"),
                                fieldWithPath("michelinStarRating").description("Michelin Rating"),
                                fieldWithPath("zagatRating").description("Zagat Rating")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of saved of restaurant"),
                                fieldWithPath("name").description("Name of Restaurant"),
                                fieldWithPath("ownerName").description("Name of Owner"),
                                fieldWithPath("headChefName").description("Name of Head Chef"),
                                fieldWithPath("cuisineType").description("Type of Cuisine Served"),
                                fieldWithPath("shortDescription").description("Short Description of Restaurant"),
                                fieldWithPath("fullDescription").description("Long Description of Restaurant"),
                                fieldWithPath("websiteUrl").description("Website of Restaurant"),
                                fieldWithPath("rating").description("Restaurant Rating"),
                                fieldWithPath("michelinStarRating").description("Michelin Rating"),
                                fieldWithPath("zagatRating").description("Zagat Rating")
                        )
                ))
    }

    @Test
    fun restaurantController_get() {
        mockMvc.perform(get("/restaurant"))
                .andExpect(status().isOk)
                .andDo(document("getRestaurant",
                        responseFields(
                                fieldWithPath("[]").description("An array of restaurants"),
                                fieldWithPath("[].id").description("Id of saved of restaurant"),
                                fieldWithPath("[].name").description("Name of Restaurant"),
                                fieldWithPath("[].ownerName").description("Name of Owner"),
                                fieldWithPath("[].headChefName").description("Name of Head Chef"),
                                fieldWithPath("[].cuisineType").description("Type of Cuisine Served"),
                                fieldWithPath("[].shortDescription").description("Short Description of Restaurant"),
                                fieldWithPath("[].fullDescription").description("Long Description of Restaurant"),
                                fieldWithPath("[].websiteUrl").description("Website of Restaurant"),
                                fieldWithPath("[].rating").description("Restaurant Rating"),
                                fieldWithPath("[].michelinStarRating").description("Michelin Rating"),
                                fieldWithPath("[].zagatRating").description("Zagat Rating")
                        )
                ))
    }
}
