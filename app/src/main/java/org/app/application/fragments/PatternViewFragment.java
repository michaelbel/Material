package org.app.application.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.app.application.R;
import org.app.material.widget.PatternView;

import java.util.List;

public class PatternViewFragment extends Fragment {

    private final String correctPattern = "24865";

    private PatternView lockView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pattern_fr, container, false);

        FrameLayout layout = (FrameLayout) view.findViewById(R.id.frameLock);
        layout.setBackgroundColor(0xFF64B5F6);

        lockView = (PatternView) view.findViewById(R.id.patternLock);
        lockView.setInStealthMode(false);
        lockView.setOnPatternListener(new PatternView.OnPatternListener() {
            @Override
            public void onPatternDetected(List<PatternView.Cell> pattern, String SimplePattern) {
                if (!SimplePattern.equals(correctPattern)) {
                    lockView.setDisplayMode(PatternView.DisplayMode.Wrong);

                    Toast.makeText(getActivity(), "Pattern is invalid \n" + lockView.getPattern(), Toast.LENGTH_SHORT).show();
                    lockView.clearPattern();
                } else {
                    lockView.setDisplayMode(PatternView.DisplayMode.Correct);
                    Toast.makeText(getActivity(), "Pattern is correct", Toast.LENGTH_SHORT).show();
                }
                super.onPatternDetected(pattern, SimplePattern);
            }
        });

        return view;
    }

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setBackgroundColor(0xFFF0F0F0);

        FrameLayout lockFrame = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.pattern_fr, null);
        lockFrame.setLayoutParams(LayoutHelper.makeFrame(getActivity(), LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
        layout.addView(lockFrame);

        lockView = (MaterialLockView) getActivity().findViewById(R.id.patternLock);
        lockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternDetected(List<MaterialLockView.Cell> pattern, String SimplePattern) {
                if (!SimplePattern.equals(correctPattern)) {
                    lockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);

                    Toast.makeText(getActivity(), "Pattern is invalid", Toast.LENGTH_SHORT).show();
                    lockView.clearPattern();
                } else {
                    lockView.setDisplayMode(MaterialLockView.DisplayMode.Correct);
                    Toast.makeText(getActivity(), "Pattern is correct", Toast.LENGTH_SHORT).show();
                }
                super.onPatternDetected(pattern, SimplePattern);
            }
        });

        return layout;
    }*/
}