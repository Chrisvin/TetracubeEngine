package com.jem.tetracubeengine.util

import com.jem.tetracubeengine.pieces.*
import java.util.*

internal object PieceUtil {

    private val random: Random = Random()

    fun getRandomPiece(): Piece {
        // TODO: Change random implementation so that pieces don't repeat more than 2 times in a row?
        return when (random.nextInt(7)) {
            0 -> IPiece()
            1 -> JPiece()
            2 -> LPiece()
            3 -> OPiece()
            4 -> SPiece()
            5 -> TPiece()
            /*6*/ else -> ZPiece()
        }
    }
}