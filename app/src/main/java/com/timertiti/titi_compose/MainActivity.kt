package com.timertiti.titi_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.timertiti.titi_compose.ui.theme.TiTi_ComposeTheme
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope.coroutineContext

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiTi_ComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    TimerScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun TimerScreen() {
    var isRunning = remember { mutableStateOf(false) }
    var time = remember { mutableStateOf(180) }
    val progress = time.value.toFloat() / 180f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

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
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = time.value.toTimeString(),
                    style = MaterialTheme.typography.h2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "남은시간: ${(180 - time.value).toTimeString()}",
                    style = MaterialTheme.typography.h5,
                )
            }
        }

        // 프로그래스바
        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            CustomCircularProgressIndicator(progress)
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
                    onClick = { startTimer(isRunning, time) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "시작")
                }
                Button(
                    onClick = { pauseTimer(isRunning) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "일시정지")
                }
                Button(
                    onClick = { resetTimer(isRunning, time) },
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

// 초를 "mm:ss" 형식의 문자열로 변환하는 확장 함수
fun Int.toTimeString(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}

fun startTimer(isRunning: MutableState<Boolean>, time: MutableState<Int>) {
    isRunning.value = true
    CoroutineScope(Dispatchers.Default).launch {
        while (isRunning.value && time.value > 0) {
            delay(1000L)
            time.value -= 1
        }
    }
}

fun pauseTimer(isRunning: MutableState<Boolean>) {
    isRunning.value = false
}

fun resetTimer(isRunning: MutableState<Boolean>, time: MutableState<Int>) {
    isRunning.value = false
    time.value = 180
}

@Composable
fun CustomCircularProgressIndicator(progress: Float) {
    val stroke = with(LocalDensity.current) { 8.dp.toPx() }
    val progressAngle = progress * 360f

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(400.dp)
    ) {
        Canvas(modifier = Modifier.size(400.dp)) {
            val arcWidth = size.minDimension - stroke
            val arcCenter = Offset(size.width / 2f, size.height / 2f)
            val arcRadius = arcWidth / 2f

            val arcRect = Rect(
                arcCenter.x - arcRadius,
                arcCenter.y - arcRadius,
                arcCenter.x + arcRadius,
                arcCenter.y + arcRadius
            )

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(androidx.compose.ui.graphics.Color.Cyan, androidx.compose.ui.graphics.Color.Cyan, androidx.compose.ui.graphics.Color.Cyan),
                    center = Offset.Zero
                ),
                startAngle = 270f,
                sweepAngle = progress * -360f,
                useCenter = false,
                topLeft = arcRect.topLeft,
                size = Size(arcWidth, arcWidth),
                style = Stroke(stroke)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TiTi_ComposeTheme {
        TimerScreen()
    }
}