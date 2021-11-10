package br.com.ceducarneiro.tuit.adapter

import android.annotation.SuppressLint
import android.widget.GridLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.ceducarneiro.tuit.databinding.TweetItemBinding
import br.com.ceducarneiro.tuit.model.Tweet
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class TweetListViewHolder(private val binding: TweetItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SimpleDateFormat")
    fun bind(tweet: Tweet, clickListener: ((Tweet) -> Unit)?) {
        itemView.setOnClickListener {
            clickListener?.let { it1 -> it1(tweet) }
        }
        binding.apply {
            txtName.text = tweet.user?.name
            txtUsername.text = ("@ ${tweet.user?.username.toString()}")
            txtTweet.text = tweet.text
            Picasso.get().load(tweet.user?.avatar).into(imgAvatar)
            val dateTimeFormat = SimpleDateFormat("EE MMM dd HH':'mm':'ss Z yyyy")

            if (tweet.date != null) {
                val dateTime = dateTimeFormat.parse(tweet.date)
                if (dateTime != null) {
                    val now = Calendar.getInstance().time
                    val diff = now.time - dateTime.time
                    val seconds = diff / 1000
                    val minutes = seconds / 60
                    val hours = minutes / 60
                    val days = hours / 24
                    if (days < 1) {
                        if (hours < 1) {
                            if (minutes < 1) {
                                txtTime.text = ("${seconds}s")
                            } else {
                                txtTime.text = ("${minutes}m")
                            }
                        } else {
                            txtTime.text = ("${hours}h")
                        }
                    } else {
                        txtTime.text = ("${days}d")
                    }
                }
            }
            gridImages.removeAllViews()
            tweet.entities?.media?.forEach {
                if (it.type == "photo") {
                    val imageView = ImageView(gridImages.context)
                    val layout = GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    )
                    layout.width = GridLayout.LayoutParams.MATCH_PARENT
                    layout.height = GridLayout.LayoutParams.WRAP_CONTENT
                    layout.columnSpec
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    gridImages.addView(imageView, layout)
                    Picasso.get().load(it.url).into(imageView)
                }
            }
        }
    }

}