package pl.ozodbek.paginationpractice.adapters

import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.ozodbek.paginationpractice.databinding.LoadStateRowItemBinding
import pl.ozodbek.paginationpractice.util.gone
import pl.ozodbek.paginationpractice.util.onClick
import pl.ozodbek.paginationpractice.util.show
import pl.ozodbek.paginationpractice.util.viewBinding

class PageLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PageLoadStateAdapter.LoadStateViewHolder>() {

    private val handler = Handler(Looper.getMainLooper())
    private var isErrorButtonPressed = false

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(parent.viewBinding(LoadStateRowItemBinding::inflate))
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: LoadStateRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.errorButton.onClick {
                isErrorButtonPressed = true
                binding.errorPagingContainer.gone()

                if (loadState is LoadState.Loading) {
                    // If still loading, keep the progress bar visible until loading completes
                    handler.postDelayed({
                        binding.bottomProgressbar.gone()
                        isErrorButtonPressed = false
                        retry.invoke()
                    }, 350)
                } else {
                    // If not loading, show the progress bar and retry after handler time ends up
                    binding.bottomProgressbar.show()
                    handler.postDelayed({
                        retry.invoke()
                    }, 350)
                }
            }
        }



        fun bind(loadState: LoadState) {
            if (isErrorButtonPressed && loadState is LoadState.Loading) {
                // If error button pressed and still loading, keep the progress bar visible
                binding.bottomProgressbar.show()
            } else {
                // Otherwise, show/hide the progress bar based on the loading state
                binding.bottomProgressbar.isVisible = loadState is LoadState.Loading
            }

            if (loadState is LoadState.Loading && isErrorButtonPressed) {
                // If loading and error button pressed, hide the error container
                binding.errorPagingContainer.gone()
            } else {
                // Otherwise, show/hide the error container based on the error state
                binding.errorPagingContainer.isVisible = loadState is LoadState.Error
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        handler.removeCallbacksAndMessages(null)
        super.onDetachedFromRecyclerView(recyclerView)
    }
}