package nsu.fit.cookbookapp.resultsShow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nsu.fit.cookbookapp.R
import nsu.fit.cookbookapp.client.CookBookRecipe

class CookBookRecipeAdapter :
    ListAdapter<CookBookRecipe, CookBookRecipeAdapter.RecipeViewHolder>(RecipeDiffCallback) {

    class RecipeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val nameTextView: TextView = itemView.findViewById(R.id.headline)
        private val stepsTextView: TextView = itemView.findViewById(R.id.steps)
        private val ingredientTextView: TextView = itemView.findViewById(R.id.ingredients)

        private var currentRecipe: CookBookRecipe? = null

        fun bind(recipe: CookBookRecipe) {
            currentRecipe = recipe

            nameTextView.text = recipe.name
            ingredientTextView.text = recipe.ingredients.toString()
            stepsTextView.text = recipe.steps.orEmpty().toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_view, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val cookBookRecipe = getItem(position)
        holder.bind(cookBookRecipe)
    }
}

object RecipeDiffCallback : DiffUtil.ItemCallback<CookBookRecipe>() {
    override fun areItemsTheSame(oldItem: CookBookRecipe, newItem: CookBookRecipe): Boolean {
       return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: CookBookRecipe, newItem: CookBookRecipe): Boolean {
        return (oldItem.steps == newItem.steps) && (oldItem.ingredients == newItem.ingredients)
    }
}