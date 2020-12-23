package com.esys.mviinitialproject

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import kotlinx.coroutines.async
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScopeRule = MainCoroutineScopeRule()

    @Mock
    lateinit var observer: Observer<MainState>

    @Captor
    private lateinit var captor: ArgumentCaptor<MainState>

    @Mock
    private lateinit var mainRepository: MainRepository

    @Test
    fun fetchUserDetails_whenGettingFromNetwork_shouldReturnSuccess() {
        //Arrange
        val expectedUser = User(1, "TEST", "test@test.com", "test.jpg")
        val expectedReturn = ResultWrapper.Success(expectedUser)
        val expectedNumberOfInvocation = 3
        val viewModel = MainViewModel(mainRepository)

        //Act
        runBlockingTest {
            `when`(mainRepository.getUserDetail(expectedUser.id)).thenReturn(expectedReturn)
            viewModel.mainState.asLiveData().observeForever(observer)
            viewModel.mainIntent.send(MainIntent.GetUserDetails(expectedUser.id))
        }

        //Assert
        verify(observer, times(expectedNumberOfInvocation)).onChanged(captor.capture())
        captor.allValues.run {
            assertEquals(MainState.Idle, this@run[0])
            assertEquals(MainState.Loading, this@run[1])
            assertEquals(MainState.LoadUserDetails(expectedUser), this@run[2])
        }
    }

    @Test
    fun fetchUsers_whenFetchingFromNetwork_shouldReturnSuccess() {
        //Arrange
        val expectedResult = emptyList<User>()
        val expectedReturn = ResultWrapper.Success(expectedResult)
        val expectedNumberOfInvocation = 3
        val viewModel = MainViewModel(mainRepository)

        //Act
        runBlockingTest {
            `when`(mainRepository.getUsers()).thenReturn(expectedReturn)
            viewModel.mainState.asLiveData().observeForever(observer)
            viewModel.mainIntent.send(MainIntent.FetchUsers)
        }

        //Assert
        verify(observer, times(expectedNumberOfInvocation)).onChanged(captor.capture())
        captor.allValues.run {
            assertEquals(MainState.Idle, this@run[0])
            assertEquals(MainState.Loading, this@run[1])
            assertEquals(MainState.LoadUsers(expectedResult), this@run[2])
        }
    }
    @Test
    fun fetchUsers_whenGettingFromNetwork_shouldReturnNetworkError() {
        //Arrange
        val expectedReturn = ResultWrapper.NetworkError
        val viewModel = MainViewModel(mainRepository)

        //Act
        runBlockingTest {
            `when`(mainRepository.getUsers()).thenReturn(expectedReturn)
            viewModel.mainState.asLiveData().observeForever(observer)
            viewModel.mainIntent.send(MainIntent.FetchUsers)
        }

        //Assert
        verify(observer, times(3)).onChanged(captor.capture())
        captor.allValues.run {
            assertEquals(MainState.Idle, this@run[0])
            assertEquals(MainState.Loading, this@run[1])
            assertEquals(MainState.NoNetworkAvailableError, this@run[2])
        }
    }
    @Test
    fun fetchUserDetails_whenGettingFromNetwork_shouldReturnNetworkError() {
        //Arrange
        val expectedReturn = ResultWrapper.NetworkError
        val viewModel = MainViewModel(mainRepository)

        //Act
        runBlockingTest {
            `when`(mainRepository.getUserDetail(anyInt())).thenReturn(expectedReturn)
            viewModel.mainState.asLiveData().observeForever(observer)
            viewModel.mainIntent.send(MainIntent.GetUserDetails(anyInt()))
        }

        //Assert
        verify(observer, times(3)).onChanged(captor.capture())
        captor.allValues.run {
            assertEquals(MainState.Idle, this@run[0])
            assertEquals(MainState.Loading, this@run[1])
            assertEquals(MainState.NoNetworkAvailableError, this@run[2])
        }
    }
}