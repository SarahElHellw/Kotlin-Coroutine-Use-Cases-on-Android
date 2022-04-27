package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch

class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading
        /**
         * 1- The main characteristic here in the coroutine
         * is that the code is written in an imperative fashion
         * and is executed sequentially with no need
         * to pass retrofit callbacks or to construct a reactive stream
         *
         * 2- The implementation is concise compared to callbacks/RxJava implementations
         *
         * 3- We don't have to worry about lifecycle management because the coroutine
         * gets cancelled automatically when the viewModel gets cleared
         *
         * 4- Calls to suspend functions of Retrofit are main safe, we can call them on the main-thread
         * without blocking it, Retrofit it self performs the actual network requests on a background thread
         *
         * Coroutines gives us both, the simplicity of conventional synchronous code together with the power
         * of asynchronous programming
         */
        viewModelScope.launch{ //This line launches a new coroutine

            // This an ordinary imperative code that is
            // executed sequentially from top to bottom

            //for adding another sequential call -> just add another call to another suspend fn
            //for making parallel calls -> use async coroutine builder
            //for retry behavior -> use high order functions

            // (alt + ctrl + t) for try/catch shortcut
            try {
                /*
                These are two sequential network requests using coroutines
                 */
                val recentVersions = mockApi.getRecentAndroidVersions() //  the first network call
                val mostRecentVersion = recentVersions.last()
                val featuresOfMostRecentVersion = mockApi.getAndroidVersionFeatures(mostRecentVersion.apiLevel)
                uiState.value = UiState.Success(featuresOfMostRecentVersion)
            } catch (e: Exception) {
                uiState.value = UiState.Error("Network request failed!")
            }

        }
    }
}