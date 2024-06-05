package com.tareq.domain.usecase

import com.tareq.domain.repository.QuickScannerRepository
import javax.inject.Inject

class GetScanItemsUseCase @Inject constructor(
    private val quickScannerRepository: QuickScannerRepository
) {
    suspend operator fun invoke() = quickScannerRepository.getScanItems()
}