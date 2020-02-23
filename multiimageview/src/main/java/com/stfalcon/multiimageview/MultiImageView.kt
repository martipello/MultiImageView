/*******************************************************************************
 * Copyright 2016 stfalcon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.stfalcon.multiimageview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import java.util.*
import kotlin.math.max


/**
 * Created by Anton Bevza on 12/22/16.
 */

class MultiImageView @JvmOverloads constructor (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageView(context, attrs) {

    val TOP_RIGHT = 0
    val TOP_LEFT = 1
    val BOTTOM_RIGHT = 2
    val BOTTOM_LEFT = 3

    //Shape of view
    var shape = Shape.NONE
        set(value) {
            field = value
            invalidate()
        }
    //Corners radius for rectangle shape
    var rectCorners = 100

    private val bitmaps = ArrayList<Bitmap>()
    private val path = Path()
    private val rect = RectF()
    private var multiDrawable: Drawable? = null

    /**
     * Add image to view
     */
    fun addImage(bitmap: Bitmap) {
        bitmaps.add(bitmap)
        refresh()
    }

    /**
     * Add image to view
     */
    fun addAllImages(bitmaps: MutableList<Bitmap>) {
        this.bitmaps.clear()
        this.bitmaps.addAll(bitmaps)
        refresh()
    }

    /**
     * Remove all images
     */
    fun clear() {
        bitmaps.clear()
        refresh()
    }

    /**
     * Get images count
     */
    fun getBitmapCount(): Int {
        return bitmaps.size
    }

    /**
     * Set badge text
     */
    fun setBadgeText() {

    }

//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        refresh()
//    }

    init {
        attrs?.let {
            bitmaps.clear()
            val typedArray = context.obtainStyledAttributes(it, R.styleable.MultiImageView)
            shape = typedArray.getEnum(R.styleable.MultiImageView_shape, Shape.NONE)
            rectCorners = typedArray.getInt(R.styleable.MultiImageView_corner_radius, 100)
            val badgeColor = typedArray.getColor(R.styleable.MultiImageView_badge_color, getThemeAccentColor(context))
            val badgeTextColor = typedArray.getColor(R.styleable.MultiImageView_badge_text_color, android.R.attr.textColor)
            val badgeLocation = typedArray.getString(R.styleable.MultiImageView_badge_location)
            val badgeMinCount = typedArray.getInt(R.styleable.MultiImageView_badge_shown_from_count, 4)
            val badgeMaxCount = typedArray.getInt(R.styleable.MultiImageView_badge_shown_to_count, 9)
            typedArray.recycle()
        }
    }


    /**
     * recreate MultiDrawable and set it as Drawable to ImageView
     */
    private fun refresh() {
        multiDrawable = MultiDrawable(bitmaps)
        setImageDrawable(multiDrawable)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            if (drawable != null) {
                //if shape not set - just draw
                if (shape != Shape.NONE) {
                    path.reset()
                    //ImageView size
                    rect.set(0f, 0f, width.toFloat(), height.toFloat())
                    if (shape == Shape.RECTANGLE) {
                        //Rectangle with corners
                        path.addRoundRect(rect, rectCorners.toFloat(),
                                rectCorners.toFloat(), Path.Direction.CW)
                    } else {
                        //Oval
                        path.addOval(rect, Path.Direction.CW)
                    }
                    //Clip with shape
                    canvas.clipPath(path)
                }
                super.onDraw(canvas)
            }
        }
    }


    private fun getThemeAccentColor(context: Context): Int {
        val colorAttr: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.R.attr.colorAccent
        } else {
            //Get colorAccent defined for AppCompat
            context.resources.getIdentifier("colorAccent", "attr", context.packageName)
        }
        val outValue = TypedValue()
        context.theme.resolveAttribute(colorAttr, outValue, true)
        return outValue.data
    }


    //Types of shape
    enum class Shape {
        CIRCLE, RECTANGLE, NONE
    }

    inline fun <reified T : Enum<T>> TypedArray.getEnum(index: Int, default: T) =
            getInt(index, -1).let { if (it >= 0) enumValues<T>()[it] else default
            }
}

