package br.com.ceducarneiro.tuit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.ceducarneiro.tuit.model.Tweet
import br.com.ceducarneiro.tuit.model.Tweet.User
import br.com.ceducarneiro.tuit.service.TwitterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: TwitterRepository
) : ViewModel() {

    fun userTimeline(user: User): Flow<PagingData<Tweet>> {
        return repository.userTimeline(user).cachedIn(viewModelScope)
    }

}