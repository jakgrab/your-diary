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

fun Instant.toRealmInstant(): RealmInstant {
    val sec: Long = this.epochSecond
    val nano: Int = this.nano
    return if (sec >= 0) {
        RealmInstant.from(sec, nano)
    } else {
        RealmInstant.from(sec + 1, -1_000_000 + nano)
    }
}