package pl.ozodbek.paginationpractice.data


import javax.inject.Inject

class RemoteDataSource @Inject constructor(

    private val profexAPI: FakeStoreAPI,

    ) : SafeApiCall {

    suspend fun getProducts(page: Int, offset: Int) =
        safeApiCall { profexAPI.getProducts(page, offset) }


}