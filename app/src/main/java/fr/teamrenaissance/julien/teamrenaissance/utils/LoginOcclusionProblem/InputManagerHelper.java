package fr.teamrenaissance.julien.teamrenaissance.utils.LoginOcclusionProblem;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import fr.teamrenaissance.julien.teamrenaissance.R;

public class InputManagerHelper {

    private Activity activity;
    private int lastKeyBoardHeight;
    private int offset;

    private InputManagerHelper(Activity activity) {
        this.activity = activity;
    }

    public static InputManagerHelper attachToActivity(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return new InputManagerHelper(activity);
    }

    public InputManagerHelper bind(ViewGroup viewGroup, View lastVisibleView) {
        if (viewGroup instanceof KeyboardListenLayout) {
            bindKeyboardListenLayout((KeyboardListenLayout) viewGroup, lastVisibleView);
        } else {
            if (viewGroup instanceof RecyclerView || viewGroup instanceof ScrollView) {
                bindViewGroup(viewGroup);
            } else {
                bindLayout(viewGroup, lastVisibleView);
            }
        }
        return this;
    }

    public InputManagerHelper bind(ViewGroup viewGroup) {
        bind(viewGroup, activity.getCurrentFocus());
        return this;
    }

    public InputManagerHelper offset(int offset) {
        this.offset = offset;
        return this;
    }

    private void bindKeyboardListenLayout(final KeyboardListenLayout keyboardListenLayout, final View view) {
        keyboardListenLayout.setOnSizeChangedListener(new KeyboardListenLayout.onSizeChangedListener() {
            @Override
            public void onChanged(final boolean showKeyboard, final int h, final int oldh) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        View lastVisibleView = view;
                        if (lastVisibleView == null) {
                            lastVisibleView = activity.getCurrentFocus();
                        }
                        if (showKeyboard) {
                            int keyboardTop = oldh - (oldh - h);
                            int[] location = new int[2];
                            lastVisibleView.getLocationOnScreen(location);
                            int lastVisibleViewBottom = location[1] + lastVisibleView.getHeight();
                            int reSizeLayoutHeight = lastVisibleViewBottom - keyboardTop;
                            reSizeLayoutHeight -= getStatusBarHeight();
                            if (null != (((AppCompatActivity) activity).getSupportActionBar()) && (((AppCompatActivity) activity).getSupportActionBar()).isShowing()) {
                                reSizeLayoutHeight -= getActionBarHeight();
                            }
                            reSizeLayoutHeight += offset;
                            if (reSizeLayoutHeight > 0)
                                keyboardListenLayout.setPadding(0, -reSizeLayoutHeight, 0, 0);
                        } else {
                            keyboardListenLayout.setPadding(0, 0, 0, 0);
                        }
                    }
                }, 50);
            }
        });
    }

    private void bindLayout(final ViewGroup viewGroup, final View view) {
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        View lastVisibleView = view;
                        if (lastVisibleView == null) {
                            lastVisibleView = activity.getCurrentFocus();
                        }
                        int screenHeight = viewGroup.getRootView().getHeight();
                        Rect r = new Rect();
                        viewGroup.getWindowVisibleDisplayFrame(r);
                        int keyboardHeight = screenHeight - (r.bottom - r.top);
                        if (keyboardHeight == lastKeyBoardHeight) {
                            return;
                        }
                        lastKeyBoardHeight = keyboardHeight;
                        if (keyboardHeight < 300) {
                            viewGroup.setPadding(0, 0, 0, 0);
                        } else {
                            int keyboardTop = screenHeight - keyboardHeight;
                            int[] location = new int[2];
                            lastVisibleView.getLocationOnScreen(location);
                            int lastVisibleViewBottom = location[1] + lastVisibleView.getHeight();
                            int reSizeLayoutHeight = lastVisibleViewBottom - keyboardTop;
                            reSizeLayoutHeight -= getStatusBarHeight();
                            reSizeLayoutHeight += offset;
                            if (reSizeLayoutHeight > 0) {
                                viewGroup.setPadding(0, -reSizeLayoutHeight, 0, 0);
                            }
                        }
                    }
                }, 50);
            }
        });
    }

    private void bindViewGroup(final ViewGroup viewGroup) {
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int screenHeight = viewGroup.getRootView().getHeight();
                        Rect r = new Rect();
                        viewGroup.getWindowVisibleDisplayFrame(r);
                        int keyboardHeight = screenHeight - (r.bottom - r.top);
                        if (keyboardHeight == lastKeyBoardHeight) {
                            return;
                        }
                        lastKeyBoardHeight = keyboardHeight;
                        View view = activity.getWindow().getCurrentFocus();
                        if (keyboardHeight > 300 && null != view) {
                            if (view instanceof TextView) {
                                int keyboardTop = screenHeight - keyboardHeight;
                                int[] location = new int[2];
                                view.getLocationOnScreen(location);
                                int viewBottom = location[1] + view.getHeight();
                                if (viewBottom <= keyboardTop)
                                    return;
                                int reSizeLayoutHeight = viewBottom - keyboardTop;
                                reSizeLayoutHeight -= getStatusBarHeight();
                                reSizeLayoutHeight += offset;
                                if (viewGroup instanceof ScrollView) {
                                    ((ScrollView) viewGroup).smoothScrollBy(0, reSizeLayoutHeight);
                                } else if (viewGroup instanceof RecyclerView) {
                                    ((RecyclerView) viewGroup).smoothScrollBy(0, reSizeLayoutHeight);
                                }
                            }
                        }
                    }
                }, 50);
            }
        });
    }


    private int getStatusBarHeight() {
        int result = 0;
        int resId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = activity.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    private int getActionBarHeight() {
        if (null != (((AppCompatActivity) activity).getSupportActionBar())) {
            TypedValue typedValue = new TypedValue();
            if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
                return TypedValue.complexToDimensionPixelSize(typedValue.data, activity.getResources().getDisplayMetrics());
            }
        }
        return 0;
    }

}
