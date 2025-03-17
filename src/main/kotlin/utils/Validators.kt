package com.mayantsev_vs.utils

fun String.isValidEmail(): Boolean = this.matches(emailRegex)

val emailRegex = "([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)".toRegex()