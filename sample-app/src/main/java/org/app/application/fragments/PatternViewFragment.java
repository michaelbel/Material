package org.app.application.fragments;

import android.os.Bundle;
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

    private PatternView lockView;
    private final String correctPattern = "24865";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pattern, vGroup, false);

        FrameLayout layout = (FrameLayout) view.findViewById(R.id.frameLock);
        layout.setBackgroundColor(0xFF64B5F6);

        lockView = (PatternView) view.findViewById(R.id.patternLock);
        lockView.setVibrate(true);
        lockView.setOnPatternListener(new PatternView.OnPatternListener() {
            @Override
            public void onPatternDetected(List<PatternView.Cell> pattern, String SimplePattern) {
                if (!SimplePattern.equals(correctPattern)) {
                    Toast.makeText(getActivity(), getString(R.string.PatternInvalid), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.PatternCorrect), Toast.LENGTH_SHORT).show();
                }

                lockView.clearPattern();
                super.onPatternDetected(pattern, SimplePattern);
            }
        });

        return view;
    }
}