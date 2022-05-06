package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//RunBlocking only blocks the current thread
//if we are calling a suspend function inside the runBlocking
//block, but if we are starting a coroutine inside the runBlocking block
//it will not block the thread and it will not wait for the coroutine to finish its work
fun main() = runBlocking<Unit> {
    val statTime = System.currentTimeMillis()

//    val result3 = networkCall(3) //runBlocking blocked the thread here(calling a suspend function)
    // runBlocking will block here, after the suspend function is completed
    // and the result is received the coroutine will execute the rest of the code
//    println("$result3 received at ${elapsedTime(statTime)}")

    // This list was made as a workaround to
    // save the result returned from each coroutine
    // but this is not the best solution
    // because now we are sharing the state
    // between 2 parallel coroutines
    val resultList = mutableListOf<String>()
    // implementing two coroutines in parallel
    // We can start coroutines in parallel using launch
    // coroutine builder but we can't access the result
    // outside the launch coroutine builder

    //runBlocking will not block the thread here and will not wait for the coroutine to complete
    val job1 = launch {
        val result1 = networkCall(1, 500)
        resultList.add(result1)
        println("Result1: $result1 is received at ${elapsedTime(statTime)} ms")
    }

    //runBlocking will not block the thread here and will not wait for the coroutine to complete
    val job2 = launch {
        val result2 = networkCall(2, 300)
        resultList.add(result2)
        println("Result2: $result2 is received at ${elapsedTime(statTime)} ms")
    }
    /**
     * When trying to remove both job1.join(), job2.join() lines
     * the result was received but the result was not added to the list
     * if the join() function is called on either job1 or job2 both results
     * will be added in the list, if both lines were removed
     * both results will not be added to the list
     *
     * leh by7sal keda 3ashan el app byroo7 ynafez awel line
     * fe kol coroutine w msh bystanaha te5las
     * we yeroo7 yenfaz ba2et el lines ely fel app
     * fa yeroo7 ye3ml println lel resultList abl ma el resultList
     * tetmely bel data aslun lw garabt teshel el 2 join() commands
     * hytba3 el resultList el awel Then later the result will be
     * received in each coroutine -> batba3 el list abl ma tetmely
     *
     * Result 3 received at 525
     * [] at 541
     * Result1: Result 1 is received at 1046 ms
     * Result2: Result 2 is received at 1047 ms
     *
     * ta2reban lama ba3ml join() l wa7da mnhom btb2a el result bta3t el tanya gat
     * 3ashan el etnen parallel w byt25ro bnfs el delay
     *
     * lw 3amlt join le job2 only w 5alet job2 te5las before job1
     * ya3ny job2 delay 300ms
     * w job1 delay 500
     * b keda el runBlocking hat3ml launch lel coroutine w tenady awel line bs fe job1
     * w teroo7 tenafez job2 and waits for job 2 to complete
     * then prints the list with Result 2 only and the program ends
     * before Result1 is even added in the resultList
     */
//    job1.join() // to make the runBlocking coroutine wait for this coroutine to complete
    job2.join() // to make the runBlocking coroutine wait for this coroutine to complete
    println("$resultList at ${elapsedTime(statTime)}")
}

suspend fun networkCall(number: Int, delay:Long): String {
    delay(delay)
    return "Result $number"
}

fun elapsedTime(startTime: Long) = System.currentTimeMillis() - startTime