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
                    BottomNavigationScreens(viewModel)
                }
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun BottomNavigationScreens(viewModel:TimerViewModel) {
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
                0 -> TimerScreen(viewModel)
                1 -> TimerScreen(viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TiTi_ComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF73B5D8)) {
            BottomNavigationScreens(TimerViewModel())
        }
    }
}

/*
class TestViewModel(~~): TestUserProfile,TestMenu,~~

interface TestUserProfile {
  val user: MutableStateFlow<User>
  fun onClickPoint()
}

interface TestMenu {
  val user: MutableStateFlow<User>
  val menuList: StateFlow<MutableList<MenuItem>>
  fun onClickMenuItem(position:Int)
}

@Preview
@Composable
fun TestPreview() {
  TestView(TestUserProfileMock(),TestMenuMock())
}


@Composable
fun TestViewContent() {
  val viewModel = getViewModel<TestViewModel>()
  TestView(
    viewModel as TestUserProfile,
    viewModel as TestMenu
  )
}

*/