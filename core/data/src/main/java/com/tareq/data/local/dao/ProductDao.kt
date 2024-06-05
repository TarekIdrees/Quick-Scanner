package com.tareq.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tareq.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("DELETE FROM product_table WHERE barcode = :productId")
    suspend fun deleteProduct(productId: String)
    @Query("SELECT * FROM product_table")
    fun getAllProducts(): Flow<List<ProductEntity>>

}