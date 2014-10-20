package com.learning.dino.criminalintent;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.OrientationEventListener;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by dbulj on 14/10/2014.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {

    //CHALLANGE Ch.20-Image Orientation
    private OrientationEventListener mOrientationEventListener;
    private int mOrientation =  -1;
    public static final int ORIENTATION_PORTRAIT_NORMAL =  1;
    public static final int ORIENTATION_PORTRAIT_INVERTED =  2;
    public static final int ORIENTATION_LANDSCAPE_NORMAL =  3;
    public static final int ORIENTATION_LANDSCAPE_INVERTED =  4;

    @Override
    protected Fragment createFragment(){
        return new CrimeCameraFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        //Hide the window title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Hide status bar and other OS-level chrome
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
    }

    //CHALLANGE Ch.20-Image Orientation
    @Override
    protected void onResume() {
        super.onResume();

        if (mOrientationEventListener == null) {
            mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {

                @Override
                public void onOrientationChanged(int orientation) {

                    // determine our orientation based on sensor response
                    if (orientation >= 315 || orientation < 45) {
                        if (mOrientation != ORIENTATION_PORTRAIT_NORMAL) {
                            mOrientation = ORIENTATION_PORTRAIT_NORMAL;
                        }
                    }
                    else if (orientation < 315 && orientation >= 225) {
                        if (mOrientation != ORIENTATION_LANDSCAPE_NORMAL) {
                            mOrientation = ORIENTATION_LANDSCAPE_NORMAL;
                        }
                    }
                    else if (orientation < 225 && orientation >= 135) {
                        if (mOrientation != ORIENTATION_PORTRAIT_INVERTED) {
                            mOrientation = ORIENTATION_PORTRAIT_INVERTED;
                        }
                    }
                    else { // orientation <135 && orientation > 45
                        if (mOrientation != ORIENTATION_LANDSCAPE_INVERTED) {
                            mOrientation = ORIENTATION_LANDSCAPE_INVERTED;
                        }
                    }
                }
            };
        }
        if (mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        }
    }

    //CHALLANGE Ch.20-Image Orientation
    @Override protected void onPause() {
        super.onPause();
        mOrientationEventListener.disable();
    }

    //CHALLANGE Ch.20-Image Orientation
    int getOrientation() {
        return mOrientation;
    }
}
