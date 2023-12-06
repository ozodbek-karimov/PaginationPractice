package pl.ozodbek.paginationpractice.data

import pl.ozodbek.paginationpractice.data.models.FakeStoreUsers
import retrofit2.http.GET
import retrofit2.http.Query

interface FakeStoreAPI {

    @GET("api/v1/users")
    suspend fun getProducts(@Query("limit") page: Int, @Query("offset") offset: Int): List<FakeStoreUsers>


}