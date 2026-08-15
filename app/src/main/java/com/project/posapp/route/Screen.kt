package com.project.posapp.route

enum class Screen(
    val route: String,
    val title: String
) {
    POS(route = "POS", title = "Point of Sale"),
    HISTORY(route = "HISTORY", title = "History"),
//    RECEIVABLE("receivable", "Piutang"),
//    RETURN("return", "Retur"),
//    SHIFT("shift", "Shift")
    ;

    companion object {
        fun fromRoute(route: String?): Screen =
            entries.firstOrNull { it.route == route } ?: POS
    }

}