package br.com.ceducarneiro.tuit.view

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.ceducarneiro.tuit.R
import br.com.ceducarneiro.tuit.adapter.LoadStateAdapter
import br.com.ceducarneiro.tuit.adapter.TweetListAdapter
import br.com.ceducarneiro.tuit.databinding.BaseTweetFragmentBinding
import br.com.ceducarneiro.tuit.util.ErrorCode
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

abstract class BaseTweetFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    protected lateinit var adapter: TweetListAdapter
    protected lateinit var binding: BaseTweetFragmentBinding
    private var manualLoad = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BaseTweetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
    }

    protected open fun setupBinding() {
        binding.swipe.setOnRefreshListener(this)
        binding.rvTweets.apply {
            layoutManager = LinearLayoutManager(context)
            configAdapter()
        }
        val color = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorAccent, color, true)

        binding.swipe.setColorSchemeColors(color.data)
        binding.btnRetry.setOnClickListener {
            adapter.refresh()
        }
        binding.placeholder.isVisible = false
        binding.loading.isVisible = false
        binding.error.isVisible = false
        binding.swipe.isVisible = false
    }

    abstract fun getOnClickListener(): TweetListAdapter.TweetListListener?

    protected open fun configAdapter() {
        adapter = TweetListAdapter(getOnClickListener())
        binding.rvTweets.adapter = adapter.withLoadStateFooter(LoadStateAdapter(adapter::retry))

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvTweets.scrollToPosition(0) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                binding.welcome.isVisible = false
                binding.placeholder.isVisible = false
                binding.loading.isVisible = false
                binding.error.isVisible = false
                binding.swipe.isVisible = true
                when (it.refresh) {
                    is LoadState.Loading -> {
                        if (binding.swipe.isRefreshing) {
                            manualLoad = true
                        }
                        binding.loading.isVisible = !manualLoad
                    }
                    is LoadState.NotLoading -> {
                        if (manualLoad) {
                            binding.swipe.isRefreshing = false
                            manualLoad = false
                        }
                        binding.placeholder.isVisible = (adapter.itemCount == 0)
                    }
                    is LoadState.Error -> {
                        binding.swipe.isVisible = false
                        binding.error.isVisible = true
                        val msgResId =
                            ErrorCode.getErrorFromNetwork((it.refresh as LoadState.Error).error)
                        binding.txtError.text = getString(msgResId)
                        (it.refresh as LoadState.Error).error.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onRefresh() {
        adapter.refresh()
    }

}