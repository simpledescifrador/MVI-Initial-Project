package com.esys.mviinitialproject

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.esys.mviinitialproject.data.local.database.DbHelper
import com.esys.mviinitialproject.data.local.preference.PreferenceHelper
import com.esys.mviinitialproject.data.model.User
import com.esys.mviinitialproject.data.network.api.ApiHelper
import com.esys.mviinitialproject.data.network.api.ResultWrapper
import com.esys.mviinitialproject.data.repository.MainRepository
import com.esys.mviinitialproject.ui.main.MainViewModel
import com.esys.mviinitialproject.ui.main.MainViewModel.MainIntent
import com.esys.mviinitialproject.ui.main.MainViewModel.MainState
import com.esys.mviinitialproject.util.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScopeRule = MainCoroutineScopeRule()

    @Mock
    lateinit var apiHelper: ApiHelper

    @Mock
    lateinit var dbHelper: DbHelper

    @Mock
    lateinit var preferenceHelper: PreferenceHelper

    @Mock
    lateinit var observer: Observer<MainState>

    @Captor
    private lateinit var captor: ArgumentCaptor<MainState>


    @Test
    fun getUserDetails_whenGettingFromNetwork_shouldReturnSuccess() = runBlockingTest {
        val user = User(1, "Ed", "ed@email.com", "http://test.jpg")
        val viewModel = MainViewModel(MainRepository(apiHelper, preferenceHelper, dbHelper))
        viewModel.mainState.asLiveData().observeForever(observer)
        `when`(apiHelper.getUserDetail(user.id)).thenReturn(ResultWrapper.Success(user))
        viewModel.mainIntent.send(MainIntent.GetUserDetails(user.id))
        verify(observer, times(3)).onChanged(captor.capture())
        val values: MutableList<MainState> = captor.allValues
        assertEquals(MainState.Idle, values[0])
        assertEquals(MainState.Loading, values[1])
        assertEquals(MainState.LoadUserDetails(user), values[2])
    }

    @Test
    fun getUsers_whenFetchingFromNetwork_shouldReturnSuccess() = runBlockingTest {
        val expectedResult = emptyList<User>()
        val viewModel = MainViewModel(MainRepository(apiHelper, preferenceHelper, dbHelper))
        viewModel.mainState.asLiveData().observeForever(observer)
        `when`(apiHelper.getUsers()).thenReturn(ResultWrapper.Success(expectedResult))
        viewModel.mainIntent.send(MainIntent.FetchUsers)
        delay(5000)
        verify(observer, times(3)).onChanged(captor.capture())
        val values: MutableList<MainState> = captor.allValues
        assertEquals(MainState.Idle, values[0])
        assertEquals(MainState.Loading, values[1])
        assertEquals(MainState.LoadUsers(expectedResult), values[2])
    }
}