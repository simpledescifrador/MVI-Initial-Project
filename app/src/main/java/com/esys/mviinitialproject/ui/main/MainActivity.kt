package com.esys.mviinitialproject.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.esys.mviinitialproject.MainApplication
import com.esys.mviinitialproject.databinding.ActivityMainBinding
import com.esys.mviinitialproject.ui.base.BaseActivity
import com.esys.mviinitialproject.util.AppUtils
import com.esys.mviinitialproject.util.ViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class MainActivity : BaseActivity<MainViewModel>() {

    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }

    override fun setupUI() {
    }


    override fun setupClicks() {
        lifecycleScope.launchWhenCreated {
        }
    }

    override fun observeViewModel() {
        lifecycleScope.launchWhenCreated {
            viewModel.mainState.collect {
                when (it) {
                    is MainViewModel.MainState.Idle -> {

                    }
                    is MainViewModel.MainState.Loading -> {

                    }
                    is MainViewModel.MainState.Error -> {
                        AppUtils.appendLog(this@MainActivity, it.error, AppUtils.LogType.ERROR)
                    }
                }
            }
        }
    }


    override fun setupViewModel(): MainViewModel {
        val app = this.application as MainApplication
        return ViewModelProviders.of(
            this,
            ViewModelFactory(
                app.apiHelper,
                app.preferenceHelper,
                app.dbHelper
            )
        ).get(MainViewModel::class.java)
    }
}