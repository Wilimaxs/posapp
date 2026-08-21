package com.project.posapp.utils.extensions

import android.app.DatePickerDialog
import android.content.Context
import java.time.LocalDate
import java.time.ZoneId

fun Context.showDatePicker(
    initialDate: LocalDate = LocalDate.now(),
    minDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit
) {
    val dialog = DatePickerDialog(
        this,
        { _, year, month, day ->
            onDateSelected(
                LocalDate.of(
                    year,
                    month + 1,
                    day
                )
            )
        },
        initialDate.year,
        initialDate.monthValue - 1,
        initialDate.dayOfMonth
    )

    minDate?.let { date ->
        dialog.datePicker.minDate = date
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    dialog.show()
}