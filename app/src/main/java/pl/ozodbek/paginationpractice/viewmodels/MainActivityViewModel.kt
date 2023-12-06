package pl.ozodbek.paginationpractice.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.ozodbek.paginationpractice.data.Repository
import pl.ozodbek.paginationpractice.data.models.FakeStoreUsers
import pl.ozodbek.paginationpractice.pagination.FakeStoreUsersSource
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val commonMastersPagingData: LiveData<PagingData<FakeStoreUsers>> = Pager(
        config = PagingConfig(
            pageSize = 15,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { FakeStoreUsersSource(repository.remote) }
    ).liveData.cachedIn(viewModelScope)

}