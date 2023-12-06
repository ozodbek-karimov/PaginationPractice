package pl.ozodbek.paginationpractice.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import pl.ozodbek.paginationpractice.data.RemoteDataSource
import pl.ozodbek.paginationpractice.data.models.FakeStoreUsers
import pl.ozodbek.paginationpractice.util.Resource
import javax.inject.Inject


class FakeStoreUsersSource @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) :
    PagingSource<Int, FakeStoreUsers>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FakeStoreUsers> {
        return try {
            val nextPageNumber = params.key ?: 1
            val resource = remoteDataSource.getProducts(nextPageNumber, params.loadSize)

            if (resource is Resource.Success) {
                val data = resource.value

                LoadResult.Page(
                    data = data,
                    prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                    nextKey = if (data.isEmpty()) null else nextPageNumber + 1
                )
            } else {
                LoadResult.Error(Exception("Failed to load data"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FakeStoreUsers>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
