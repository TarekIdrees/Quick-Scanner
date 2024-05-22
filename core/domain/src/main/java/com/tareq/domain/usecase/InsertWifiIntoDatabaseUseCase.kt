package com.tareq.domain.usecase

import com.tareq.domain.repository.ScannerRepository
import com.tareq.model.Wifi
import javax.inject.Inject

class InsertWifiIntoDatabaseUseCase @Inject constructor(
    private val scannerRepository: ScannerRepository
) {
    suspend operator fun invoke(wifi: Wifi, scanDate: String) =
        scannerRepository.insertWifiIntoDatabase(wifi, scanDate)
}