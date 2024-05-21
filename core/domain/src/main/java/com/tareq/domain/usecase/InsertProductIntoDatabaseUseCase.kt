package com.tareq.domain.usecase

import com.tareq.domain.repository.ScannerRepository
import com.tareq.model.Product
import javax.inject.Inject

class InsertProductIntoDatabaseUseCase @Inject constructor(
    private val scannerRepository: ScannerRepository
) {
    suspend operator fun invoke(product: Product) = scannerRepository.insertProduct(product)
}