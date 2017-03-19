package com.example.root.atmdata.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.google.android.gms.maps.SupportMapFragment;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 *
 */
public class ScrollCompatibleMapFragment extends SupportMapFragment {

    private OnTouchListener touchListener;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View layout = super.onCreateView(layoutInflater, viewGroup, bundle);
        TouchableWrapper frameLayout = new TouchableWrapper(getActivity());
        frameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ((ViewGroup) layout).addView(frameLayout, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        return layout;
    }

    public void setTouchListener(OnTouchListener touchListener) {
        this.touchListener = touchListener;
    }

    public interface OnTouchListener {
        void onTouch();
    }

    private class TouchableWrapper extends FrameLayout {

        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchListener.onTouch();
                    break;
                case MotionEvent.ACTION_UP:
                    touchListener.onTouch();
                    break;
            }
            return super.dispatchTouchEvent(event);
        }
    }
}
