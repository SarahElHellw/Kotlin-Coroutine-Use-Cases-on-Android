package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlin.concurrent.thread

fun main () {
    println("main starts")
    coroutinesWithThreads(1, 500)
    coroutinesWithThreads(2, 300)
    Thread.sleep(1000)// if this line is removed
//    main starts and ends before the threads are finished
    println("main ends")
}

fun coroutinesWithThreads(number: Int, delay: Long){
    thread {
        println("Coroutine $number starts work")
        Thread.sleep(delay)
        println("Coroutine $number has finished")
    }


    /**
     * printlns with Thread.sleep(1000)
     *
    main starts
    Coroutine 1 starts work
    Coroutine 2 starts work
    Coroutine 2 has finished
    Coroutine 1 has finished
    main ends
     */


    /**
     * printlns without Thread.sleep(1000)
    main starts
    main ends
    Coroutine 1 starts work
    Coroutine 2 starts work
    Coroutine 2 has finished
    Coroutine 1 has finished
     */
}