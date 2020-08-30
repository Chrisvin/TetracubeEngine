package com.jem.tetracubeengine

data class TetracubeSettings(
    val boardWidth: Int = TetracubeBoard.DEFAULT_WIDTH,
    val boardHeight: Int = TetracubeBoard.DEFAULT_HEIGHT,
    val boardBreadth: Int = TetracubeBoard.DEFAULT_BREADTH,
    val initialTickDelay: Long = DEFAULT_INITIAL_TICK_DELAY,
    val tickInterval: Long = DEFAULT_TICK_INTERVAL
) {
    companion object {
        const val DEFAULT_INITIAL_TICK_DELAY: Long = 0 //ms
        const val DEFAULT_TICK_INTERVAL: Long = 1000 //ms
    }
}