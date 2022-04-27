package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7.rx

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class TimeoutAndRetryRxViewModel(
    private val api: RxMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val disposables = CompositeDisposable()

    fun performNetworkRequest() {

        uiState.value = UiState.Loading

        val timeout = 1000L
        val numberOfRetries = 2

        /**
         * Zip operator combines the emissions of (multiple) Observables together via a
         * specified function and emit single items for each combination
         * based on the results of this function
         *
         * Example:
         * --------
         *         api1: getCricketFans(): List<User>
         *         api2: getFootballFans(): List<User>
         *         use the zip operator to perform both network calls in parallel
         *         and give the zip operator a utility function that finds
         *         the list of users who love both sports
         *         https://www.youtube.com/watch?v=iYD_Onb2wSU
         *
         *         zip operator can be called on observable/single etc. objects
         *         and also the timeout and retry operators can be called on observable/single objects
         *         here zip operator is applied to (Single) object and in the video the zip operator
         *         is applied to (Observable) object
         *
         * Advantages of Zip operator:
         * ---------------------------
         * 1- it performs all the   tasks(here network calls) in parallel
         * 2- it is not limited to 2 sources only i saw in documentation up to 9 sources
         * 3- it returns the result of all parallel tasks in a single callback function when all the
         *    tasks are completed, which makes it easy to combine the returned results from different
         *    sources or apply some business logic to them
         */

        Single.zip( //Zip operator
            api.getAndroidVersionFeatures(27)
                .timeout(timeout, TimeUnit.MILLISECONDS)
                .retry { x, e ->
                    Timber.e(e)
                    x <= numberOfRetries
                },
            api.getAndroidVersionFeatures(28)
                .timeout(timeout, TimeUnit.MILLISECONDS)
                .retry { x, e ->
                    Timber.e(e)
                    x <= numberOfRetries
                },
            BiFunction<VersionFeatures, VersionFeatures, List<VersionFeatures>> { versionFeaturesOreo, versionFeaturesPie ->
                listOf(versionFeaturesOreo, versionFeaturesPie)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                // lazem adeha subscriber or observer 3ashan y observe 3al observable stream of data
                // hena onError()/onSuccess() 3ashan ba3ml subscribe 3ala Single object
                // in the video we used subscribe fn( w edenaha as input parameter Observer -> onSubscribe/onNext/onError/OnComplete
                // w kan bysta2bel el result w yetb3ha fel onNext())
                onSuccess = { versionFeatures ->
                    uiState.value = UiState.Success(versionFeatures)
                },
                onError = { error ->
                    Timber.e(error)
                    uiState.value = UiState.Error("Network Request failed")
                })
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}