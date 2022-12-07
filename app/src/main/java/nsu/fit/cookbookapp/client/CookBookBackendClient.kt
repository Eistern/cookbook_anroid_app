package nsu.fit.cookbookapp.client

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class CookBookBackendClient {
    private val client = OkHttpClient()

    fun postPicture(pictureLocalUri: InputStream?) {
        val scaledPictureBitmap = loadScaledPictureIntoBitmap(pictureLocalUri)

        Log.d(TAG,"Created scaled bitmap")

        val stream = ByteArrayOutputStream()
        scaledPictureBitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream)
        scaledPictureBitmap.recycle()
        val scaledArray = stream.toByteArray()

        Log.d(TAG,"Dumped into array")

        val requestBody = scaledArray.toRequestBody("image/jpeg".toMediaType())

        val request = Request.Builder()
            .url(BACKEND_URL)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "Got response $response")
            }
        })
    }

    private fun loadScaledPictureIntoBitmap(initialPictureFile: InputStream?, scaleTo: Int = 640): Bitmap {
        val initialBm = BitmapFactory.decodeStream(initialPictureFile)

        val scaledBitmap = Bitmap.createScaledBitmap(initialBm, scaleTo, scaleTo, true)
        if (initialBm !== scaledBitmap) {
            initialBm.recycle()
        }

        return scaledBitmap
    }

    companion object {
        const val BACKEND_URL = "https://c42b991d-0d1a-4761-a986-95e97ca1e4c5.mock.pstmn.io/test"
        const val TAG = "CookBookClient"
    }
}