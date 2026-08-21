package com.project.posapp.utils.extensions

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val indonesiaLocale = Locale.forLanguageTag("id-ID")

fun String?.toLocalDateOrNull(): LocalDate? =
    this?.let {
        runCatching {
            LocalDate.parse(it)
        }.getOrNull()
    }

fun String?.toDisplayDate(
    fallback: String = ""
): String =
    toLocalDateOrNull()
        ?.format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        )
        ?: fallback

fun String?.toLongDisplayDate(
    fallback: String = "-"
): String =
    toLocalDateOrNull()
        ?.format(
            DateTimeFormatter.ofPattern(
                "dd MMMM yyyy",
                indonesiaLocale
            )
        )
        ?: fallback

private fun String?.toZonedDateTimeOrNull() =
    this?.let { value ->
        runCatching {
            OffsetDateTime
                .parse(value)
                .atZoneSameInstant(ZoneId.systemDefault())
        }.getOrNull()
    }

fun String?.toDisplayTime(
    fallback: String = "-"
): String =
    toZonedDateTimeOrNull()
        ?.format(
            DateTimeFormatter.ofPattern("HH.mm")
        )
        ?: fallback

fun String?.toLongDisplayDateTime(
    fallback: String = "-"
): String =
    toZonedDateTimeOrNull()
        ?.format(
            DateTimeFormatter.ofPattern(
                "dd MMMM yyyy, HH.mm",
                indonesiaLocale
            )
        )
        ?: fallback

fun String?.toLongDisplayDateFromDateTime(
    fallback: String = "-"
): String =
    toZonedDateTimeOrNull()
        ?.format(
            DateTimeFormatter.ofPattern(
                "dd MMMM yyyy",
                indonesiaLocale
            )
        )
        ?: fallback