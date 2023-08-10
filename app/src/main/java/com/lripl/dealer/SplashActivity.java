package com.lripl.dealer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lripl.customviews.TypeWriterTextView;
import com.lripl.database.AppDatabase;
import com.lripl.database.AppExecutors;

public class SplashActivity extends AppCompatActivity implements TypeWriterTextView.TextTypeWriteComplete {



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
       setContentView(R.layout.splash_layout);

        TypeWriterTextView typeWriterTextView = findViewById(R.id.txt_india_num_one);
        typeWriterTextView.setTypeWriteListener(this);
        typeWriterTextView.setText("");
        typeWriterTextView.setCharacterDelay(120);
        typeWriterTextView.displayTextWithAnimation(getString(R.string.txt_india_num_one));

    }


    @Override
    public void onTypeComplete() {
        findViewById(R.id.txt_remote_control).setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            final LiveData<Integer> isUserExists = AppDatabase.getInstance(SplashActivity.this.getApplicationContext()).userDao().getUserCount();
            //final LiveData<List<States>> stateList = AppDatabase.getInstance(SplashActivity.this.getApplicationContext()).statesDao().getAllStates();
            isUserExists.observe(SplashActivity.this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
                    Log.i(SplashActivity.class.getName(),"----------User Count-------"+ integer+" : "+isUserExists.getValue());
                    Intent i = new Intent(SplashActivity.this, (integer > 0) ? ItemsTypeListActivity.class : LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            },500);
    }
}
