package com.timertiti.titi_compose

fun Int.toTimeString(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}