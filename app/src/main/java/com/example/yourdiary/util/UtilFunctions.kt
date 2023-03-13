package com.example.yourdiary.util

import io.realm.kotlin.types.RealmInstant
import java.time.Instant

fun RealmInstant.toInstant(): Instant {
    val seconds: Long = this.epochSeconds
    val nano: Int = this.nanosecondsOfSecond
    return if (seconds >= 0) {
        Instant.ofEpochSecond(seconds, nano.toLong())
    } else {
        Instant.ofEpochSecond(seconds - 1, 1_000_000 + nano.toLong())
    }
}