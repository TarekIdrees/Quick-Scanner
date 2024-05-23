package com.tareq.domain.usecase

import com.tareq.domain.repository.ScannerRepository
import com.tareq.model.Email
import javax.inject.Inject

class InsertEmailIntoDatabaseUseCase @Inject constructor(
    private val scannerRepository: ScannerRepository
) {
    suspend operator fun invoke(email: Email, scanDate: String) =
        scannerRepository.insertEmailIntoDatabase(email, scanDate)
}