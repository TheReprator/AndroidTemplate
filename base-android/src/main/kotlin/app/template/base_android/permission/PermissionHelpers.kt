/*
 * Copyright 2021
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

package app.template.base_android.permission

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.annotation.RequiresApi

/**
 * see [RequestPermission]
 */
suspend fun ActivityResultManager.requestPermission(permission: String): Boolean {
    return requestResult(RequestPermission(), permission) ?: false
}

/**
 * see [RequestMultiplePermissions]
 */
suspend fun ActivityResultManager.requestPermissions(vararg permission: String): Map<String, Boolean>? {
    return requestResult(RequestMultiplePermissions(), arrayOf(*permission))
}

/**
 * see [TakePicturePreview]
 */
suspend fun ActivityResultManager.takePicturePreview(): Bitmap? {
    return requestResult(TakePicturePreview(), null)
}

/**
 * see [TakePicture]
 */
suspend fun ActivityResultManager.takePicture(destination: Uri): Boolean {
    return requestResult(TakePicture(), destination) ?: false
}

/**
 * see [TakeVideo]
 */
suspend fun ActivityResultManager.takeVideo(destination: Uri): Bitmap? {
    return requestResult(TakeVideo(), destination)
}

/**
 * see [PickContact]
 */
suspend fun ActivityResultManager.pickContact(): Uri? {
    return requestResult(PickContact(), null)
}

/**
 * see [GetContent]
 */
suspend fun ActivityResultManager.getContent(mimeType: String): Uri? {
    return requestResult(GetContent(), mimeType)
}

/**
 * see [GetMultipleContents]
 */
@RequiresApi(18)
suspend fun ActivityResultManager.getMultipleContents(mimeType: String): List<Uri> {
    return requestResult(GetMultipleContents(), mimeType) ?: emptyList()
}

/**
 * see [OpenDocument]
 */
@RequiresApi(19)
suspend fun ActivityResultManager.openDocument(mimeTypes: Array<String>): Uri? {
    return requestResult(OpenDocument(), mimeTypes)
}

/**
 * see [OpenMultipleDocuments]
 */
@RequiresApi(19)
suspend fun ActivityResultManager.openMultipleDocuments(mimeTypes: Array<String>): List<Uri> {
    return requestResult(OpenMultipleDocuments(), mimeTypes) ?: emptyList()
}

@RequiresApi(21)
suspend fun ActivityResultManager.openDocumentTree(startingLocation: Uri? = null): Uri? {
    return requestResult(OpenDocumentTree(), startingLocation)
}

/**
 * see [CreateDocument]
 */
@RequiresApi(19)
suspend fun ActivityResultManager.createDocument(fileName: String): Uri? {
    return requestResult(CreateDocument(), fileName)
}
