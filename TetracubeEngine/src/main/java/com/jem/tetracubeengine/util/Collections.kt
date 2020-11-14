package com.jem.tetracubeengine.util

fun <T> arrayListOf(size: Int, value: T): ArrayList<T> = arrayListOf<T>().apply {
    repeat(size) {
        add(value)
    }
}