package com.example.masterfood.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream

const val HIGH_QUALITY_IMAGE =  100
const val MEDIMUM_QUALITY_IMAGE = 80
const val LOW_QUALITY_IMAGE =  50

//Esta clase permite obtener un ByteArray de diferentes origenes o
//convertir un byArray a un Bitmap
object  ImageUtilities{
    init{

    }
    //Obtener una imagen desde la carpeta res y convertirla a byte array
    fun getByteArrayFromResource(idImage:Int, content: Context):ByteArray{
        var bitmap = BitmapFactory.decodeResource(content.resources, idImage)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,HIGH_QUALITY_IMAGE, stream)
        return stream.toByteArray()
    }

    //Obtiene un byte array desde un bitmap
    fun getByteArrayFromBitmap(bitmap: Bitmap):ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,HIGH_QUALITY_IMAGE, stream)
        return stream.toByteArray()
    }

    //Obtiene un byte arrya desde un drawable
    fun getByteArrayFromDrawable(drawable: Drawable, content: Context):ByteArray{
        var bitMap =  drawable.toBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight,null)
        val stream = ByteArrayOutputStream()
        bitMap.compress(Bitmap.CompressFormat.JPEG,HIGH_QUALITY_IMAGE, stream)
        return stream.toByteArray()
    }

    //Obtiene un bitamap desde byteArray
    fun getBitMapFromByteArray(data:String?): Bitmap {
        val decodedBytes : ByteArray = Base64.decode(data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes,0,decodedBytes.size)
    }
}