class MultiDrawable(private val bitmaps: ArrayList<Bitmap>) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val items = ArrayList<PhotoItem>()

    /**
     * Create PhotoItem with position and size depends of count of images
     */
    private fun init() {
        items.clear()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true

        when {
            bitmaps.size == 1 -> {
                val bitmap = scaleCenterCrop(bitmaps[0], bounds.width(), bounds.height())
                items.add(PhotoItem(bitmap, Rect(0, 0, bounds.width(), bounds.height())))
            }
            bitmaps.size == 2 -> {
                val bitmap1 = scaleCenterCrop(bitmaps[0], bounds.width() / 2, bounds.height())
                val bitmap2 = scaleCenterCrop(bitmaps[1], bounds.width() / 2, bounds.height())
                items.add(PhotoItem(bitmap1, Rect(0, 0, bounds.width(), bounds.height())))
                items.add(PhotoItem(bitmap2, Rect(bounds.width() / 2, 0, bounds.width() + bounds.width() / 2, bounds.height())))
            }
            bitmaps.size == 3 -> {
                val bitmap1 = scaleCenterCrop(bitmaps[0], bounds.width() / 2, bounds.height())
                val bitmap2 = scaleCenterCrop(bitmaps[1], bounds.width(), bounds.height())
                val bitmap3 = scaleCenterCrop(bitmaps[2], bounds.width(), bounds.height())
                items.add(PhotoItem(bitmap1, Rect(0, 0, bounds.width(), bounds.height())))
                items.add(PhotoItem(bitmap2, Rect(bounds.width() / 2, 0, bounds.width(), bounds.height() / 2)))
                items.add(PhotoItem(bitmap3, Rect(bounds.width() / 2, bounds.height() / 2, bounds.width(), bounds.height())))
            }
            bitmaps.size > 3 -> {
                val bitmap1 = scaleCenterCrop(bitmaps[0], bounds.width(), bounds.height())
                val bitmap2 = scaleCenterCrop(bitmaps[1], bounds.width(), bounds.height())
                val bitmap3 = scaleCenterCrop(bitmaps[2], bounds.width(), bounds.height())
                val bitmap4 = scaleCenterCrop(bitmaps[3], bounds.width(), bounds.height())
                items.add(PhotoItem(bitmap1, Rect(0, 0, bounds.width() / 2, bounds.height() / 2)))
                items.add(PhotoItem(bitmap3, Rect(0, bounds.height() / 2, bounds.width() / 2, bounds.height())))
                items.add(PhotoItem(bitmap2, Rect(bounds.width() / 2, 0, bounds.width(), bounds.height() / 2)))
                items.add(PhotoItem(bitmap4, Rect(bounds.width() / 2, bounds.height() / 2, bounds.width(), bounds.height())))
            }
        }
    }

    override fun draw(canvas: Canvas) {
        items.forEach {
            canvas.drawBitmap(it.bitmap, bounds, it.position, paint)
        }


    }

    /**
     * scale and center crop image
     */
//    private fun scaleCenterCrop(source: Bitmap, newHeight: Int, newWidth: Int): Bitmap {
//        return ThumbnailUtils.extractThumbnail(source, newWidth, newHeight)
//    }


    private fun scaleCenterCrop(source: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val sourceWidth = source.width
        val sourceHeight = source.height

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        val xScale = newWidth.toFloat() / sourceWidth
        val yScale = newHeight.toFloat() / sourceHeight
        val scale = max(xScale, yScale)

        // Now get the size of the source bitmap when scaled
        val scaledWidth = scale * sourceWidth
        val scaledHeight = scale * sourceHeight

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        val left = (newWidth - scaledWidth) / 2
        val top = (newHeight - scaledHeight) / 2

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        val dest = Bitmap.createBitmap(newWidth, newHeight, source.config)
        val canvas = Canvas(dest)
        canvas.drawBitmap(source, null, targetRect, null)

        return dest
    }

    /***
     * Data class for store bitmap and position
     */
    data class PhotoItem(val bitmap: Bitmap, val position: Rect)


    //***Needed to override***//
    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        init()
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }


    //***------------------***//
}

