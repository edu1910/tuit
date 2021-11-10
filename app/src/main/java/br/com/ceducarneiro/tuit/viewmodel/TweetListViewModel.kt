package br.com.ceducarneiro.tuit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.ceducarneiro.tuit.model.Tweet
import br.com.ceducarneiro.tuit.service.TwitterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TweetListViewModel @Inject constructor(
    private val repository: TwitterRepository
) : ViewModel() {

    private val mutableSelectedItem = MutableLiveData<Tweet>()
    private val mutableCurrentSearch = MutableLiveData<String>()
    val selectedItem: LiveData<Tweet> get() = mutableSelectedItem
    val currentSearch: LiveData<String> get() = mutableCurrentSearch

    fun selectItem(item: Tweet) {
        mutableSelectedItem.value = item
    }

    fun currentSearch(search: String) {
        mutableCurrentSearch.value = search
    }

    fun searchTweets(query: String): Flow<PagingData<Tweet>> {
        return repository.searchTweets(query).cachedIn(viewModelScope)
    }

}