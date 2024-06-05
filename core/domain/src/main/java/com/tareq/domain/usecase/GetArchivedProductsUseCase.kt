package com.tareq.domain.usecase

import com.tareq.domain.repository.QuickScannerRepository
import javax.inject.Inject

class GetArchivedProductsUseCase @Inject constructor(
    private val quickScannerRepository: QuickScannerRepository
) {
    operator fun invoke() = quickScannerRepository.getArchivedProducts()
}