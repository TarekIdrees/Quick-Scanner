package com.tareq.repository

import com.tareq.data.local.ScanItemsSource
import com.tareq.data.local.dao.ContactDao
import com.tareq.data.local.dao.EmailDao
import com.tareq.data.local.dao.ProductDao
import com.tareq.data.local.dao.WifiDao
import com.tareq.data.local.mapper.toContactEntity
import com.tareq.data.local.mapper.toEmailEntity
import com.tareq.data.local.mapper.toProductEntity
import com.tareq.data.local.mapper.toWifiEntity
import com.tareq.data.remote.ApiCallResult
import com.tareq.data.remote.ProductApi
import com.tareq.data.remote.mapper.toProduct
import com.tareq.domain.DataError
import com.tareq.domain.DatabaseOperation
import com.tareq.domain.Result
import com.tareq.domain.repository.ScannerRepository
import com.tareq.model.Contact
import com.tareq.model.Email
import com.tareq.model.Product
import com.tareq.model.Wifi
import com.tareq.model.local.ScanItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScannerRepositoryImpl @Inject constructor(
    private val scanItemsSource: ScanItemsSource,
    private val productApi: ProductApi,
    private val productDao: ProductDao,
    private val wifiDao: WifiDao,
    private val contactDao: ContactDao,
    private val emailDao: EmailDao
) : ScannerRepository {
    override suspend fun getScanItems(): List<ScanItem> {
        return scanItemsSource.scanItems
    }

    override suspend fun getProductByBarcode(barcode: String): Result<Product, DataError.Network> {
        return when (val result = productApi.getProductByBarcode(barcode)) {
            is ApiCallResult.Success -> Result.Success(result.data.toProduct())
            is ApiCallResult.Failure -> Result.Error(result.exception)
        }
    }

    override suspend fun insertProductIntoDatabase(
        product: Product,
        scanDate: String
    ): DatabaseOperation {
        return executeDatabaseOperation {
            productDao.insertProduct(product.toProductEntity(scanDate = scanDate))
        }
    }

    override suspend fun deleteProductFromDatabase(productId: String): DatabaseOperation {
        return executeDatabaseOperation {
            productDao.deleteProduct(productId)
        }
    }

    override suspend fun insertWifiIntoDatabase(wifi: Wifi, scanDate: String): DatabaseOperation {
        return executeDatabaseOperation {
            wifiDao.insertWifi(wifi.toWifiEntity(scanDate = scanDate))
        }
    }

    override suspend fun deleteWifiFromDatabase(wifiSSID: String): DatabaseOperation {
        return executeDatabaseOperation {
            wifiDao.deleteWifi(wifiSSID)
        }
    }

    override suspend fun insertContactIntoDatabase(
        contact: Contact,
        scanDate: String
    ): DatabaseOperation {
        return executeDatabaseOperation {
            contactDao.insertContact(contact.toContactEntity(scanDate = scanDate))
        }
    }

    override suspend fun deleteContactFromDatabase(name: String): DatabaseOperation {
        return executeDatabaseOperation {
            contactDao.deleteContact(name = name)
        }
    }

    override suspend fun insertEmailIntoDatabase(
        email: Email,
        scanDate: String
    ): DatabaseOperation {
        return executeDatabaseOperation {
            emailDao.insertEmail(email.toEmailEntity(scanDate = scanDate))
        }
    }

    override suspend fun deleteEmailFromDatabase(email: String): DatabaseOperation {
        return executeDatabaseOperation {
            emailDao.deleteEmail(email)
        }
    }

    private suspend fun executeDatabaseOperation(operation: suspend () -> Unit): DatabaseOperation {
        return withContext(Dispatchers.IO) {
            try {
                operation()
                DatabaseOperation.Complete
            } catch (e: Exception) {
                DatabaseOperation.InComplete(DataError.Local.INCOMPLETE)
            }
        }
    }
}