package fr.teamrenaissance.julien.teamrenaissance.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class BaseScrollView extends ScrollView{
    public BaseScrollView(Context context) {
        super(context);
    }

    public BaseScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
