package com.lripl.dealer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lripl.dealer.databinding.HelpFeedbackLayoutBinding;

public class HelpActivity extends BaseActivity {

    HelpFeedbackLayoutBinding helpFeedbackLayoutBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.help_feedback_layout);
        helpFeedbackLayoutBinding.txtLriplCallUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri u = Uri.parse(getString(R.string.txt_phone_call_tel) + getString(R.string.txt_lripl_phone_num));
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                startActivity(i);
            }
        });

        helpFeedbackLayoutBinding.txtLriplSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendFeedbackEmail();
            }
        });
    }

    private void sendFeedbackEmail() {
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.txt_send_email_to)});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.txt_send_email_subject));
        emailIntent.setSelector(selectorIntent);

        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.txt_provide_feedback)));
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void setContentLayout(int id) {
        view = inflater.inflate(id,null);
        lnr_main_layout.addView(view);
        txtTitle.setText(getString(R.string.menu_contactus));
        helpFeedbackLayoutBinding = DataBindingUtil.bind(view);
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
                Intent i = new Intent(HelpActivity.this, ItemsTypeListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            case R.id.nav_enquiries:
                i = new Intent(HelpActivity.this, EnquiriesListActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_about_us:
                Intent aboutUs = new Intent(HelpActivity.this, AboutUsActivity.class);
                startActivity(aboutUs);
                break;
            case R.id.nav_profile:
                i = new Intent(HelpActivity.this, ProfileActivity.class);
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
