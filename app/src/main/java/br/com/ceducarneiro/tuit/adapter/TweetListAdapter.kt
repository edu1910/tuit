package br.com.ceducarneiro.tuit.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import br.com.ceducarneiro.tuit.databinding.TweetItemBinding
import br.com.ceducarneiro.tuit.model.Tweet

class TweetListAdapter(private val listener: TweetListListener?) :
    PagingDataAdapter<Tweet, TweetListViewHolder>(TWEET_COMPARATOR) {

    companion object {
        private val TWEET_COMPARATOR = object : DiffUtil.ItemCallback<Tweet>() {
            override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetListViewHolder {
        return TweetListViewHolder(
            TweetItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: TweetListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, listener?.onTweetClick) }
    }

    data class TweetListListener(val onTweetClick: (tweet: Tweet) -> Unit)
}