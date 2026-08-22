package com.project.posapp.route

enum class Screen(
    val route: String,
    val title: String
) {
    POS(route = "POS", title = "Point of Sale"),
    HISTORY(route = "HISTORY", title = "Transaksi"),
    RECEIVABLE(route = "Piutang", title = "Piutang");

    companion object {
        fun fromRoute(route: String?): Screen =
            entries.firstOrNull { it.route == route } ?: POS
    }

}