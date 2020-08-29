package com.jem.tetracubeengine.pieces

import com.jem.tetracubeengine.model.Pair

/**
 * Base class for the Piece design
 */
abstract class Piece {

    /**
     * Name of the piece, just for reference
     */
    abstract val pieceName: String

    /**
     * Get initial body (state) of the piece
     */
    abstract fun getInitialBody(): ArrayList<Pair<Int, Int>>

    /**
     * Coordinates of the blocks in the piece
     */
    var body: ArrayList<Pair<Int, Int>>

    /**
     * Lowest y value for each column in the body of the piece
     */
    val skirt: ArrayList<Int> = ArrayList()


    /**
     * Rightmost block of the piece
     */
    var width: Int = 0

    /**
     * Topmost block of the piece
     */
    var height: Int = 0

    init {
        body = getInitialBody()
        width = 0
        height = 0
        for (block in body) {
            if (width < block.first + 1) {
                width = block.first + 1
            }
            if (height < block.second + 1) {
                height = block.second + 1
            }
        }
        // Update the values of the variables first
        updateSkirt()
    }

    /**
     * Rotate the piece clockwise
     */
    fun rotateClockwise() {
        for (block in body) {
            val newFirst = block.second
            val newSecond = ((width - 1) - block.first)
            block.first = newFirst
            block.second = newSecond
        }
        width = height.also { height = width }
        updateSkirt()
    }

    /**
     * Rotate the piece counterclockwise
     */
    fun rotateCounterClockwise() {
        for (block in body) {
            val newFirst = ((height - 1) - block.second)
            val newSecond = block.first
            block.first = newFirst
            block.second = newSecond
        }
        width = height.also { height = width }
        updateSkirt()
    }

    /**
     * To be called when body changes, so that the values of skirt can be updated
     */
    private fun updateSkirt() {
        skirt.clear()
        repeat(width) {
            skirt.add(-1)
        }
        for (block in body) {
            if (skirt[block.first] < block.second) {
                skirt[block.first] = block.second
            }
        }
    }

}