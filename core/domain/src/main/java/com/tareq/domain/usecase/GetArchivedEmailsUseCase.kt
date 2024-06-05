package com.tareq.domain.usecase

import com.tareq.domain.repository.QuickScannerRepository
import javax.inject.Inject

class GetArchivedEmailsUseCase @Inject constructor(
    private val quickScannerRepository: QuickScannerRepository
) {
    operator fun invoke() = quickScannerRepository.getArchivedEmails()
}