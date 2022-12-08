package nsu.fit.cookbookapp.client

data class CookBookIngredient (
    val name: String,
    val amount: Float,
    val unit: String
) : java.io.Serializable

data class CookBookRecipe (
    val name: String,
    val ingredients: List<CookBookIngredient>,
    val steps: List<String>?
) : java.io.Serializable {
    init {
        require(ingredients.isNotEmpty()) { "Parse failure: empty list of ingredients" }
    }
}