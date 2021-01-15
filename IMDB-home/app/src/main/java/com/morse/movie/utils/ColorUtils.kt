package com.morse.movie.utils

import android.content.Context
import android.graphics.drawable.Drawable
import com.morse.movie.R

data class Color(
    public var drawableRes: Drawable? = null,
    public var colorRes: Int? = null,
    public var frameRes: Drawable? = null
)

class ColorUtils(private val context: Context) {

    private var listOfResource : ArrayList<Color> = arrayListOf(

        Color(
            context?.getDrawable(R.drawable.movie_card_bg_except_left_corners )!!,
            context?.getColor(R.color.colorOfGreen )!!,
            context?.getDrawable(R.drawable.ic_image_frame_green)!!
        ) ,

        Color(
            context?.getDrawable(R.drawable.movie_card_bg_except_left_corners_amber )!!,
            context?.getColor(R.color.colorOfAmber )!!,
            context?.getDrawable(R.drawable.ic_image_frame_amber)!!
        ) ,
        Color(
            context?.getDrawable(R.drawable.movie_card_bg_except_left_corners_color )!!,
            context?.getColor(R.color.colorOfRedSpecial )!!,
            context?.getDrawable(R.drawable.ic_image_frame_red)!!
        ) ,
        Color(
            context?.getDrawable(R.drawable.movie_card_bg_except_left_corners_red )!!,
            context?.getColor(R.color.colorOfBlueSpecial )!!,
            context?.getDrawable(R.drawable.ic_image_frame_blue)!!
        )
    )

    public fun getRandomTheme () : Color {
        return listOfResource?.random()
    }

}