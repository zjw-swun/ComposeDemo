package com.zjw.compose.util
class State<T>(initial: T, private val onChange: () -> Unit) {
    private var _value = initial

    var value: T
        get() = _value
        set(v) {
            if (_value != v) {
                _value = v
                onChange()
            }
        }
}

fun <T> mutableStateOf(
    initial: T,
    onChange: () -> Unit
): State<T> = State(initial, onChange)