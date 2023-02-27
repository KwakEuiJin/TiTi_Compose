package com.timertiti.titi_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.timertiti.titi_compose.ui.theme.TiTi_ComposeTheme
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    private val viewModel:TimerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiTi_ComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF73B5D8)) {
                    BottomNavigationScreens()
                }
            }
        }
    }


    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    // 초를 "mm:ss" 형식의 문자열로 변환하는 확장 함수
    fun Int.toTimeString(): String {
        val minutes = this / 60
        val seconds = this % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun BottomNavigationScreens() {
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            bottomBar = {
                BottomNavigation {
                    val navItems = listOf("Timer", "Stopwatch", "Todo", "Log", "Setting")
                    navItems.forEachIndexed { index, label ->
                        BottomNavigationItem(
                            icon = {
                                when (index) {
                                    0 -> Icon(Icons.Filled.Home, contentDescription = null)
                                    1 -> Icon(Icons.Filled.Person, contentDescription = null)
                                    2 -> Icon(Icons.Filled.List, contentDescription = null)
                                    3 -> Icon(Icons.Filled.Person, contentDescription = null)
                                    else -> Icon(Icons.Filled.Settings, contentDescription = null)
                                }
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 10.sp
                                )
                            },
                            selected = pagerState.currentPage == index,
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                        )
                    }
                }
            }
        ) { innerPadding ->
            HorizontalPager(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(Color(0xFF73B5D8)),
                count = 2,
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> com.timertiti.titi_compose.TimerScreen(viewModel)
                    1 -> TimerScreen(viewModel)
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        TiTi_ComposeTheme {
            BottomNavigationScreens()
        }
    }
}