package com.zjw.compose.util

// 位运算标记
object Changed {
    const val UNKNOWN = 0
    const val UNCHANGED = 2   // 010
    const val CHANGED = 4     // 100
    const val FORCE = 6       // 110
}

// SlotTable + Group 支持 key() 和嵌套
class SlotTable {

    private val slots = mutableListOf<Any?>()
    private var index = 0

    private val groupStack = ArrayDeque<Int>()
    private val groupSizes = ArrayDeque<Int>()

    fun startGroup() {
        groupStack.addLast(index)
    }

    fun endGroup() {
        val start = groupStack.removeLast()
        val size = index - start
        groupSizes.addLast(size)
    }

    fun skipGroup() {
        val size = groupSizes.removeLast()
        index += size
    }

    fun next(): Any? {
        val v = if (index < slots.size) slots[index] else null
        index++
        return v
    }

    fun update(value: Any?) {
        val i = index - 1
        if (i < slots.size) slots[i] = value
        else slots.add(value)
    }

    fun reset() {
        index = 0
        groupStack.clear()
        groupSizes.clear()
    }

    override fun toString(): String = slots.toString()
}