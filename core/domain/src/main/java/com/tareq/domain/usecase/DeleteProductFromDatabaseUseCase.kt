package com.tareq.domain.usecase

import com.tareq.domain.repository.ScannerRepository
import javax.inject.Inject

class DeleteProductFromDatabaseUseCase @Inject constructor(
    private val scannerRepository: ScannerRepository
) {
    suspend operator fun invoke(productId: String) =
        scannerRepository.deleteProductFromDatabase(productId)
}