package com.bunbeauty.fakelivestream.common.util

import java.time.Instant.ofEpochSecond
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

fun Int.toTimeString(): String {
    return ofEpochSecond(this.toLong())
        .atZone(UTC)
        .toLocalDateTime()
        .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}