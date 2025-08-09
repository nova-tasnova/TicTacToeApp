package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicTacToeGame()
                }
            }
        }
    }
}

@Composable
fun TicTacToeGame() {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var gameOver by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("Player X's turn") }
    var winningCells by remember { mutableStateOf(listOf<Int>()) }

    fun checkWin(): List<Int> {
        val winPositions = listOf(
            listOf(0,1,2), listOf(3,4,5), listOf(6,7,8),
            listOf(0,3,6), listOf(1,4,7), listOf(2,5,8),
            listOf(0,4,8), listOf(2,4,6)
        )
        for (combo in winPositions) {
            val (a,b,c) = combo
            if (board[a].isNotEmpty() && board[a] == board[b] && board[b] == board[c]) {
                return combo
            }
        }
        return emptyList()
    }

    fun resetGame() {
        board = List(9) { "" }
        currentPlayer = "X"
        statusMessage = "Player X's turn"
        gameOver = false
        winningCells = emptyList()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Turn indicator
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        if (currentPlayer == "X") Color.Red else Color.Blue,
                        shape = RoundedCornerShape(50)
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = statusMessage,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }

        // Game board
        for (row in 0..2) {
            Row {
                for (col in 0..2) {
                    val index = row * 3 + col
                    val cellColor = when {
                        winningCells.contains(index) -> Color.Green
                        else -> Color.White
                    }
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(1.dp, Color.Black)
                            .background(cellColor)
                            .clickable(enabled = !gameOver && board[index].isEmpty()) {
                                val newBoard = board.toMutableList()
                                newBoard[index] = currentPlayer
                                board = newBoard

                                val winCombo = checkWin()
                                if (winCombo.isNotEmpty()) {
                                    statusMessage = "Player $currentPlayer wins!"
                                    winningCells = winCombo
                                    gameOver = true
                                } else if (board.all { it.isNotEmpty() }) {
                                    statusMessage = "It's a draw!"
                                    gameOver = true
                                } else {
                                    currentPlayer = if (currentPlayer == "X") "O" else "X"
                                    statusMessage = "Player $currentPlayer's turn"
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = board[index], fontSize = 48.sp, color = Color.Black)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = { resetGame() }) {
            Text("Reset Game")
        }
    }
}
