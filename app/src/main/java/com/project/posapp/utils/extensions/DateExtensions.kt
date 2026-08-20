package com.project.posapp.utils.extensions

import java.time.LocalDate
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