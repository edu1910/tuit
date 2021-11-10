package br.com.ceducarneiro.tuit.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import br.com.ceducarneiro.tuit.adapter.TweetListAdapter
import br.com.ceducarneiro.tuit.model.Tweet.User
import br.com.ceducarneiro.tuit.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseTweetFragment() {

    companion object {
        const val USER_PARAM = "user"

        fun newInstance(user: User): ProfileFragment {
            val bundle = Bundle()
            bundle.putSerializable(USER_PARAM, user)
            val instance = ProfileFragment()
            instance.arguments = bundle
            return instance
        }
    }

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timeline(arguments?.getSerializable(USER_PARAM) as User)
    }

    override fun getOnClickListener(): TweetListAdapter.TweetListListener? {
        return null
    }

    override fun setupBinding() {
        super.setupBinding()
        binding.welcome.isVisible = false
    }

    private fun timeline(user: User) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userTimeline(user).collectLatest {
                adapter.submitData(it)
            }
        }
    }

}