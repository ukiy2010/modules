package com.ukiy.fastemplet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by UKIY on 2016/4/6.
 */
public abstract class BaseFragment extends Fragment {
    protected View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false);
        }
        if (mView != null) {
            ViewGroup vg = (ViewGroup) mView.getParent();
            if (vg != null) {
                vg.removeView(mView);
            }
        }
        return mView;
    }

    protected abstract int getLayoutId();

    @Override
    public abstract void onViewCreated(View view, @Nullable Bundle savedInstanceState);

    public <T extends View> T $(int id) {
        return (T) mView.findViewById(id);
    }
}
