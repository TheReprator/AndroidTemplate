/*
 * Copyright 2021 Vikram LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.template.base_android.binding

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import app.template.base_android.extensions.snackBar

@BindingAdapter("goneUnless")
fun bindGoneUnless(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) {
        VISIBLE
    } else {
        GONE
    }
}

@BindingAdapter(
    value = ["isLoading", "isError", "isEmpty"],
    requireAll = true
)
fun bindErrorLoaderParent(view: View, isLoading: Boolean, isError: Boolean, isEmpty: Boolean) {

    view.visibility = if (!isLoading && !isError && !isEmpty) {
        GONE
    } else {
        VISIBLE
    }
}

@BindingAdapter(value = ["recyclerListAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(
    adapter: RecyclerView.Adapter<*>,
) {
    this.run {
        this.setHasFixedSize(true)
        this.adapter = adapter
    }
}

@BindingAdapter("snackBar")
fun showSnackBar(view: View, message: String?) {
    if (message.isNullOrBlank())
        return
    view.snackBar(message)
}

@BindingAdapter("isViewDisabled")
fun View.isViewDisabled(isViewDisabled: Boolean?) {
    if (null == isViewDisabled)
        return
    isEnabled = isViewDisabled
}
