package com.tareq.domain.usecase

import com.tareq.domain.repository.QuickScannerRepository
import com.tareq.model.Wifi
import javax.inject.Inject

class InsertWifiIntoDatabaseUseCase @Inject constructor(
    private val quickScannerRepository: QuickScannerRepository
) {
    suspend operator fun invoke(wifi: Wifi) =
        quickScannerRepository.insertWifiIntoDatabase(wifi)
}