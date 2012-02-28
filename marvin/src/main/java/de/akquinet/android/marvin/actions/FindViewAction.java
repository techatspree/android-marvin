package de.akquinet.android.marvin.actions;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;

public interface FindViewAction {
    ViewAction withText(String regex);

    ViewAction withId(int id);
    
    ViewAction root();
}

class FindViewActionImpl<T extends Activity> extends BaseActionImpl implements FindViewAction  {
    private final T activity;
    
    public T getActivity() {
        return activity;
    }

    public FindViewActionImpl(Instrumentation instrumentation,
                              ExtendedActivityMonitor activityMonitor, T activity) {
        super(instrumentation, activityMonitor);

        this.activity = activity;
    }

    @Override
    public ViewAction withText(final String regex) {
        View rootView = activity.getWindow().getDecorView();

        View theView = findView(rootView, new ViewFilter() {
            @Override
            public boolean accept(View view) {
                return view instanceof TextView
                        && ((TextView) view).getText().toString()
                        .matches(regex);
            }
        });

        return new ViewActionImpl<T>(this, theView);
    }

    @Override
    public ViewAction withId(int id) {
        return new ViewActionImpl<T>(this, activity.findViewById(id));
    }

    @Override
    public ViewAction root() {
        return new ViewActionImpl<T>(this, activity.getWindow().getDecorView());
    }

    public ViewAction with(ViewFilter filter) {
        View rootView = activity.getWindow().getDecorView();

        View theView = findView(rootView, filter);
        return new ViewActionImpl<T>(this, theView);
    }

    private View findView(View view, ViewFilter filter) {
        if (filter.accept(view)) {
            return view;
        }

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View childView = findView(group.getChildAt(i), filter);
                if (childView != null) {
                    return childView;
                }
            }
        }

        return null;
    }
}
