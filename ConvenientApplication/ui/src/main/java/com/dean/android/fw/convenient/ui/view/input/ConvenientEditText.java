package com.dean.android.fw.convenient.ui.view.input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.dean.android.framework.convenient.util.TextUtils;

/**
 * 此类主要解决直接设置文字后，焦点光标在文字之前显示问题
 * <p>
 * Created by dean on 2017/5/27.
 */
@SuppressLint("AppCompatCustomView")
public class ConvenientEditText extends EditText {

    public ConvenientEditText(Context context) {
        super(context);
    }

    public ConvenientEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String content = TextUtils.isEmpty(text) ? "" : text.toString();
        super.setText(content, type);

        setSelection(TextUtils.isEmpty(text) ? 0 : text.length());
    }
}
