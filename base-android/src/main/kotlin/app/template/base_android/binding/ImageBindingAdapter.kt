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

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import app.template.base_android.R
import app.template.base_android.extensions.drawableFromViewContext
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import coil.transform.Transformation

@BindingAdapter(
    value = [
        "imageUrl",
        "placeHolder",
        "errorDrawable",
        "dimension",
        "transformation"
    ],
    requireAll = false
)
fun imageLoad(
    view: ImageView,
    imageUrl: String?,
    placeHolder: Drawable?,
    @DrawableRes errorDrawable: Int?,
    dimension: String?,
    transformation: Transformation?
) {
    val errorDrawableValid = view.drawableFromViewContext(errorDrawable ?: R.drawable.ic_error)

    if (imageUrl.isNullOrBlank()) {
        val drawable = errorDrawableValid ?: placeHolder
        view.load(drawable)
    } else {

        val url = if (dimension.isNullOrEmpty())
            imageUrl
        else
            "$imageUrl?$dimension"

        view.load(url) {
            val placeHolderDrawable =
                placeHolder ?: view.drawableFromViewContext(R.drawable.ic_circles_loader)
            placeholder(placeHolderDrawable)

            if (transformation == null)
                transformations(CircleCropTransformation())
            else
                transformations(transformation)
            error(errorDrawableValid)
            scale(Scale.FILL)
        }
    }
}
