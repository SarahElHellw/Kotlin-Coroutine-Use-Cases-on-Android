package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main()= runBlocking {
    println("main starts")
    repeat(1_000_000){
        launch {
            println("Coroutine $it starts")
            delay(5000)
            println("Coroutine $it ends")
        }
    }
    println("main ends")
}