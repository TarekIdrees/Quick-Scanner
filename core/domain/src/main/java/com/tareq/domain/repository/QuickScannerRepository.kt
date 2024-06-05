package com.tareq.domain.repository

import com.tareq.domain.DataError
import com.tareq.domain.DatabaseOperation
import com.tareq.domain.Result
import com.tareq.model.Contact
import com.tareq.model.Email
import com.tareq.model.Product
import com.tareq.model.Wifi
import com.tareq.model.local.ScanItem
import kotlinx.coroutines.flow.Flow

interface QuickScannerRepository {
    suspend fun getScanItems(): List<ScanItem>
    suspend fun getProductByBarcode(barcode: String): Result<Product, DataError.Network>
    suspend fun insertProductIntoDatabase(product: Product, scanDate: String): DatabaseOperation
    suspend fun deleteProductFromDatabase(productId: String): DatabaseOperation
    suspend fun insertWifiIntoDatabase(wifi: Wifi): DatabaseOperation
    suspend fun deleteWifiFromDatabase(wifiSSID: String): DatabaseOperation
    suspend fun insertContactIntoDatabase(contact: Contact, scanDate: String): DatabaseOperation
    suspend fun deleteContactFromDatabase(name: String): DatabaseOperation
    suspend fun insertEmailIntoDatabase(email: Email, scanDate: String): DatabaseOperation
    suspend fun deleteEmailFromDatabase(email: String): DatabaseOperation

    fun getArchivedWifiItems(): Flow<Result<List<Wifi>, DataError.Local>>
    fun getArchivedContacts(): Flow<Result<List<Contact>, DataError.Local>>
    fun getArchivedEmails(): Flow<Result<List<Email>, DataError.Local>>
    fun getArchivedProducts(): Flow<Result<List<Product>, DataError.Local>>
}