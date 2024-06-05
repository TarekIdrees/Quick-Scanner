package com.tareq.domain.usecase

import com.tareq.domain.repository.QuickScannerRepository
import com.tareq.model.Contact
import javax.inject.Inject

class InsertContactIntoDatabaseUseCase @Inject constructor(
    private val quickScannerRepository: QuickScannerRepository
) {
    suspend operator fun invoke(contact: Contact, scaData: String) =
        quickScannerRepository.insertContactIntoDatabase(contact, scaData)
}