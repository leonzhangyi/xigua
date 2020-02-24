package com.water.melon.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.water.melon.application.MyApplication;


/**
 * @Package: com.feifanzhixing.o2ozhiye.utils
 * @ClassName: SetTextViewDrawable.java
 * @Description:TODO
 * @author: pifeifan
 * @date: 2015-8-19
 * <p>
 * Copyright @ 2015 51ZhiYe
 */
public class SetTextViewDrawable {
    /**
     * 设置Title TextView图片 drawableTop
     *
     * @param textView
     * @param drawableId
     */
    public static void setTopView(final TextView textView, int drawableId) {
        Drawable drawable = MyApplication.getDrawableByResId(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    /**
     * 设置Title TextView图片 drawableBottom
     *
     * @param view
     * @param drawableId
     */
    public static void setBottomView(final View view, int drawableId) {
        Drawable drawable = MyApplication.getDrawableByResId(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (view instanceof TextView) {
            ((TextView)view).setCompoundDrawables(null, null, null, drawable);
        }else if (view instanceof RadioButton){
            ((RadioButton)view).setCompoundDrawables(null, null, null, drawable);
        }
    }

    /**
     * 设置Title TextView图片 drawableLeft
     *
     * @param textView
     * @param drawableId
     */
    public static void setLeftView(final TextView textView, int drawableId) {
        Drawable drawable = MyApplication.getDrawableByResId(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 设置Title TextView图片 drawableRight
     *
     * @param textView
     * @param drawableId
     */
    public static void setRightView(final TextView textView, int drawableId) {
        if (drawableId != 0) {
            Drawable drawable = MyApplication.getDrawableByResId(drawableId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, null, drawable, null);
        } else {
            //隐藏图片
            textView.setCompoundDrawables(null, null, null, null);
        }
    }

    /**
     * 设置Title TextView图片 drawableLeft和drawableRight
     *
     * @param textView
     * @param leftDrawableId
     * @param rightDrawableId
     */
    public static void setLeftAndRightView(final TextView textView, int leftDrawableId, int rightDrawableId) {
        Drawable drawableLeft = MyApplication.getDrawableByResId(leftDrawableId);
        drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
        Drawable drawableRight = MyApplication.getDrawableByResId(rightDrawableId);
        drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        textView.setCompoundDrawables(drawableLeft, null, drawableRight, null);
    }


    /**
     * 取消Title 所有drawable
     */
    public static void claearView(final View view) {
        if (view instanceof TextView) {
            ((TextView)view).setCompoundDrawables(null, null, null, null);
        }else if (view instanceof RadioButton){
            ((RadioButton)view).setCompoundDrawables(null, null, null, null);
        }
    }

}

