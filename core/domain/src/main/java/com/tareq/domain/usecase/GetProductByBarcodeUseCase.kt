package com.tareq.domain.usecase

import com.tareq.domain.repository.ScannerRepository
import javax.inject.Inject


class GetProductByBarcodeUseCase @Inject constructor(
    private val scannerRepository: ScannerRepository
) {
    suspend operator fun invoke(barcode: String) = scannerRepository.getProductByBarcode(barcode)
}