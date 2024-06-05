package com.tareq.domain.usecase

import com.tareq.domain.repository.QuickScannerRepository
import com.tareq.model.Email
import javax.inject.Inject

class InsertEmailIntoDatabaseUseCase @Inject constructor(
    private val quickScannerRepository: QuickScannerRepository
) {
    suspend operator fun invoke(email: Email, scanDate: String) =
        quickScannerRepository.insertEmailIntoDatabase(email, scanDate)
}