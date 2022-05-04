package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val job = launch(start = CoroutineStart.LAZY) { //default is that coroutine is started immediately
        networkRequest()
        println("result received")
    }
    job.start()
    job.join() // suspends the coroutine until this job is completed
    println("runBlocking ends")
}

suspend fun networkRequest(): String {
    delay(500)
    return "result"
}