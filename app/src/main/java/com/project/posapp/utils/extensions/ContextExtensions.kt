package com.project.posapp.utils.extensions

import android.app.DatePickerDialog
import android.content.Context
import java.time.LocalDate

fun Context.showDatePicker(
    initialDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit
) {
    DatePickerDialog(
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
    ).show()
}