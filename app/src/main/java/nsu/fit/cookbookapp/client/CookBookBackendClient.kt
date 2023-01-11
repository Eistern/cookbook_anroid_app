package nsu.fit.cookbookapp.client

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.function.Consumer


class CookBookBackendClient {
    private val client = OkHttpClient()

    @OptIn(ExperimentalStdlibApi::class)
    private val deserializer = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
        .adapter<List<CookBookRecipe>>()

    fun postPicture(pictureLocalUri: InputStream?, resultConsumer: Consumer<List<CookBookRecipe>>) {
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
                Log.e(TAG, "Got error for the call $call", e)
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "Got response $response")
                if (response.isSuccessful) {
                    response.body.use {
                        val parsed = deserializer.fromJson(it!!.source()) ?: return
                        resultConsumer.accept(parsed)
                    }
                }
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
        const val BACKEND_URL = "http://192.168.0.10:8080/main"
        const val TAG = "CookBookClient"
    }
}