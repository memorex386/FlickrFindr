package com.example.flickerexample.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.example.flickerexample.R
import com.example.flickerexample.core.extensions.onActionSearch
import com.example.flickerexample.core.extensions.textString
import com.example.flickerexample.network.PhotoRepository
import com.example.flickerexample.network.PhotoSortItem
import kotlinx.android.synthetic.main.custom_search_view.view.*

class CustomSearchView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    first: Int = -1
) : FrameLayout(context, attributeSet, first) {

    init {
        View.inflate(context, R.layout.custom_search_view, this)

        appCompatImageView.setOnClickListener {
            appCompatEditText.textString = ""
        }

        sort.setOnClickListener {

            var selectedItem = PhotoRepository.sortItem.value

            val values = PhotoSortItem.values()

            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.sort_by))
            val titles = values.map { it.sortText }.toTypedArray()
            val checkedItem = values.indexOf(selectedItem)
            builder.setSingleChoiceItems(titles, checkedItem) { dialog, which ->
                selectedItem = values[which]
            }
            builder.setPositiveButton(context.getString(R.string.ok)) { dialog, which ->
                if (selectedItem != PhotoRepository.sortItem.value) {
                    PhotoRepository.sortItem.value = selectedItem
                    onSortChanged(selectedItem)
                }
            }
            builder.setNegativeButton(context.getString(R.string.cancel), null)

            val dialog = builder.create()
            dialog.show()
        }

    }

    var onSortChanged: ((PhotoSortItem) -> Unit) = {}

    var text: String
        set(value) {
            appCompatEditText.setText(value)
        }
        get() = appCompatEditText.textString

    fun onActionSearch(onActionSearch: (String) -> Unit) = appCompatEditText.onActionSearch(onActionSearch)

}