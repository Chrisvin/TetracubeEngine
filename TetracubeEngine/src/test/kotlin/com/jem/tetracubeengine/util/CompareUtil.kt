package com.jem.tetracubeengine.util

import com.jem.tetracubeengine.model.Pair

object CompareUtil {
    fun compareBodyState(
        actualBody: ArrayList<Pair<Int, Int>>,
        expectedBody: ArrayList<Pair<Int, Int>>
    ): Boolean {
        println("Actual: ${actualBody.joinToString()} :: Expected: ${expectedBody.joinToString()}")
        if (actualBody.size != expectedBody.size) {
            return false
        }
        for (i in 0 until expectedBody.size) {
            if (!actualBody[i].equals(expectedBody[i])) {
                return false
            }
        }
        return true
    }
}