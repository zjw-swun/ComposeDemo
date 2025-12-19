package com.zjw.compose.util

class Composer(private val slots: SlotTable) {

    fun startGroup() = slots.startGroup()
    fun endGroup() = slots.endGroup()
    fun skipGroup() = slots.skipGroup()

    fun changed(value: Any?): Boolean {
        val old = slots.next()
        return if (old != value) {
            slots.update(value)
            true
        } else {
            false
        }
    }

    fun <T> remember(factory: () -> T): T {
        val old = slots.next()
        return if (old == null) {
            val v = factory()
            slots.update(v)
            v
        } else {
            old as T
        }
    }
}