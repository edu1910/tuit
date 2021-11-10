package br.com.ceducarneiro.tuit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import br.com.ceducarneiro.tuit.databinding.LoadingItemBinding

class LoadStateAdapter(
    private val retry: () -> Unit
) : androidx.paging.LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = LoadStateViewHolder(
        LoadingItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ), retry
    )

    override fun onBindViewHolder(
        holder: LoadStateViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)

}