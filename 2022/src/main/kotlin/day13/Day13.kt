package day13

import java.io.File

enum class CompareRes {
    Wrong,
    Right,
    Continue
}

enum class ValType { List, Int }
interface ListElement {
    val valType: ValType
    val valueInt: Int?
    val valueList: List<ListElement>?
    fun compare(otherInt: D13Int): CompareRes
    fun compare(otherList: D13List): CompareRes
    fun compare(other: ListElement): CompareRes {
        if (other.valType == ValType.Int) return compare(other as D13Int)
        return compare(other as D13List)
    }
}

class D13Int(override val valueInt: Int) : ListElement {
    override val valType = ValType.Int
    override val valueList: List<D13List>? = null

    override fun compare(otherInt: D13Int): CompareRes {
        if (valueInt == otherInt.valueInt) return CompareRes.Continue
        if (valueInt < otherInt.valueInt) return CompareRes.Right
        return CompareRes.Wrong
    }

    override fun compare(otherList: D13List): CompareRes {
        return D13List(arrayListOf(this)).compare((otherList))
    }
}

class D13List(override val valueList: List<ListElement>) : ListElement, ArrayList<ListElement>(valueList) {
    override val valType = ValType.List
    override val valueInt: Int? = null

    override fun compare(otherInt: D13Int): CompareRes {
        return compare(D13List(arrayListOf(otherInt)))
    }

    override fun compare(otherList: D13List): CompareRes {
        var idx = 0
        if (this.isEmpty()) return CompareRes.Right
        if (otherList.isEmpty()) return CompareRes.Wrong
        var left = this[idx]
        var right = otherList[idx]
        var lastCompare = left.compare(right)
        while (lastCompare == CompareRes.Continue) {
            idx++;
            if (idx == this.size && idx == otherList.size) return CompareRes.Continue
            if (idx == this.size) return CompareRes.Right
            if (idx == otherList.size) return CompareRes.Wrong
            left = this[idx]
            right = otherList[idx]
            lastCompare = left.compare(right)
        }
        return lastCompare
    }
}

fun splitStringAtIndices(indices: List<Int> , str: String ): List<String> {
    var prevIndex = 1;
    val res = indices.map {
        val sub = str.substring(prevIndex, it)
        prevIndex = it+1
        sub
    } as ArrayList
    res.add(str.substring(prevIndex, str.length-1))
    return res
}

fun parseBrackets(line: String): D13List {
    var openBracketAcc = 0
    val commaIndices = (line.indices).map {
        if (line[it] == '[') {
            openBracketAcc++
        } else if (line[it] == ']') {
            openBracketAcc--
        }
        if(openBracketAcc == 1 && line[it] == ',') it else 0
    }.filter { it > 0 }
    val elements = splitStringAtIndices(commaIndices, line)
    val listElements: List<ListElement> = elements.map {
        if  (it.isEmpty()) return D13List(emptyList())
        if (it[0] == '[') parseBrackets(it)
        else D13Int(it.toInt())
    }
    return D13List(listElements)
}


fun main() {
    val input = File("src/main/kotlin/day13/input13.txt").readText()
   solve(input)
}

fun solve(input: String): Int {
    val comparisonResults = input.split("\r\n\r\n").map { pairStr ->
        val pair = pairStr.split(System.lineSeparator()).map { packet -> parseBrackets(packet) }
        pair[0].compare(pair[1])
    }
    if (comparisonResults.any { it == CompareRes.Continue }) {
        throw Exception("unexpected continue result")
    }
    val p1 = comparisonResults.foldIndexed(0) { idx, acc, res ->
        if (res == CompareRes.Right) acc + idx + 1
        else acc
    }
    println("P1: $p1")
    return p1
}