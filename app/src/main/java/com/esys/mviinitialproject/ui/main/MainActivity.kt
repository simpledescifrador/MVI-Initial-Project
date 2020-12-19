package com.esys.mviinitialproject.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.esys.mviinitialproject.data.local.database.DbHelperImp
import com.esys.mviinitialproject.data.local.preference.CommonPreference
import com.esys.mviinitialproject.data.local.preference.PreferenceHelperImpl
import com.esys.mviinitialproject.data.network.api.ApiHelperImpl
import com.esys.mviinitialproject.data.network.api.RetrofitBuilder
import com.esys.mviinitialproject.databinding.ActivityMainBinding
import com.esys.mviinitialproject.ui.base.BaseActivity
import com.esys.mviinitialproject.util.AppUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        lifecycleScope.launch {
        }
    }

    override fun observeViewModel() {
        lifecycleScope.launch {
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


    override fun setupViewModel(): MainViewModel = ViewModelProviders.of(
        this,
        MainViewModel.ViewModelFactory(
            ApiHelperImpl(RetrofitBuilder.apiService),
            PreferenceHelperImpl(CommonPreference(this)),
            DbHelperImp(this)
        )
    ).get(MainViewModel::class.java)
}