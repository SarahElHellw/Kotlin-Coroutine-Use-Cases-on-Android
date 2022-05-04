package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.*

fun main() = runBlocking {
    println("main starts")
    joinAll(
        async { switchingCoroutineThread(1, 500) },
        async { switchingCoroutineThread(2, 300) }
    )
    println("main ends")
}

suspend fun switchingCoroutineThread(number: Int, delay: Long) {
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    delay(delay)
    withContext(Dispatchers.Default) {
        println("Coroutine $number has finished on ${Thread.currentThread().name}")
    }
}