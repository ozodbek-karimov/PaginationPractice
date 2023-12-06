package pl.ozodbek.paginationpractice.util

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import coil.request.CachePolicy
import coil.size.ViewSizeResolver
import pl.ozodbek.paginationpractice.R

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }


inline fun <reified T : ViewBinding> ViewGroup.viewBinding(
    crossinline inflate: (LayoutInflater, ViewGroup, Boolean) -> T,
): T {
    return inflate(LayoutInflater.from(context), this, false)
}

fun <T> ImageView.loadImage(image: T?) {
    this.load(image.takeIf { it?.toString()?.isNotBlank() == true } ?: R.drawable.ic_error_placeholder) {
        crossfade(500)
        placeholder(R.drawable.ic_error_placeholder)
        error(R.drawable.ic_error_placeholder)
        size(ViewSizeResolver(this@loadImage))
        memoryCachePolicy(CachePolicy.ENABLED)
        diskCachePolicy(CachePolicy.ENABLED)
    }


    /** IMAGE LOADING CACHE

    val imageLoader = ImageLoader.Builder(context)
    .respectCacheHeaders(false)
    .build()
    Coil.setImageLoader(imageLoader)


     */
}


fun View.onClick(clickListener: (View) -> Unit) {
    setOnClickListener(clickListener)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}


fun <VH : RecyclerView.ViewHolder> RecyclerView.setNullableAdapter(
    adapter: RecyclerView.Adapter<VH>,
) {
    this.adapter = adapter
    this.clearReference()
}


internal fun RecyclerView.clearReference() {
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

        override fun onViewAttachedToWindow(v: View) {
        }

        override fun onViewDetachedFromWindow(v: View) {
            this@clearReference.adapter = null
        }
    })
}

fun <T : Any> T?.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    when (this) {
        is Fragment -> Toast.makeText(this.requireContext(), msg, duration).show()
        is Activity -> Toast.makeText(this.applicationContext, msg, duration).show()
        is Context -> Toast.makeText(this, msg, duration).show()
        is AndroidViewModel -> Toast.makeText(getApplication(), msg, duration).show()
        else -> throw IllegalArgumentException("Unsupported type for toast: ${this?.javaClass?.simpleName}")
    }
}
