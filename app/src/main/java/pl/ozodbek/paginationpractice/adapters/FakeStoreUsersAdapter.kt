
package pl.ozodbek.paginationpractice.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.ozodbek.paginationpractice.data.models.FakeStoreUsers
import pl.ozodbek.paginationpractice.databinding.FakeStoreUsersRowItemBinding
import pl.ozodbek.paginationpractice.util.loadImage
import pl.ozodbek.paginationpractice.util.onClick
import pl.ozodbek.paginationpractice.util.viewBinding

class FakeStoreUsersAdapter :
    PagingDataAdapter<FakeStoreUsers, FakeStoreUsersAdapter.AdapterViewHolder>(MyDiffCallback()) {

    private var itemClickListener: ((FakeStoreUsers) -> Unit)? = null

    fun setItemClickListener(listener: (FakeStoreUsers) -> Unit) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return AdapterViewHolder(parent.viewBinding(FakeStoreUsersRowItemBinding::inflate))
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val question = getItem(position)
        question?.let { holder.bind(it, itemClickListener) }
    }


    class AdapterViewHolder(private val binding: FakeStoreUsersRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(remoteData: FakeStoreUsers, clickListener: ((FakeStoreUsers) -> Unit)?) {

            remoteData.let {
                binding.apply {

                    binding.imageView.loadImage(remoteData.avatar)

                    binding.textView.text = remoteData.name
                    binding.textView2.text = remoteData.email
                    binding.textView3.text = remoteData.role
                    binding.textView4.text = remoteData.password

                    root.onClick { clickListener?.invoke(remoteData) }
                }
            }
        }
    }

    private class MyDiffCallback : DiffUtil.ItemCallback<FakeStoreUsers>() {
        override fun areItemsTheSame(oldItem: FakeStoreUsers, newItem: FakeStoreUsers) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FakeStoreUsers, newItem: FakeStoreUsers) =
            oldItem == newItem
    }
}