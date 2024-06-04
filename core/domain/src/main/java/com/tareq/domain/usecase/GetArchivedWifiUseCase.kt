package com.tareq.domain.usecase

import com.tareq.domain.repository.ScannerRepository
import javax.inject.Inject

class GetArchivedWifiUseCase @Inject constructor(
    private val scannerRepository: ScannerRepository
) {
    operator fun invoke() = scannerRepository.getArchivedWifiItems()
}