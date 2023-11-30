package dev.neo8.adventofcode.days

interface IDay {

    fun forceTest() = true

    fun common(input: Array<String>)

    fun part01(): String

    fun part02(): String

}