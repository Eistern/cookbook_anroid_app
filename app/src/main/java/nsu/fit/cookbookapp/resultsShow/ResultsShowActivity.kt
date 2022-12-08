package nsu.fit.cookbookapp.resultsShow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import nsu.fit.cookbookapp.R
import nsu.fit.cookbookapp.client.CookBookRecipe

class ResultsShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results_show)

        val results = intent.getSerializableExtra("results") as ArrayList<CookBookRecipe>

        val recipeAdapter = CookBookRecipeAdapter()
        recipeAdapter.submitList(results)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = recipeAdapter
    }

}