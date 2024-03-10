package com.bunbeauty.fakelivestream.common.util

import java.time.Instant.ofEpochSecond
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

fun Long.toTimeString(): String {
    return ofEpochSecond(this / 1_000)
        .atZone(UTC)
        .toLocalDateTime()
        .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}