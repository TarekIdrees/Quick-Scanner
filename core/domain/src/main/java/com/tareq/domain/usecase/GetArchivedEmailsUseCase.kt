package com.tareq.domain.usecase

import com.tareq.domain.repository.ScannerRepository
import javax.inject.Inject

class GetArchivedEmailsUseCase @Inject constructor(
    private val scannerRepository: ScannerRepository
) {
    operator fun invoke() = scannerRepository.getArchivedEmails()
}