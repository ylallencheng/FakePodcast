package com.ylallencheng.fakepodcast.ui.player

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ylallencheng.fakepodcast.databinding.ActivityPlayerBinding
import com.ylallencheng.fakepodcast.di.viewmodel.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class PlayerActivity : DaggerAppCompatActivity() {

    // View Model
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel: PlayerViewModel by viewModels { viewModelFactory }

    // View binding
    private val mBinding: ActivityPlayerBinding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    companion object {

        fun navigate(
            from: AppCompatActivity,
            title: String,
            description: String,
            contentUrl: String
        ) {
            from.startActivity(Intent(from, PlayerActivity::class.java).apply {
                putExtra("title", title)
                putExtra("description", description)
                putExtra("contentUrl", contentUrl)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }
}