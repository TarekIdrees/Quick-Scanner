package com.tareq.domain.usecase

import com.tareq.domain.repository.QuickScannerRepository
import javax.inject.Inject

class DeleteEmailFromDatabaseUseCase @Inject constructor(
    private val quickScannerRepository: QuickScannerRepository
) {
    suspend operator fun invoke(email: String) = quickScannerRepository.deleteEmailFromDatabase(email)
}