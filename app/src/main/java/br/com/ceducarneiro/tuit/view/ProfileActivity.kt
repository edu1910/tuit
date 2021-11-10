package br.com.ceducarneiro.tuit.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.ceducarneiro.tuit.R
import br.com.ceducarneiro.tuit.databinding.ProfileActivityBinding
import br.com.ceducarneiro.tuit.model.Tweet.User
import br.com.ceducarneiro.tuit.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    companion object {
        const val USER_PARAM = "user"
    }

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: ProfileActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getSerializableExtra(USER_PARAM) as User
        binding.toolbarLayout.title = user.name
        binding.txtUsername.text = ("@ ${user.username}")
        binding.txtDescription.text = user.description
        Picasso.get().load(user.cover).into(binding.imgCover)
        Picasso.get().load(user.avatar).into(binding.imgAvatar)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ProfileFragment.newInstance(user))
            .commitNow()
    }
}