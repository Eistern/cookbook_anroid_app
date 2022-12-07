package nsu.fit.cookbookapp.client

data class CookBookIngredient (
    val name: String,
    val amount: Float,
    val unit: String
)

data class CookBookRecipe (
    val name: String,
    val ingredients: List<CookBookIngredient>,
    val steps: List<String>?
) {
    init {
        require(ingredients.isNotEmpty()) { "Parse failure: empty list of ingredients" }
    }
}