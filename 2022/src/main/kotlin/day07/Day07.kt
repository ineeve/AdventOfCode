package day07

import java.io.File

data class AOCFile(val name: String, val size: Int)

class AOCDir(val name: String) {
    val files: ArrayList<AOCFile> = ArrayList()
    val subdirs: ArrayList<AOCDir> = ArrayList()
    var parentDir: AOCDir? = null

    fun addSubDir(dir: AOCDir) {
        subdirs.add(dir)
    }
    fun addFile(file: AOCFile) {
        files.add(file)
    }
    fun getSize(): Int {
        return files.sumOf { it.size } + subdirs.sumOf { it.getSize() }
    }

    fun getAllSubdirs(): ArrayList<AOCDir> {
        val result = ArrayList<AOCDir>()
        if (subdirs.isEmpty()) return result
        result.addAll(subdirs.flatMap { it.getAllSubdirs() })
        result.addAll(subdirs)
        return result
    }
}

fun main() {
    val input = File("src/main/kotlin/day07/input07.txt").readText()
    val rootDir = AOCDir("/")
    var currentDir = rootDir
    val lines = input.split(System.lineSeparator())
    lines.subList(1, lines.size).forEach{
        if (it.startsWith("$ cd")) {
            currentDir = if (it.endsWith("..")) {
                currentDir.parentDir!!
            } else {
                val newDirName = it.split(" ").last().trim()
                currentDir.subdirs.find { d -> d.name == newDirName }!!
            }
        } else if (it.startsWith("dir")) {
            val dirToCreate = it.split(" ").last().trim()
            val newDir = AOCDir(dirToCreate)
            newDir.parentDir = currentDir
            currentDir.addSubDir(newDir)
        } else if (!it.startsWith("$ ls")) {
            // file
            val fileProps = it.split(" ")
            val newFile = AOCFile(fileProps[1], fileProps[0].toInt())
            currentDir.addFile(newFile)
        }
    }
    val allDirs = flattenTree(rootDir)
    val result1 = allDirs.filter { it.getSize() < 100000 }.sumOf { it.getSize() }
    println("Result1: $result1")
    val requiredSpace = (30000000 - (70000000 - rootDir.getSize()))
    val result2 = allDirs.filter { it.getSize() > requiredSpace }.minByOrNull { it.getSize() }!!
    println("Result2: Delete dir ${result2.name} with size ${result2.getSize()}")
}

fun flattenTree(rootDir: AOCDir): HashSet<AOCDir> {
    val allDirs = HashSet<AOCDir>()
    allDirs.add(rootDir)
    allDirs.addAll(rootDir.getAllSubdirs())
    println(allDirs)
    return allDirs
}