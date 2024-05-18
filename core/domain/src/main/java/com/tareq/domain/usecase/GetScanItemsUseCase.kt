package com.tareq.domain.usecase

import com.tareq.domain.repository.ScannerRepository
import javax.inject.Inject

class GetScanItemsUseCase @Inject constructor(
    private val scannerRepository: ScannerRepository
) {
    suspend operator fun invoke() = scannerRepository.getScanItems()
}