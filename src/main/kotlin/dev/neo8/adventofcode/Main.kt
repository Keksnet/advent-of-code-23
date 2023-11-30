package dev.neo8.adventofcode

import dev.neo8.adventofcode.days.Day00
import dev.neo8.adventofcode.days.IDay
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

fun main(args: Array<String>) {
    val day = run {
        if (args.isNotEmpty()) {
            if (args.size == 1) {
                val suppliedDay = args[0].toIntOrNull()
                if (suppliedDay == null || suppliedDay < 1 || suppliedDay > 25) {
                    println("Invalid day given!")
                } else {
                    var daySupplied = suppliedDay.toString()
                    while (daySupplied.length < 2) {
                        daySupplied = "0$daySupplied"
                    }
                    return@run daySupplied
                }
            }
        }
        var dayEnv = System.getenv("DAY")
        if (dayEnv != null) {
            while (dayEnv.length < 2) {
                dayEnv = "0$dayEnv"
            }
            return@run dayEnv
        }
        var dayWeek = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
        while (dayWeek.length < 2) {
            dayWeek = "0$dayWeek"
        }
        dayWeek
    }

    var dayFile = Path.of("input")
    if (!Files.exists(dayFile)) {
        Files.createDirectory(dayFile)
    }

    dayFile = dayFile.resolve("day$day")
    if (!Files.exists(dayFile)) {
        Files.createDirectory(dayFile)
    }

    createIfNotExists(dayFile.resolve("input.txt"))
    createIfNotExists(dayFile.resolve("sample.txt"))
    for (i in 1..2) {
        createIfNotExists(dayFile.resolve("solution_$i.txt"))
    }

    var sample = Files.readAllLines(dayFile.resolve("sample.txt"))
    if (sample.size == 0) {
        println("Sample is empty. STOP.")
        return
    }
    for (i in 1..2) {
        if (i == 2 && Files.exists(dayFile.resolve("sample_2.txt"))) {
            println("Different sample for Part 2 detected.")
            sample = Files.readAllLines(dayFile.resolve("sample_2.txt"))
            if (sample.size == 0) {
                println("Second sample is empty. STOP.")
                return
            }
        }
        val dayInstance = retrieveDayInstance(day)
        val solution = Files.readString(dayFile.resolve("solution_$i.txt")).trim().replace("\r\n", "\n")
        if (solution.isBlank()) {
            println("Solution for Part $i is empty. Skipping...")
            continue
        }
        dayInstance.common(sample.toTypedArray())
        val result = (if (i == 1) dayInstance.part01() else dayInstance.part02()).trim().replace("\r\n", "\n")
        if (result == solution) {
            println("Test for Part $i SUCCESS.")
            continue
        }
        println("Expected '$solution' got '$result' instead.")
        if (dayInstance.forceTest()) {
            println("Test for Part $i FAILED. Stop.")
            return
        }
        println("Test for Part $i FAILED.")
    }

    val dayInstance = retrieveDayInstance(day)
    val input = Files.readAllLines(dayFile.resolve("input.txt"))
    if (input.size == 0) {
        println("Input is empty. STOP.")
        return
    }

    dayInstance.common(input.toTypedArray())
    println("Solution (Part 1): ${dayInstance.part01()}")
    println("Solution (Part 2): ${dayInstance.part02()}")
    println("Done.")
}

fun retrieveDayInstance(day: String): IDay {
    return try {
        val dayClass = Class.forName("dev.neo8.adventofcode.days.Day$day")
        dayClass.getConstructor().newInstance() as IDay
    } catch (_: Exception) {
        Day00()
    }
}

fun createIfNotExists(path: Path) {
    if (!Files.exists(path)) {
        Files.createFile(path)
    }
}