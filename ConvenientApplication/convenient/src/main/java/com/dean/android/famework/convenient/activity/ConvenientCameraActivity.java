package com.dean.android.famework.convenient.activity;

import android.content.Intent;

import com.dean.android.famework.convenient.bitmap.util.BitmapUtil;

/**
 * 相机、相册的Activity
 * <p/>
 * Created by Dean on 16/8/2.
 */
public abstract class ConvenientCameraActivity extends ConvenientActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BitmapUtil.ACTION_OPEN_PHOTO_ALBUM:
                if (data == null)
                    return;

                albumResult(data);
                break;
            case BitmapUtil.ACTION_OPEN_CAMERA:
                cameraResult(data);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    protected abstract void albumResult(Intent bitmapIntent);

    protected abstract void cameraResult(Intent bitmapIntent);

}
