package com.jem.tetracubeenginedemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jem.tetracubeengine.TetracubeEngine
import com.jem.tetracubeengine.TetracubeSettings
import com.jem.tetracubeengine.listener.GameStateListener
import com.jem.tetracubeengine.pieces.Piece
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val tetracubeEngine = TetracubeEngine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moveLeft.setOnClickListener {
            tetracubeEngine.moveLeft()
        }

        moveRight.setOnClickListener {
            tetracubeEngine.moveRight()
        }

        rotateCounterClockwise.setOnClickListener {
            tetracubeEngine.rotateCounterClockwise()
        }

        rotateClockwise.setOnClickListener {
            tetracubeEngine.rotateClockwise()
        }

        softDrop.setOnClickListener {
            tetracubeEngine.dropOneLayer()
        }

        hardDrop.setOnClickListener {
            tetracubeEngine.hardDrop()
        }

        tetracubeEngine.gameStateListener = object : GameStateListener {
            override fun onGameStarted(settings: TetracubeSettings) {
                Toast.makeText(this@MainActivity, "Game Started", Toast.LENGTH_SHORT).show()
            }

            override fun onGridUpdated(
                grid: Array<Array<BooleanArray>>, height: Int, width: Int, breadth: Int
            ) {
                runOnUiThread {
                    val stringBuilder = StringBuilder("\n")
                    for (h in height - 1 downTo 0) {
                        val widthAtHeight = tetracubeEngine.board.widths[h][0]
                        stringBuilder.append(
                            if (widthAtHeight < 10) {
                                "0$widthAtHeight"
                            } else {
                                "$widthAtHeight"
                            }
                        )
                        stringBuilder.append("|")
                        for (w in 0 until width) {
                            stringBuilder.append(if (grid[h][w][0]) "!=!" else "   "/*("$h:$w ")*/)
                        }
                        stringBuilder.append("|\n")
                    }
                    stringBuilder.append("  -")
                    for (w in 0 until width) {
                        stringBuilder.append("---")
                    }
                    stringBuilder.append("-\n   ")
                    for (w in 0 until width) {
                        val heightAtWidth = tetracubeEngine.board.heights[w][0]
                        stringBuilder.append(
                            if (heightAtWidth < 10) {
                                "0$heightAtWidth "
                            } else {
                                "$heightAtWidth "
                            }
                        )
                    }
                    stringBuilder.append(" \n\n${tetracubeEngine.board.maxHeight}")
                    gridTextView.text = stringBuilder.toString()
                }
            }

            override fun onPieceChanged(currentPiece: Piece, nextPiece: Piece) {
                currentPieceTextView.text = currentPiece.pieceName
            }

            override fun onGameEnded() {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Game Ended", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tetracubeEngine.startNewGame()

        newGame.setOnClickListener {
            tetracubeEngine.startNewGame()
        }
    }
}