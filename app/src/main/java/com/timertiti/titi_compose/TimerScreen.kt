package com.timertiti.titi_compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.cancel

@Composable
fun TimerScreen(viewModel: TimerViewModel) {
    val isRunning = remember { mutableStateOf(false) }
    val time = viewModel.time.value
    val progress = time.toFloat() / 180f

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 누적시간, 타이머 시간, 남은시간
        Box(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "누적시간: 00:00",
                    style = MaterialTheme.typography.h5,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = time.toTimeString(),
                    style = MaterialTheme.typography.h2,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "남은시간: ${(180 - time).toTimeString()}",
                    style = MaterialTheme.typography.h5,
                    color = Color.White
                )
            }
        }

        // 프로그래스바
        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(400.dp),
                progress = progress,
                strokeWidth = 15.dp,
                color = Color.Black,
                backgroundColor = Color.Gray
            )
        }

        // 버튼들
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Spacer(modifier = Modifier.size(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.startTimer() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "시작")
                }
                Button(
                    onClick = { viewModel.pauseTimer() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "일시정지")
                }
                Button(
                    onClick = { viewModel.resetTimer() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "초기화")
                }
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            isRunning.value = false
            coroutineContext.cancel()
        }
    }

}

fun Int.toTimeString(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}