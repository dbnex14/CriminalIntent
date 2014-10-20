package com.learning.dino.criminalintent;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by dbulj on 19/10/2014.
 */
public class ImageFragment extends DialogFragment {

    public static final String EXTRA_IMAGE_PATH = "com.learning.dino.criminalintent.image_path";
    public static final String EXTRA_IMAGE_ORIENTATION = "com.learning.dino.criminalintent.image_orientation"; //CHALLANGE Ch.20-Image Orientation

    private ImageView mImageView;

    public static ImageFragment newInstance(String imagePath, int orientation){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
        args.putSerializable(EXTRA_IMAGE_ORIENTATION, orientation);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mImageView = new ImageView(getActivity());
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        int orientation = getArguments().getInt(EXTRA_IMAGE_ORIENTATION); //CHALLANGE Ch.20-Image Orientation
        BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(), path);  //first get scalled image

        //CHALLANGE Ch.20-Image Orientation
        //db* if (orientation == Configuration.ORIENTATION_PORTRAIT){
        if (orientation == CrimeCameraActivity.ORIENTATION_PORTRAIT_INVERTED ||
                orientation == CrimeCameraActivity.ORIENTATION_PORTRAIT_NORMAL) { //db*
            image = PictureUtils.getPortraitBitmapDrawable(mImageView, image); //then get portrait view of image if needed
        }

        mImageView.setImageDrawable(image);
        return mImageView;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        PictureUtils.cleanImageView(mImageView);
    }
}
