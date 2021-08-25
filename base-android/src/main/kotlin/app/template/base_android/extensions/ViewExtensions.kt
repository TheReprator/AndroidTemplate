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

package app.template.base_android.extensions

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import app.template.base_android.util.isAndroidJOrLater
import com.google.android.material.snackbar.Snackbar

fun View.drawableFromViewContext(@DrawableRes resourceId: Int) = ContextCompat.getDrawable(context, resourceId)

fun View.setbackGround(@DrawableRes drawableRes: Int) {
    if (isAndroidJOrLater)
        background = drawableFromViewContext(drawableRes)
    else
        setBackgroundDrawable(drawableFromViewContext(drawableRes))
}

fun View.shortSnackBar(msg: String) = snackBar(msg)

fun View.shortSnackBar(@StringRes msg: Int) = snackBar(context.getString(msg))

fun View.snackBar(msg: String, block: (Snackbar.() -> Unit) = {}) {
    val snackBar = Snackbar.make(
        this, msg,
        Snackbar.LENGTH_SHORT
    )
    block(snackBar)
    snackBar.show()
}
