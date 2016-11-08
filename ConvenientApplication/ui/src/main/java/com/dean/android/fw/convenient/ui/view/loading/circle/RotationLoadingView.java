package com.dean.android.fw.convenient.ui.view.loading.circle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dean.android.fw.convenient.ui.R;

/**
 * Created by Dean on 16/8/29.
 */
public class RotationLoadingView extends FrameLayout {

    private Context context;

    private RotationProgress rotationProgress;
    private TextView textView;

    public RotationLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        initView();
    }

    private void initView() {
        View layout = LayoutInflater.from(context).inflate(R.layout.view_circle_loading, null);
        rotationProgress = (RotationProgress) layout.findViewById(R.id.rotationProgress);
        textView = (TextView) layout.findViewById(R.id.textView);
        addView(layout);
    }

    public void start(View view) {
        if (view != null)
            view.setVisibility(View.GONE);

        if (rotationProgress != null) {
            rotationProgress.start();
            this.setVisibility(View.VISIBLE);
        }
    }

    public void stop(View view) {
        if (rotationProgress != null) {
            this.setVisibility(View.GONE);
            rotationProgress.stop();
        }

        if (view != null)
            view.setVisibility(View.VISIBLE);
    }

}
