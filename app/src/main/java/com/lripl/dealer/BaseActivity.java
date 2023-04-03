package com.lripl.dealer;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lripl.database.AppDatabase;
import com.lripl.database.AppExecutors;
import com.lripl.database.SharedPrefsHelper;
import com.lripl.entities.Products;
import com.lripl.entities.Users;
import com.lripl.network.RestApiClient;
import com.lripl.utils.Constants;
import com.lripl.utils.PicassoCircleTransformation;
import com.lripl.utils.Utils;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.internal.Util;

public abstract class BaseActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {


    public FrameLayout lnr_main_layout;
    public View view;
    public LayoutInflater inflater;
    public DrawerLayout drawer;
    public NavigationView navigationView;
    public TextView txt_full_name;
    public TextView txt_emailid;
    public TextView txt_appversion;
    public TextView txt_lripl_phone;
    public ImageView imageView;
    public ProgressWheel progress_wheel;
    public LiveData<Users> user = new MutableLiveData<>();
    public String user_id;
    public TextView cart_badge;
    protected TextView txtTitle;
    protected TextView iconMenu;
    protected TextView iconBack;
    protected TextView iconNotification;
    public TextView txtSave;
    public ImageView iconCart;
    public FrameLayout cartLayout;
    public MutableLiveData<List<Products>> enquiry_livedata = new MutableLiveData<>();

    protected Typeface hindiTypeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //ButterKnife.bind(this);

        lnr_main_layout = findViewById(R.id.lnr_main_layout);
        txtTitle = findViewById(R.id.txt_app_title);
        iconMenu = findViewById(R.id.icon_menu);
        iconBack = findViewById(R.id.icon_back);
        txtSave = findViewById(R.id.txt_profile_save);
        iconCart = findViewById(R.id.icon_cart);
        cartLayout = findViewById(R.id.cart_layout);
        iconNotification = findViewById(R.id.icon_notification);
        progress_wheel = findViewById(R.id.progress_wheel);
        cart_badge = findViewById(R.id.txt_cart_number);

        enquiry_livedata.setValue(Utils.product_enquiry);


        inflater = getLayoutInflater();


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txt_full_name = headerView.findViewById(R.id.txt_full_name);
        txt_emailid = headerView.findViewById(R.id.txt_email_id);
        imageView = headerView.findViewById(R.id.nav_header_profile_image);
        txt_appversion = navigationView.findViewById(R.id.txt_appVersion);
        txt_lripl_phone = navigationView.findViewById(R.id.txt_lripl_phone_num);
        txt_appversion.setText("v"+BuildConfig.VERSION_NAME);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                user = AppDatabase.getInstance(getApplicationContext()).userDao().getUserEntity();
                user.observe(BaseActivity.this, new Observer<Users>() {
                    @Override
                    public void onChanged(@Nullable Users users) {
                        if(users != null) {
                            user_id = users.user_id;
                            txt_full_name.setText(users.fullname);
                            txt_emailid.setText(users.emailid);
                            Picasso.get().load(RestApiClient.PROFILE_IMAGES_PATH + users.profilepicurl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.mipmap.image_unknown).error(R.mipmap.image_unknown).transform(new PicassoCircleTransformation()).into(imageView);
                        }
                    }
                });
            }
        });

        //enquiry_livedata.setValue(Utils.product_enquiry);
        enquiry_livedata.observe(this, new Observer<List<Products>>() {
            @Override
            public void onChanged(@Nullable List<Products> productsList) {
                Log.i(ProductListActivity.class.getName(),"-----productsList--observe----"+productsList.size()+" : "+cart_badge);
                if(productsList.size() > 0 && cart_badge != null){
                    cart_badge.setText(productsList.size()+"");
                    cart_badge.setVisibility(View.VISIBLE);
                }else if (productsList.size() == 0 && cart_badge != null){
                    cart_badge.setVisibility(View.INVISIBLE);

                }
            }
        });

        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);

                }
            }
        });

        cartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(BaseActivity.this, CartListActivity.class);
                    startActivity(i);
            }
        });

        txt_lripl_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri u = Uri.parse(getString(R.string.txt_phone_call_tel) + getString(R.string.txt_lripl_phone_num));
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                startActivity(i);
            }
        });

    }


    @Override
    protected void onResume(){
        super.onResume();
        enquiry_livedata.postValue(Utils.product_enquiry);
    }
   /* public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .appComponent(MyApplication.get(this).getAppComponent())
                    .build();
        }
        return activityComponent;
    }*/

    public abstract void setContentLayout(int id);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_cart);
        View main_view = item.getActionView();
        cart_badge = main_view.findViewById(R.id.txt_cart_number);
        enquiry_livedata.setValue(Utils.product_enquiry);
        main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utils.product_enquiry.isEmpty()){
                    Intent i = new Intent(BaseActivity.this, CartListActivity.class);
                    startActivity(i);
                }else{
                    Utils.showAlertDialog(BaseActivity.this,"Please select the products.");
                }

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        item.setChecked(true);
        //noinspection SimplifiableIfStatement
        Log.i(BaseActivity.this.getLocalClassName(),"--------onOptionsItemSelected----"+(id == R.id.action_cart));
        if (id == R.id.action_cart) {
            Intent i = new Intent(BaseActivity.this, CartListActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayLogoutAlert(final Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();
        // Setting Dialog Title
        alertDialog.setTitle(context.getResources().getString(R.string.app_name));
        // Setting Dialog Message
        alertDialog.setMessage(context.getString(R.string.logout_msg));

        //this will not allow user to click outside of dialog and cancel the alert
        alertDialog.setCanceledOnTouchOutside(false);
        // Setting OK Button
        alertDialog.setButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                //Toast.makeText(context, "You clicked on OK", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        alertDialog.setButton2(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                //Toast.makeText(context, "You clicked on OK", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(context).clearAllTables();
                        SharedPrefsHelper.getInstanse(context).deleteSavedData(Constants.USER_AUTH_TOKEN);
                        Intent i = new Intent(context, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                        ((AppCompatActivity) context).finish();
                    }
                });

            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
}
