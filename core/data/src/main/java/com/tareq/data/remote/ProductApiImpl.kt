package com.tareq.data.remote

import com.tareq.data.remote.model.ProductDto
import com.tareq.data.remote.model.ProductsDto
import com.tareq.domain.DataError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import javax.inject.Inject

class ProductApiImpl @Inject constructor(private val client: HttpClient) : ProductApi {
    override suspend fun getProductByBarcode(barcode: String): ApiCallResult<ProductDto> {
        return wrapApiCall {
            client.get(ProductApiEndpointsUrl.PRODUCT_ENDPOINT_URL) {
                parameter(ProductApiEndpointsUrl.BARCODE_PARAMETER_NAME, barcode)
                parameter(
                    ProductApiEndpointsUrl.API_KEY_PARAMETER_NAME,
                    ProductApiEndpointsUrl.API_KEY
                )
            }.body<ProductsDto>().products!!.filterNotNull().first()
        }
    }

    private inline fun <T> wrapApiCall(apiCall: () -> T): ApiCallResult<T> {
        return try {
            ApiCallResult.Success(data = apiCall())
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.NotFound -> {
                    ApiCallResult.Failure(DataError.Network.NOT_FOUND)
                }

                HttpStatusCode.InternalServerError -> {
                    ApiCallResult.Failure(DataError.Network.SERVER_ERROR)
                }

                HttpStatusCode.NonAuthoritativeInformation -> {
                    ApiCallResult.Failure(DataError.Network.UNAUTHORIZED)
                }

                HttpStatusCode.Forbidden -> {
                    ApiCallResult.Failure(DataError.Network.FORBIDDEN)
                }

                HttpStatusCode.TooManyRequests -> {
                    ApiCallResult.Failure(DataError.Network.EXCEED_LIMIT)
                }
                else -> {
                    ApiCallResult.Failure(DataError.Network.UNKNOWN)
                }
            }
        } catch (e: ServerResponseException) {
            ApiCallResult.Failure(DataError.Network.SERVER_ERROR)
        } catch (e: SerializationException) {
            ApiCallResult.Failure(DataError.Network.SERIALIZATION_ERROR)
        } catch (e: IOException) {
            ApiCallResult.Failure(DataError.Network.NO_INTERNET)
        } catch (e: Exception) {
            ApiCallResult.Failure(DataError.Network.UNKNOWN)
        }
    }
}