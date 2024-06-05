package com.tareq.domain.usecase

import com.tareq.domain.repository.QuickScannerRepository
import com.tareq.model.Product
import javax.inject.Inject

class InsertProductIntoDatabaseUseCase @Inject constructor(
    private val quickScannerRepository: QuickScannerRepository
) {
    suspend operator fun invoke(product: Product, scanDate: String) =
        quickScannerRepository.insertProductIntoDatabase(product, scanDate)
}