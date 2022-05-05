package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch
import timber.log.Timber

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
class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading
        //No need to switch thread while calling retrofit
        //retrofit switches threads internally(it provides main safety)
        //it is safe to call Retrofit suspend functions on the main thread without blocking it

        //viewModel scope runs all coroutines on the main thread
        //that's why we can assign the result to LiveData
        // if you try to update LiveData from a background thread
        // app will throw exceptions

        //viewModelScope cancels coroutines when the user leaves the screen
        // and the viewModel gets cleared
        viewModelScope.launch { //This line launches a new coroutine
            /*
                These are two sequential network requests using coroutines

                This an ordinary imperative code that is
                executed sequentially from top to bottom

               for adding another sequential call -> just add another call to another suspend fn
               for making parallel calls -> use async coroutine builder
               for retry behavior -> use high order functions

              (alt + ctrl + t) for try/catch shortcut
                 */
            try {
                //making first network call
                //calling first suspend function
                //here the coroutine will get suspended until the network response is returned
                val recentVersions = mockApi.getRecentAndroidVersions()
                val apiLevel = recentVersions.last().apiLevel

                // making second network call
                // calling the second suspend function
                // here the coroutines is suspended/paused until the network response is returned
                val versionFeatures = mockApi.getAndroidVersionFeatures(apiLevel)
                uiState.value = UiState.Success(versionFeatures)
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error("Network Request failed!")
            }

        }
    }
}