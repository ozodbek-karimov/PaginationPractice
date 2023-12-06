package pl.ozodbek.paginationpractice.data

import android.util.Log
import pl.ozodbek.paginationpractice.util.Resource
import retrofit2.HttpException

interface SafeApiCall {
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = throwable.response()?.errorBody()?.string()
                    Log.e("SafeApiCall", "HTTP Error - Code: $code, Error Response: $errorResponse")
                    Resource.Failure(false, code, errorResponse)
                }
                else -> {
                    Log.e("SafeApiCall", "Unexpected Error: ${throwable.message}")
                    Resource.Failure(true, null, null)
                }
            }
        }
    }
}
