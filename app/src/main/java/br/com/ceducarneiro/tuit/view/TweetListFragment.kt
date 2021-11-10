package br.com.ceducarneiro.tuit.view

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import br.com.ceducarneiro.tuit.R
import br.com.ceducarneiro.tuit.adapter.TweetListAdapter
import br.com.ceducarneiro.tuit.provider.TweetSearchSuggestionProvider
import br.com.ceducarneiro.tuit.provider.TwitterSearchRecentSuggestions
import br.com.ceducarneiro.tuit.viewmodel.TweetListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TweetListFragment : BaseTweetFragment() {

    companion object {
        fun newInstance(): TweetListFragment {
            return TweetListFragment()
        }
    }

    private lateinit var searchMenuItem: MenuItem
    private val viewModel: TweetListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentSearch.observe(requireActivity(), {
            search(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        searchMenuItem = menu.findItem(R.id.menu_search)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (searchMenuItem.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            isIconified = true
        }
        (searchMenuItem.actionView as SearchView).maxWidth = Int.MAX_VALUE
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun getOnClickListener(): TweetListAdapter.TweetListListener {
        return TweetListAdapter.TweetListListener {
            viewModel.selectItem(it)
        }
    }

    private fun search(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchTweets(query).collectLatest {
                adapter.submitData(it)
            }
        }
        (searchMenuItem.actionView as SearchView).onActionViewCollapsed()
        val suggestions = TwitterSearchRecentSuggestions(
            context, TweetSearchSuggestionProvider.AUTHORITY,
            TweetSearchSuggestionProvider.MODE, 5
        )
        suggestions.saveRecentQuery(query, null)
    }

}