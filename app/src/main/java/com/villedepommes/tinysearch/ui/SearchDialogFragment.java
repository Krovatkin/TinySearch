package com.villedepommes.tinysearch.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.villedepommes.tinysearch.R;
import com.villedepommes.tinysearch.util.SearchUrlBuilder;

public class SearchDialogFragment extends DialogFragment {
    private static final String ARG_SEARCH_TEXT = "search_text";

    private WebView webView;
    private ProgressBar progressBar;
    private String searchText;
    private Runnable onDismissListener;

    public static SearchDialogFragment newInstance(String searchText) {
        SearchDialogFragment fragment = new SearchDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_TEXT, searchText);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnDismissListener(Runnable listener) {
        this.onDismissListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchText = getArguments().getString(ARG_SEARCH_TEXT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Create a simple layout programmatically
        android.widget.FrameLayout layout = new android.widget.FrameLayout(requireContext());

        // Create WebView
        webView = new WebView(requireContext());
        webView.setLayoutParams(new android.widget.FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Create ProgressBar
        progressBar = new ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new android.widget.FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                8
        ));
        progressBar.setMax(100);

        layout.addView(webView);
        layout.addView(progressBar);

        configureWebView();

        // Load search URL
        if (searchText != null && !searchText.isEmpty()) {
            String searchUrl = SearchUrlBuilder.buildSearchUrl(searchText);
            webView.loadUrl(searchUrl);
        }

        return layout;
    }

    private void configureWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        // Disable file access for security
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);

        // Set WebViewClient to handle page loading
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                progressBar.setVisibility(View.GONE);
            }
        });

        // Set WebChromeClient to handle progress updates
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            // Set dialog size to 80% x 60% of screen
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int width = (int) (displayMetrics.widthPixels * 0.8);
            int height = (int) (displayMetrics.heightPixels * 0.6);
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.run();
        }
    }

    @Override
    public void onDestroyView() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroyView();
    }
}
