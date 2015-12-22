package de.luebben.omgwtfnzbs;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;

/**
 * Created by Matthias on 21.12.2015.
 */
public class FillParentBehavior extends CoordinatorLayout.Behavior<RelativeLayout> {

    public FillParentBehavior() {
    }

    public FillParentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RelativeLayout child, View dependency) {

        boolean x = dependency instanceof AppBarLayout
                || dependency instanceof Toolbar
                || dependency instanceof SearchView;
        Log.d("XXX", "layoutDependsOn: " + x);
        return x;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RelativeLayout child, View dependency) {

//        final CoordinatorLayout.Behavior behavior =
//                ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();
//        if (behavior instanceof CoordinatorLayout.Behavior) {
//            // Offset the child so that it is below the app-bar (with any overlap)
//
//
//
//            final int appBarOffset = ((CoordinatorLayout.Behavior) behavior)
//                    .getTopBottomOffsetForScrollingSibling();
//            final int expandedMax = dependency.getHeight() - mOverlayTop;
//            final int collapsedMin = parent.getHeight() - child.getHeight();
//
//            if (mOverlayTop != 0 && dependency instanceof AppBarLayout) {
//                // If we have an overlap top, and the dependency is an AppBarLayout, we control
//                // the offset ourselves based on the appbar's scroll progress. This is so that
//                // the scroll happens sequentially rather than linearly
//                final int scrollRange = ((AppBarLayout) dependency).getTotalScrollRange();
//                setTopAndBottomOffset(AnimationUtils.lerp(expandedMax, collapsedMin,
//                        Math.abs(appBarOffset) / (float) scrollRange));
//            } else {
//                setTopAndBottomOffset(dependency.getHeight() - mOverlayTop + appBarOffset);
//            }
//        }
        return false;



        //return super.onDependentViewChanged(parent, child, dependency);
//        Log.d("XXX", "onDependentViewChanged");
//
//        int maxHeight = parent.getHeight(); //.getLayoutParams().height;
//        int toolbarHeight = dependency.getHeight(); //.getLayoutParams().height;
//
//
//
//        ViewGroup.LayoutParams p = child.getLayoutParams();
//
//        p.height = maxHeight - toolbarHeight - 20;
//        child.setLayoutParams(p);
//
//        child.setTranslationY(dependency.getHeight() - dependency.getTranslationY());
//
//        Log.d("XXX", "dependency.translateY: " + dependency.getTranslationY());
//        Log.d("XXX", "dependency.height: " + dependency.getHeight());
//
//        Log.d("XXX", "parent.height: " + parent.getHeight());
//        Log.d("XXX", "parent.translateY: " + parent.getTranslationY());
//
//        return true;
    }
}
