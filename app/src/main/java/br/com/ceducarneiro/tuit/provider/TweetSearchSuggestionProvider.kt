package br.com.ceducarneiro.tuit.provider

import android.content.SearchRecentSuggestionsProvider

class TweetSearchSuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "br.com.ceducarneiro.tuit.provider.TweetSearchSuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES or DATABASE_MODE_2LINES
    }
}