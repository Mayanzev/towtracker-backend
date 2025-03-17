package com.mayantsev_vs.utils

import at.favre.lib.crypto.bcrypt.BCrypt

fun hashPassword(password: String): String {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray()) // 12 - уровень сложности
}

fun verifyPassword(password: String, hashedPassword: String): Boolean {
    return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
}