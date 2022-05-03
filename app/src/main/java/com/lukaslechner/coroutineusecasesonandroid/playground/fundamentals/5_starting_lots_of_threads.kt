package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlin.concurrent.thread
//in the tutorial out of memory exception was thrown
//on my machine this code didn't throw exception
//but took long time to finish
fun main(){
    repeat(1_000_000){
        thread {
            Thread.sleep(5000)
            println(".")
        }
    }
}