package pl.ozodbek.paginationpractice.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.ozodbek.paginationpractice.adapters.FakeStoreUsersAdapter
import pl.ozodbek.paginationpractice.adapters.PageLoadStateAdapter
import pl.ozodbek.paginationpractice.databinding.ActivityMainBinding
import pl.ozodbek.paginationpractice.util.NetworkListener
import pl.ozodbek.paginationpractice.util.gone
import pl.ozodbek.paginationpractice.util.onClick
import pl.ozodbek.paginationpractice.util.setNullableAdapter
import pl.ozodbek.paginationpractice.util.show
import pl.ozodbek.paginationpractice.util.viewBinding
import pl.ozodbek.paginationpractice.viewmodels.MainActivityViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val fakeStoreUsersAdapter: FakeStoreUsersAdapter by lazy { FakeStoreUsersAdapter() }
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setupUI()
    }

    private fun setupUI() {
        setupActionBar()
        setupRecyclerView()
        networkStateListener()
    }


    /** SETTING UP ACTIONBAR */
    private fun setupActionBar() {
        this.setSupportActionBar(binding.toolbar)
        this.title = "Pagination Practice"
    }


    /** SETTING UP RECYCLERVIEW */
    private fun setupRecyclerView() {
        binding.recyclerview.adapter = fakeStoreUsersAdapter
        binding.recyclerview.setNullableAdapter(adapter = fakeStoreUsersAdapter)

        fakeStoreUsersAdapter.addLoadStateListener { loadState ->
            val isShimmerVisible = loadState.refresh is LoadState.Loading
            updateShimmerVisibility(isShimmerVisible)
        }

        binding.recyclerview.adapter =
            fakeStoreUsersAdapter.withLoadStateFooter(PageLoadStateAdapter {
                fakeStoreUsersAdapter.retry()
            })


    }


    /** OBSERVING API RESPONSE */
    private fun observeApiResponse() {

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.commonMastersPagingData.observe(this@MainActivity) { pagingData ->
                fakeStoreUsersAdapter.submitData(this@MainActivity.lifecycle, pagingData)
            }
        }

    }

    private fun networkStateListener() {
        networkListener = NetworkListener()
        lifecycleScope.launch(Dispatchers.Main) {
            networkListener.checkNetworkAvailability(applicationContext)
                .collect { isNetworkAvailable ->
                    updateUI(isNetworkAvailable)
                }
        }
    }

    private fun updateUI(isNetworkAvailable: Boolean) {
        binding.apply {
            when {
                isNetworkAvailable && fakeStoreUsersAdapter.itemCount == 0 -> {
                    recyclerview.show()
                    noInternetImageview.gone()
                    noInternetTv.gone()
                    tryAgainButton.gone()
                    observeApiResponse()
                }
                fakeStoreUsersAdapter.itemCount > 0 && !isNetworkAvailable -> {
                    recyclerview.show()
                    shimmerLayout.show()
                    noInternetImageview.gone()
                    noInternetTv.gone()
                    tryAgainButton.gone()
                }
                !isNetworkAvailable && fakeStoreUsersAdapter.itemCount == 0 -> {
                    recyclerview.gone()
                    shimmerLayout.gone()
                    noInternetImageview.show()
                    noInternetTv.show()
                    tryAgainButton.show()
                    tryAgainButton.onClick {
                        fakeStoreUsersAdapter.retry()
                        observeApiResponse()
                    }
                }
            }
        }
    }



    /** UPDATING VISIBILITY OF SHIMMER AND RECYCLERVIEW */
    private fun updateShimmerVisibility(showShimmer: Boolean) {
        val visibility = if (showShimmer) View.INVISIBLE else View.VISIBLE
        val shimmerVisibility = if (showShimmer) View.VISIBLE else View.INVISIBLE

        binding.recyclerview.visibility = visibility
        binding.shimmerLayout.visibility = shimmerVisibility

        if (showShimmer) {
            binding.shimmerLayout.startShimmer()
        } else {
            binding.shimmerLayout.stopShimmer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkListener)
    }
}