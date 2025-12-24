package com.villedepommes.tinysearch;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.villedepommes.tinysearch.ui.SearchDialogFragment;

public class SearchResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Extract selected text from ACTION_PROCESS_TEXT intent
        CharSequence selectedText = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);

        if (selectedText != null && selectedText.length() > 0) {
            // Show search dialog
            SearchDialogFragment dialog = SearchDialogFragment.newInstance(selectedText.toString());
            dialog.setOnDismissListener(() -> {
                // Finish activity when dialog is dismissed
                finish();
            });
            dialog.show(getSupportFragmentManager(), "search_dialog");
        } else {
            // No text selected, finish immediately
            finish();
        }
    }
}
