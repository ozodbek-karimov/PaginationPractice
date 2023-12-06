package pl.ozodbek.paginationpractice.data

import dagger.hilt.android.scopes.ViewModelScoped
import pl.ozodbek.paginationpractice.data.RemoteDataSource
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource
) {
    val remote = remoteDataSource
}