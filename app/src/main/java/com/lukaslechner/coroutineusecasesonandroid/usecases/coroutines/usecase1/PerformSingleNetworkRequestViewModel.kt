package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performSingleNetworkRequest() {
        uiState.value = UiState.Loading

        // All coroutines started in viewModelScope are started by default on main thread
        // That's why we are able to assign the result to LiveData
        // if we try to do this from background thread app will throw an exception
        // viewModelScope cancels coroutines when the user leaves the screen and
        // viewModel gets cleared
//        viewModelScope.launch(Dispatchers.IO) -> LiveData throws exception Cannot invoke setValue on a background thread
        viewModelScope.launch{
            val recentVersions = mockApi.getRecentAndroidVersions()
            uiState.value = UiState.Success(recentVersions)
        }
    }
}