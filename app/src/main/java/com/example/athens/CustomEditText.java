package com.example.athens;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;


public class CustomEditText extends AppCompatEditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {
    private Drawable mCustomTextIcon;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnTouchListener mOnTouchListener;

    //構造方法
    public CustomEditText(Context context) {
        super(context);
        init(context);

    }
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }


    @Override
    public void setOnFocusChangeListener(final OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    private void init( final Context context){
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_error);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable); //Wrap the drawable so that it can be tinted pre Lollipop
        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        mCustomTextIcon = wrappedDrawable;
        mCustomTextIcon.setBounds(0, 0, mCustomTextIcon.getIntrinsicHeight(), mCustomTextIcon.getIntrinsicHeight());
        setCustomIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);

    }

    private void setCustomIconVisible(boolean visible) {
        mCustomTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? mCustomTextIcon : null,
                compoundDrawables[3]);
    }

    @Override
    public void onFocusChange(final View view, final boolean hasFocus) {
        if (hasFocus) {
            setCustomIconVisible(getText().length() > 0);
        } else {
            setCustomIconVisible(false);
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(view, hasFocus);
        }

    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (mCustomTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mCustomTextIcon.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText("");
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(view, motionEvent);

    }



    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (isFocused()) {
            setCustomIconVisible(charSequence.length() > 0);
        }
    }
    @Override
    public void afterTextChanged(Editable editable) {
    }
}