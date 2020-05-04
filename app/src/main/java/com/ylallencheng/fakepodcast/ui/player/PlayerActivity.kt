package com.ylallencheng.fakepodcast.ui.player

import android.os.Bundle
import androidx.activity.viewModels
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }
}