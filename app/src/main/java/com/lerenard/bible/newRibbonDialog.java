package com.lerenard.bible;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mc on 03-Jan-17.
 */

public class NewRibbonDialog extends DialogFragment {

    private static final String TAG = "NewRibbonDialog_";
    private RibbonNameListener listener;

    public void setListener(RibbonNameListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View dialogView = inflater.inflate(
                R.layout.new_ribbon_dialog,
                container,
                false);

        LinearLayout ribbonNameOptions =
                (LinearLayout) dialogView.findViewById(R.id.ribbon_name_options);

        String[] options = getResources().getStringArray(R.array.ribbon_name_options);
        for (final String option : options) {
            TextView ribbonNameOption =
                    (TextView) inflater.inflate(R.layout.ribbon_name_option, ribbonNameOptions, false);
            ribbonNameOption.setText(option);
            ribbonNameOption.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            endDialog(option);
                        }
                    });
            ribbonNameOptions.addView(ribbonNameOption);
        }

        EditText customRibbonName = (EditText) dialogView.findViewById(R.id.custom_ribbon_name);
        customRibbonName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_GO:
                        endDialog(v.getText().toString());
                        return true;
                    default:
                        return false;
                }
            }
        });

        return dialogView;
    }

    private void endDialog(String text) {
        if (listener != null) {
            listener.onRibbonNameReceived(text);
        }
        dismiss();
    }
}
