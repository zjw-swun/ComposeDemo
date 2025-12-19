package com.zjw.compose.util

import androidx.compose.runtime.Composable

class Composition {
    private val slotTable = SlotTable()
    private val composer = Composer(slotTable)
    private var content: ((Composer) -> Unit)? = null

    fun setContent(block: (Composer) -> Unit) {
        content = block
        recompose()
    }

    fun recompose() {
        println("=== recomposition ===")
        slotTable.reset()
        content?.invoke(composer)
        println("slots = $slotTable\n")
    }
}