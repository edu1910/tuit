package br.com.ceducarneiro.tuit.view

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.ceducarneiro.tuit.R
import br.com.ceducarneiro.tuit.databinding.TweetListActivityBinding
import br.com.ceducarneiro.tuit.model.Tweet.User
import br.com.ceducarneiro.tuit.viewmodel.TweetListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TweetListActivity : AppCompatActivity() {

    private val viewModel: TweetListViewModel by viewModels()
    private lateinit var binding: TweetListActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TweetListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleIntent(intent)
        viewModel.selectedItem.observe(this, {
            it.user?.let { it1 -> goToProfile(it1) }
        })

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, TweetListFragment.newInstance())
            .commitNow()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun goToProfile(user: User) {
        startActivity(Intent(this, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.USER_PARAM, user)
        })
    }

    private fun search(query: String) {
        viewModel.currentSearch(query)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)

            if (query != null) {
                search(query)
                title = query
            }
        }
    }
}