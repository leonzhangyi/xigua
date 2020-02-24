package com.water.melon.ui.player;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.WindowManager;

import java.lang.ref.SoftReference;

public class MyOrientationEventListener extends OrientationEventListener {
    //是否已经处于横屏状态,-1为未初始化，1为非左横屏(手机刘海朝左边的情况,即90度)，2为右横屏(手机刘海朝右边的情况，及270度)
    private int mIsLandRightOrientation = -1;
    private Display mDisplay;
    private SoftReference<Context> mSoftReferenceContext;
    private OrientationListener mListener;

    public MyOrientationEventListener(SoftReference<Context> context, OrientationListener listener) {
        super(context.get(), SensorManager.SENSOR_STATUS_ACCURACY_LOW);
        mSoftReferenceContext = context;
        mListener = listener;
    }

    public interface OrientationListener {
        /**
         * @param orientation 1==横屏  2==竖屏
         */
        void OrientationEventListener(int orientation);
    }

    @Override
    public void onOrientationChanged(int orientation) {
        int ro = orientation % 360;
        // 设置横屏 260
        //|| (ro > 65 && ro < 89)
        if ((ro > 265 && ro < 283) || (ro > 75 && ro < 89)) {
            if (mIsLandRightOrientation == 2 || mIsLandRightOrientation == -1) {
//                if (getScreenRotationOnPhone(mSoftReferenceContext.get()) == Surface.ROTATION_90) {
                //横屏
                mIsLandRightOrientation = 1;
                if (null != mListener) {
                    mListener.OrientationEventListener(mIsLandRightOrientation);
                }
//                }
            }
        } else if ((ro > 340 && ro < 359)) {
            if (mIsLandRightOrientation == 1 || mIsLandRightOrientation == -1) {
//                if (getScreenRotationOnPhone(mSoftReferenceContext.get()) == Surface.ROTATION_0
//                || getScreenRotationOnPhone(mSoftReferenceContext.get()) == Surface.ROTATION_90) {
                //竖屏
                mIsLandRightOrientation = 2;
                if (null != mListener) {
                    mListener.OrientationEventListener(mIsLandRightOrientation);
                }
//                }
            }
        }
    }

    private int getScreenRotationOnPhone(Context context) {
        if (mDisplay == null) {
            mDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        }
        return mDisplay != null ? mDisplay.getRotation() : -1;
    }

}
