package com.goindiainfotech.kotlinmvvm.themoviedb.ui.custom

import android.content.Context
import android.util.AttributeSet
import com.goindiainfotech.kotlinmvvm.R
import com.goindiainfotech.kotlinmvvm.drag.DragView
import com.goindiainfotech.kotlinmvvm.drag.utils.inflate
import com.goindiainfotech.kotlinmvvm.drag.utils.reWidth


import kotlinx.android.synthetic.main.layout_top.view.*

class DragSource @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : DragView(context, attrs, defStyleAttr) {

    var mWidthWhenMax = 0

    var mWidthWhenMiddle = 0

    var mWidthWhenMin = 0

    init {
        getFrameFirst().addView(inflate(R.layout.layout_top))
        getFrameSecond().addView(inflate(R.layout.layout_bottom))
    }

    override fun initFrame() {
        mWidthWhenMax = width

        mWidthWhenMiddle = (width - mPercentWhenMiddle * mMarginEdgeWhenMin).toInt()

        mWidthWhenMin = mHeightWhenMin * 22 / 9

        super.initFrame()
    }

    override fun refreshFrameFirst() {
        super.refreshFrameFirst()

        val width = if (mCurrentPercent < mPercentWhenMiddle) {
            (mWidthWhenMax - (mWidthWhenMax - mWidthWhenMiddle) * mCurrentPercent)
        } else {
            (mWidthWhenMiddle - (mWidthWhenMiddle - mWidthWhenMin) * (mCurrentPercent - mPercentWhenMiddle) / (1 - mPercentWhenMiddle))
        }

        frameTop.reWidth(width.toInt())
    }
}