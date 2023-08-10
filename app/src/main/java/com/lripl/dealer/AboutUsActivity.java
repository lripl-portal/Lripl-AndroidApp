package com.lripl.dealer;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lripl.dealer.databinding.AboutUsLayoutBinding;

public class AboutUsActivity extends BaseActivity {


    AboutUsLayoutBinding aboutUsLayoutBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.about_us_layout);
        aboutUsLayoutBinding.abousWebView.setWebChromeClient(new WebChromeClient());
        aboutUsLayoutBinding.abousWebView.setWebViewClient(new AppWebViewClients());
        final WebSettings settings =  aboutUsLayoutBinding.abousWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(getCacheDir().getPath());
        aboutUsLayoutBinding.abousWebView.loadUrl("https://www.lripl.com/aboutus");
    }

    @Override
    public void onResume() {
        super.onResume();
        aboutUsLayoutBinding.abousWebView.onResume();
    }

    @Override
    public void onPause() {
        aboutUsLayoutBinding.abousWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        aboutUsLayoutBinding.abousWebView.removeAllViews();
        aboutUsLayoutBinding.abousWebView.clearCache(true);
        aboutUsLayoutBinding.abousWebView.destroy();
        super.onDestroy();
    }

    private class AppWebViewClients extends WebViewClient {

        private AppWebViewClients() {
            progress_wheel.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progress_wheel.setVisibility(View.GONE);
        }
    }

    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id,null);
        lnr_main_layout.addView(view);
        txtTitle.setText(getString(R.string.menu_aboutus));
        aboutUsLayoutBinding = DataBindingUtil.bind(view);
        iconBack.setVisibility(View.INVISIBLE);
        iconMenu.setVisibility(View.VISIBLE);
        iconNotification.setVisibility(View.INVISIBLE);
        cartLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawer.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()) {
            case R.id.nav_products:
                Intent i = new Intent(AboutUsActivity.this, ItemsTypeListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            case R.id.nav_enquiries:
                i = new Intent(AboutUsActivity.this, EnquiriesListActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_contactus:
                i = new Intent(AboutUsActivity.this, HelpActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_profile:
                i = new Intent(AboutUsActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_logout:
                displayLogoutAlert(this);
                break;
            default:
                break;
        }
        return true;
    }
}
