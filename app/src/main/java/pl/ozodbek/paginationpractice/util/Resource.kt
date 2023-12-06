package pl.ozodbek.paginationpractice.util

sealed class Resource<out T> {

    data class Success<out T>(val value: T) : Resource<T>()

    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: String?
    ) : Resource<Nothing>()

    data object Loading : Resource<Nothing>()
}