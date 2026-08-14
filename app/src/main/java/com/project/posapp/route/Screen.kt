package com.project.posapp.route

enum class Screen(
    val route: String,
    val title: String
) {
    POS("pos", "Point of Sale"),
    TRANSACTION("transaction", "Transaksi"),
//    RECEIVABLE("receivable", "Piutang"),
//    RETURN("return", "Retur"),
//    SHIFT("shift", "Shift")
    ;

    companion object {
        fun fromRoute(route: String?): Screen =
            entries.firstOrNull { it.route == route } ?: POS
    }

}