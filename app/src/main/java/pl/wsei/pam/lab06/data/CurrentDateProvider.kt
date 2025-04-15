package pl.wsei.pam.lab06.data

import java.time.LocalDate

interface CurrentDateProvider {
    fun getCurrentDate(): LocalDate
}

class DefaultCurrentDateProvider : CurrentDateProvider {
    override fun getCurrentDate(): LocalDate = LocalDate.now()
}
