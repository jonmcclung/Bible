package com.lerenard.bible;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by mc on 07-Jan-17.
 */

public class EditRibbonNameDialog extends DialogFragment {
    private static final String TAG = "ERNDialog_";
    private EditRibbonNameListener listener;
    private Ribbon ribbon;
    private int position;

    public void setListener(EditRibbonNameListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ReadingActivity.RIBBON_KEY, ribbon);
        outState.putInt(RibbonActivity.INDEX_KEY, position);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        restoreArguments(args);
    }

    private void restoreArguments(Bundle args) {
        ribbon = args.getParcelable(ReadingActivity.RIBBON_KEY);
        position = args.getInt(RibbonActivity.INDEX_KEY);
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        if (savedInstanceState != null) {
            restoreArguments(savedInstanceState);
        }

        View dialogView =
                getActivity().getLayoutInflater().inflate(R.layout.edit_ribbon_name_dialog_layout, null);
        final EditText editText =
                (EditText) dialogView.findViewById(R.id.edit_ribbon_name_dialog_edit_text);
        editText.setText(ribbon.getName());
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_GO:
                        finish(editText.getText().toString());
                        return true;
                }
                return false;
            }
        });
        return new AlertDialog.Builder(getContext()).setView(dialogView).create();
    }

    private void finish(String newName) {
        if (listener != null) {
            listener.onEditedNameReceived(newName, position);
        }
        dismiss();
    }

    interface EditRibbonNameListener {
        void onEditedNameReceived(String newName, int position);
    }
}
