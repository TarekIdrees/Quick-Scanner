package com.tareq.domain.usecase

import com.tareq.domain.repository.QuickScannerRepository
import javax.inject.Inject

class DeleteProductFromDatabaseUseCase @Inject constructor(
    private val quickScannerRepository: QuickScannerRepository
) {
    suspend operator fun invoke(productId: String) =
        quickScannerRepository.deleteProductFromDatabase(productId)
}