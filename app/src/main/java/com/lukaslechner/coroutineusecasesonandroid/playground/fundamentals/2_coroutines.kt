package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

fun main() = runBlocking{
    println("main starts")
    joinAll(
        //async is a special coroutine builder that starts
        // a new coroutine
        //below coroutines will run concurrently
        async { coroutine(1, 500) },
        async { coroutine(2, 300) }
    )
    println("main ends")

}

/**
 * suspend functions are functions
 * that can be suspended(paused) and resumed at a later time
 *
 * suspend fn are paused at suspension points(when call another
 * suspend fn -> gray arrow icon)
 *
 * suspend functions can only be called
 * from other suspend functions or from coroutines
 * (this fn is called from a coroutine)
 *
 * suspend functions can be used for doing long
 * running operations in the background (access DB or network calls)
 *
 * suspend functions can call regular methods also
 * regular methods = methods without suspend modifier
 *
 * regular code needs to start a coroutine to call suspend functions
 */
suspend fun coroutine(number: Int, delay: Long){
    //Coroutine 1 and Coroutine 2 will run on the same thread
    //Coroutine 1 and Coroutine 2 will run on the main thread
    //bec. they inherit the coroutineScope from its parent Coroutine which is (runBlocking)
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    delay(delay) // suspend functions can be suspended/paused
                 // at every suspension point
                 // at calling another suspended functions
                 // gray arrow means this is a suspended fn (a suspension point)
    println("Coroutine $number has finished")
}