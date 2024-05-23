package com.tareq.domain.usecase

import com.tareq.domain.repository.ScannerRepository
import com.tareq.model.Contact
import javax.inject.Inject

class InsertContactIntoDatabaseUseCase @Inject constructor(
    private val scannerRepository: ScannerRepository
) {
    suspend operator fun invoke(contact: Contact, scaData: String) =
        scannerRepository.insertContactIntoDatabase(contact, scaData)
}