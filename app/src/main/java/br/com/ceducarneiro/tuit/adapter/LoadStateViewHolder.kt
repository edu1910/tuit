package br.com.ceducarneiro.tuit.adapter

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import br.com.ceducarneiro.tuit.databinding.LoadingItemBinding
import br.com.ceducarneiro.tuit.util.ErrorCode

class LoadStateViewHolder(
    private val binding: LoadingItemBinding, retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btnRetry.setOnClickListener {
            retry()
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            val msgResId = ErrorCode.getErrorFromNetwork(loadState.error)
            binding.txtError.text = itemView.context.getString(msgResId)
        }

        binding.txtProgress.isVisible = loadState is LoadState.Loading
        binding.error.isVisible = loadState is LoadState.Error
    }
}