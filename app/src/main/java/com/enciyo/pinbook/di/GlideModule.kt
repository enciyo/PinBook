package com.enciyo.pinbook.di

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey


@GlideModule
class GlideModule : AppGlideModule() {

  override fun applyOptions(context: Context, builder: GlideBuilder) {
    val calculator = MemorySizeCalculator.Builder(context)
        .setMemoryCacheScreens(2f)
        .build()
    builder.setDefaultRequestOptions(requestOptions(context))
    builder.setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))
  }



  private fun requestOptions(context: Context): RequestOptions? {
    return RequestOptions()
        .signature(ObjectKey(
            System.currentTimeMillis() / (24 * 60 * 60 * 1000)))
        .override(200, 200)
        .centerCrop()
        .encodeFormat(Bitmap.CompressFormat.PNG)
        .encodeQuality(100)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .skipMemoryCache(false)
  }

}