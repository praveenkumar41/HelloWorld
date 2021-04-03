package com.example.project2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class Non_swipable_viewpage_for_imagefilter extends ViewPager
{

    public Non_swipable_viewpage_for_imagefilter(@NonNull Context context) {
        super(context);
        setmyscroller();
    }

    public Non_swipable_viewpage_for_imagefilter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setmyscroller();
    }


    private void setmyscroller()
    {
       try{
           Class<?> viewpager=ViewPager.class;
           Field scroller=viewpager.getDeclaredField("mScroller");
           scroller.setAccessible(true);
           scroller.set(this,new MyScroller(getContext()));
       } catch (NoSuchFieldException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       }
    }

    private class MyScroller extends Scroller{

        public MyScroller(Context context) {
            super(context,new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
