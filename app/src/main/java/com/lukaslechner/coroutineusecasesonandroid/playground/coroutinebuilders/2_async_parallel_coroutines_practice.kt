package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis()

    /**
     * Using async coroutine builder to do two network requests in parallel
     */
    val deferred1 = async {
        val result1 = doNetworkCall(1)
        println("Result1: $result1 received at ${elapsedTime(startTime)}")
        result1
    }

    val deferred2 = async {
        val result2 = doNetworkCall(2)
        println("Result2: $result2 received at ${elapsedTime(startTime)}")
        result2
    }

    val resultList = listOf( deferred1.await(), deferred2.await())
    println("ResultList: $resultList at ${elapsedTime(startTime)}")
}

suspend fun doNetworkCall(number:Int): String{
    delay(500)
    return "Result $number"
}