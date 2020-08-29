package com.jem.tetracubeengine

import com.jem.tetracubeengine.pieces.Piece

class TetracubeBoard(
    val width: Int = DEFAULT_WIDTH,
    val height: Int = DEFAULT_HEIGHT,
    val breadth: Int = DEFAULT_BREADTH
) {

    /**
     * The number of blocks filled in each width-row given (y, z)
     */
    var widths: Array<IntArray> = Array(height) { IntArray(breadth) }

    /**
     * The index of open spot above topmost block in each column (x, y)
     */
    var heights: Array<IntArray> = Array(width) { IntArray(breadth) }

    /**
     * The number of blocks filled in each breadth-row given (x, y)
     */
    var breadths: Array<IntArray> = Array(height) { IntArray(width) }

    /**
     * The maximum height of the currently placed blocks (largest value from heights array).
     * Used to clear the rows in an optimal manner
     */
    var maxHeight: Int = 0

    /**
     * The grid represents the possible block spots and determines which spots are filled/empty.
     * If spot at (x,y,z) is true, then it means that a block is at (x,y,z)
     */
    val grid: Array<Array<BooleanArray>> = Array(height) { Array(breadth) { BooleanArray(width) } }

    fun clearLayers(): Int {
        var layersCleared = 0
        // Layers to drop down (fromLayer -> toLayer) since some layers below were cleared
        val layersToDrop: ArrayList<Pair<Int, Int>> = ArrayList()
        for (h in 0 until height) {
            if (isLayerFull(h)) {
                layersCleared++
            } else if (layersCleared > 0) {
                layersToDrop.add(Pair(h, h - layersCleared))
            }
        }
        maxHeight -= layersCleared
        // Check if layers were cleared, and move blocks down accordingly
        if (layersCleared > 0) {
            for (layer in layersToDrop) {
                System.arraycopy(
                    grid[layer.first], 0,
                    grid[layer.second], 0,
                    grid[layer.first].size
                )
            }
            for (layer in 1..layersCleared) {
                for (b in 0 until breadth) {
                    for (w in 0 until width) {
                        grid[maxHeight + layer][b][w] = false
                        if (layer == 1) {
                            heights[w][b] -= layersCleared
                        }
                    }
                }
            }
        }
        return layersCleared
    }

    private fun isLayerFull(layer: Int): Boolean {
        for (w in widths[layer]) {
            if (w != width) {
                return false
            }
        }
        for (b in breadths[layer]) {
            if (b != breadth) {
                return false
            }
        }
        return true
    }

    /**
     * Enums depicting the possible status values that
     * can be gotten when attempting to place a piece in the board.
     */
    enum class PlaceStatus {
        /**
         * Successfully placed piece without issues
         */
        SUCCESS,

        /**
         * Placing piece caused layer to be filled (Seriously? Does this even happen!?)
         */
        LAYER_FILLED,

        /**
         * One or more blocks of the piece are outside the bounds of the board grid
         */
        OUT_OF_BOUNDS,

        /**
         * One or more blocks of the piece overlap with blocks already in the board grid
         * Basically, it's game over. :)
         */
        OVERLAP_WITH_EXISTING
    }

    companion object {
        val DEFAULT_WIDTH = 12
        val DEFAULT_HEIGHT = 24

        // TODO: Change value to non-zero and verify if impl works for Tetracube properly
        val DEFAULT_BREADTH = 0 /*12*/
    }
}