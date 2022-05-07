package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {

    val startTime = System.currentTimeMillis()

    /**
     * What is the difference between launch and async coroutine builders
     * launch returns a job object -> you can't access the result of the coroutine outside the launch block
     * async returns a Deferred<T> object which is a subtype of job but holds the result of the coroutine
     * when you call deferred.await() await() is a suspend function when called it waits for the coroutine
     * to complete and return a value, the last line in the async block should contain the result of the coroutine
     * if this is not done async block will return a Unit object
     *
     * with the async block the shared mutable state problem is solved
     * shared mutable state problem -> multiple coroutines accessing and modifying a shared variable
     * every thing done with a job can be done with a Deferred<T> object
     */
    //async coroutine is started instantly
    val deferred1 = async {
        val result1 = networkCall(1)
        println("Result1 received $result1 at ${elapsedTime(startTime)} ms")
        result1
    }

    val deferred2 = async {
        val result2 = networkCall(2)
        println("Result2 received $result2 at ${elapsedTime(startTime)} ms")
        result2
    }

    /**
     * by just starting the async block the deferred object doesn't hold any result yet
     * because the network call takes 500ms
     * we have to call await() function on the deferred object
     *
     * deferred1.await() -> await() is a suspend function that should be called
     * inside another suspend function or inside a coroutine like runBlocking here
     *
     * the runBlocking block blocks the underlying thread if a suspend function is called
     * until the suspend function completes its work but this behavior is different with the await()
     * function , runBlocking block doesn't block a coroutine that is either launched by launch/async
     * coroutine builders
     *
     * await() is a suspend function that is called on the deferred object returned from the async block
     *
     * deferred.await() at this line which is a suspend function
     * runBlocking coroutine will suspend until the first async coroutine completes and returns a result
     * then runBlocking coroutine is resumed and continues execution of the rest of the code
     *
     * if i called deferred1.await() -> but without returning the result at the last ine
     * of the async block, nothing will be returned and deferred.await() will return Unit
     *
     * Deferred<T> object is a subtype of Job it can do everything you can do with a job
     * deferred1.cancel()
     * deferred1.join() -> will only wait for the job to complete but it doesn't return a result ->
     * if you used join in resultList the result will be Unit
     * deferred1.await()-> will wait wait for the job to complete and produce a result/value
     * deferred1.start() -> in case you start a coroutine lazily async(start = CoroutineStart.LAZY)
     */
    val resultList = listOf(deferred1.await(), deferred2.await())
    println("ResultList: $resultList at ${elapsedTime(startTime)} ms")
}

suspend fun networkCall(number: Int): String {
    delay(500)
    return "Result $number"
}
