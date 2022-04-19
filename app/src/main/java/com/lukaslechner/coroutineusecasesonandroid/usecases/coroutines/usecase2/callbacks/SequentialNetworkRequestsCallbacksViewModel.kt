package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private var getAndroidVersionCall: Call<List<AndroidVersion>>? = null
    private var getVersionFeaturesCall: Call<VersionFeatures>? = null

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading
        getAndroidVersionCall = mockApi.getRecentAndroidVersions()
        getAndroidVersionCall!!.enqueue(object : Callback<List<AndroidVersion>>{
            override fun onResponse(
                call: Call<List<AndroidVersion>>,
                response: Response<List<AndroidVersion>>
            ) {
                if(response.isSuccessful){
                    val lastAndroidVersion = response.body()!!.last()
                    getVersionFeaturesCall = mockApi.getAndroidVersionFeatures(lastAndroidVersion.apiLevel)
                    getVersionFeaturesCall!!.enqueue(object : Callback<VersionFeatures>{
                        override fun onResponse(
                            call: Call<VersionFeatures>,
                            response: Response<VersionFeatures>
                        ) {
                            if(response.isSuccessful){
                                val versionFeatures = response.body()!!
                                uiState.value = UiState.Success(versionFeatures)
                            }else{
                                uiState.value = UiState.Error("Network request failed 4XX/5XX")
                            }
                        }

                        override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                            uiState.value = UiState.Error("Something unexpected happened!/ error creating request / error parsing response")
                        }
                    })
                }else{
                    uiState.value = UiState.Error("Network Request failed 4XX/5XX")
                }
            }

            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                uiState.value = UiState.Error("Something unexpected happened/ error creating request/ error parsing response")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        getAndroidVersionCall?.cancel()
        getVersionFeaturesCall?.cancel()
    }
}