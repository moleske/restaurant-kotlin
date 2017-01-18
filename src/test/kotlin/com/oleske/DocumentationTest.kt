package com.oleske

import com.fasterxml.jackson.databind.ObjectMapper
import com.oleske.recipe.Ingredient
import com.oleske.recipe.IngredientCategory
import com.oleske.recipe.Recipe
import io.damo.aspen.Test
import io.damo.aspen.spring.SpringTestTreeRunner
import org.junit.Ignore
import org.junit.Rule
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.restdocs.snippet.Attributes.attributes
import org.springframework.restdocs.snippet.Attributes.key
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringTestTreeRunner::class)
@SpringBootTest
@Ignore
class DocumentationTest : Test() {
    @get:Rule
    val restDocumentation = JUnitRestDocumentation("build/generated-snippets")

    @Autowired
    lateinit var context: WebApplicationContext

    lateinit var mockMvc: MockMvc
    val objectMapper = ObjectMapper()

    init {
        before {
            mockMvc = MockMvcBuilders.webAppContextSetup(context)
                    .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation)
                            .uris()
                            .withScheme("https")
                            .withHost("restaurant-kotlin.cfapps.pez.pivotal.io")
                            .withPort(443))
                    .build()
        }

        test("recipeController") {
            val request = Recipe(
                    id = null,
                    ingredients = listOf(Ingredient("Milk", IngredientCategory.DAIRY)),
                    chef = "Swedish Chef"
            )

            mockMvc.perform(post("/newRecipe")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
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
    }
}