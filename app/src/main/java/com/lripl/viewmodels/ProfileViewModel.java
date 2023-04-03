package com.lripl.viewmodels;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.lripl.customviews.spinner.SelectorItemClickListener;
import com.lripl.customviews.spinner.SpinnerSelectorFragment;
import com.lripl.database.AppDatabase;
import com.lripl.database.AppExecutors;
import com.lripl.database.SharedPrefsHelper;
import com.lripl.dealer.ItemsTypeListActivity;
import com.lripl.dealer.MyApplication;
import com.lripl.dealer.ProfileActivity;
import com.lripl.dealer.R;
import com.lripl.entities.States;
import com.lripl.entities.Users;
import com.lripl.entities.Zones;
import com.lripl.network.RestApiClient;
import com.lripl.network.RestApiService;
import com.lripl.network.RetryWithDelay;
import com.lripl.utils.Constants;
import com.lripl.utils.PicassoCircleTransformation;
import com.lripl.utils.Utils;
import com.mvc.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

public class ProfileViewModel extends BaseViewModel implements BaseViewInterface.ProfilePresenterInterface {

    public MutableLiveData<String> fullName = new MutableLiveData<>();
    public MutableLiveData<String> emailId = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> companyName = new MutableLiveData<>();
    public MutableLiveData<String> gstNumber = new MutableLiveData<>();
    //public MutableLiveData<String> state = new MutableLiveData<>();
    private LiveData<List<States>> stateList = new MutableLiveData<>();
    private LiveData<Users> usersLiveData = new MutableLiveData<>();
    int selectedValue = 0;
    private String state_id;
    ProfileActivity activity;
    AppDatabase appDatabase;
    Users user;

    boolean isValidGSTIN = true;

    ProfileViewModel(AppCompatActivity appCompatActivity) {
        this.activity = (ProfileActivity) appCompatActivity;
        this.appDatabase = AppDatabase.getInstance(activity.getApplicationContext());
        stateList = appDatabase.statesDao().getAllStates();
        //fillStates();
        if (activity.getIntent().hasExtra(Constants.PARSE_USER_OBJ)) {
            this.user = (Users) activity.getIntent().getSerializableExtra(Constants.PARSE_USER_OBJ);
            Log.i(ProfileViewModel.class.getName(), "-------User Data---" + user.user_id);
            if (!TextUtils.isEmpty(user.phonenumber) && user.phonenumber.contains("+91")) {
                phoneNumber.setValue(user.phonenumber.substring(3));
            } else {
                phoneNumber.setValue(user.phonenumber);
            }
            fullName.setValue("");
        } else {
            fillData();
            addEditTextTextWatcher();
        }
    }

    private void fillData() {
        usersLiveData = appDatabase.userDao().getUserEntity();

        usersLiveData.observe(activity, new Observer<Users>() {
            @Override
            public void onChanged(@Nullable Users users) {
                ProfileViewModel.this.user = users;
                if (users != null) {
                    fullName.setValue(users.fullname);
                    emailId.setValue(users.emailid);
                    phoneNumber.setValue(users.phonenumber);
                    gstNumber.setValue(users.gstnumber);
                    companyName.setValue(users.companyname);
                    if (!TextUtils.isEmpty(users.phonenumber) && users.phonenumber.contains("+91")) {
                        phoneNumber.setValue(users.phonenumber.substring(3));
                    } else {
                        phoneNumber.setValue(users.phonenumber);
                    }
                    Log.i("ProfileViewModel", "Image " + RestApiClient.PROFILE_IMAGES_PATH + users.profilepicurl);
                    if (!Utils.NullChecker(users.profilepicurl).isEmpty()) {
                        Log.i("ProfileViewModel", "Image inside");
                        Picasso.get().load(RestApiClient.PROFILE_IMAGES_PATH + users.profilepicurl).placeholder(R.mipmap.image_unknown).error(R.mipmap.image_unknown).transform(new PicassoCircleTransformation())
                                .into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        Log.i("ProfileViewModel", "Image downloaded");
                                        activity.bitmap = bitmap;
                                        activity.profileLayoutBinding.profileImage.setImageBitmap(bitmap);
                                    }

                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                        Log.i("ProfileViewModel", "Image downloaded failed");
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        Log.i("ProfileViewModel", "Image onPrepareLoad");
                                    }
                                });
                    }
                    setUserState(users.state_id);
                } else {
                    fullName.setValue("");
                }
            }
        });
    }

    @Override
    public void showProgress() {
        activity.progress_wheel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        activity.progress_wheel.setVisibility(View.GONE);
    }

    private String getValues() {
        try {
            if (user != null) {
                user.fullname = fullName.getValue();
                user.emailid = emailId.getValue();
                user.companyname = companyName.getValue();
                user.phonenumber = phoneNumber.getValue();
                user.gstnumber = gstNumber.getValue();
                user.state_id = state_id;
                if (activity.bitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    activity.bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    user.profilepicurl = Base64.encodeToString(byteArray, Base64.DEFAULT);

                }

                return new Gson().toJson(user);
            }
        } catch (Exception e) {

        }

        return null;
    }


    @Override
    public void diplayError(String error) {
        Utils.showAlertDialog(activity, error);
    }

    public void onSelectItem(AdapterView<?> parent, View view, int pos, long id) {
        //pos                                 get selected item position
        //view.getText()                      get lable of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getAdapter().getCount()      get item count
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item
        //and other...
        //state.setValue(statesList.get());
        //Log.i(ProfileActivity.class.getName(),"---------onSelectItem--------"+pos+" : "+stateList.getValue());
        /*if(stateList.getValue() != null && stateList.getValue().size() > 0) {
            Log.i(ProfileActivity.class.getName(),"---------onSelectItem--------"+pos+" : "+stateList.getValue());
            state_id =stateList.getValue().get(pos).state_id;

            //state.setValue(stateList.getValue().get(pos).state_id);
        }*/
        state_id = ((States) parent.getAdapter().getItem(pos)).state_id;
        Log.i(ProfileActivity.class.getName(), "---------onSelectItem--------" + pos + " : " + stateList.getValue() + " : " + state_id);
    }

    @Override
    public void saveUserRequest(String body) {
        showProgress();
        getObservable(body).retryWhen(new RetryWithDelay(Constants.MAX_RETRIES)).subscribeWith(getObserver());
    }

    @Override
    public void saveUserResponse(Users user) {
        if (user != null && !Utils.NullChecker(user.phonenumber).isEmpty()) {
            insertUser(user);
            Log.i(ProfileViewModel.class.getName(), "--------Save User-------" + user.user_id);
        } else {
            diplayError("Save profile failed. Please try again.");
        }
    }

    private void insertUser(final Users user) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.userDao().insert(user);
                Intent i = new Intent(activity, ItemsTypeListActivity.class);
                activity.startActivity(i);
                activity.finish();
            }
        });
    }

    @Override
    public void loadZonesRequest() {
        getZonesObservable().retryWhen(new RetryWithDelay(Constants.MAX_RETRIES)).subscribeWith(getZonesObserver());
    }

    @Override
    public void loadZonesResponse(List<Zones> zonesList) {
        Log.i(ProfileViewModel.class.getName(), "----------loadZonesResponse-------" + zonesList.size());
        if (zonesList != null && zonesList.size() > 0) {
            inserZonesAndStates(zonesList);
        } else {
            Utils.showAlertDialog(activity, "Zones loading failed. Please try again.");
        }
    }


    private void inserZonesAndStates(final List<Zones> zonesList) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (Zones zones : zonesList) {
                    appDatabase.zonesDao().saveZone(zones);
                    Log.i(ProfileViewModel.class.getName(), "----------States Size-------" + zones.statesList.size());
                    appDatabase.statesDao().saveStates(zones.statesList);
                }
                stateList = appDatabase.statesDao().getAllStates();
            }
        });

        //activity.spinnerAdapter.notifyDataSetChanged();
    }


    private Observable<Response<Users>> getObservable(String body) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        return RestApiClient.getRetrofit().create(RestApiService.class).profile(SharedPrefsHelper.getInstanse(activity)
                .get(Constants.USER_AUTH_TOKEN, ""), requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<Response<Users>> getObserver() {
        return new DisposableObserver<Response<Users>>() {
            @Override
            public void onNext(Response<Users> baseResponseResponse) {
                if (baseResponseResponse.code() == Constants.HTTP_OK) {
                    saveUserResponse(baseResponseResponse.body());
                } else {
                    diplayError(Constants.PROFILE_ERROR_MSG);
                }
            }

            @Override
            public void onError(Throwable e) {
                diplayError(e.getLocalizedMessage());
                hideProgress();
            }

            @Override
            public void onComplete() {
                hideProgress();
            }
        };
    }

    private Observable<Response<List<Zones>>> getZonesObservable() {
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),body);
        return RestApiClient.getRetrofit().create(RestApiService.class).getzones(SharedPrefsHelper.getInstanse(activity)
                .get(Constants.USER_AUTH_TOKEN, "")).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<Response<List<Zones>>> getZonesObserver() {
        return new DisposableObserver<Response<List<Zones>>>() {
            @Override
            public void onNext(Response<List<Zones>> baseResponseResponse) {
                if (baseResponseResponse.code() == Constants.HTTP_OK) {
                    loadZonesResponse(baseResponseResponse.body());
                } else {
                    diplayError(baseResponseResponse.errorBody().toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                diplayError(e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private void addEditTextTextWatcher() {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (activity.profileLayoutBinding.edtPhoneNumber.getText().hashCode() == charSequence.hashCode()) {
                    if (activity.profileLayoutBinding.edtPhoneNumber.getText().toString().length() == 10) {
                        activity.profileLayoutBinding.txtMobileErrorMsg.setVisibility(View.GONE);
                    } else {
                        activity.profileLayoutBinding.txtMobileErrorMsg.setVisibility(View.VISIBLE);
                    }
                }
                setButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        activity.profileLayoutBinding.edtCompanyName.addTextChangedListener(textWatcher);
        activity.profileLayoutBinding.edtEmail.addTextChangedListener(textWatcher);
        activity.profileLayoutBinding.edtName.addTextChangedListener(textWatcher);
        activity.profileLayoutBinding.edtPhoneNumber.addTextChangedListener(textWatcher);
        activity.profileLayoutBinding.edtGstNumber.addTextChangedListener(textWatcher);


    }

    private void setButtonState() {

        boolean isButtonEnabled = activity.profileLayoutBinding.edtPhoneNumber.getText().toString().length() == 10;
        activity.txtSave.setEnabled(isButtonEnabled);
        activity.txtSave.setTextColor(isButtonEnabled ? activity.getResources().getColor(R.color.white) : activity.getResources().getColor(R.color.warm_grey));
        if (isButtonEnabled) {
            activity.txtSave.setTypeface(activity.txtSave.getTypeface(), Typeface.BOLD);
        } else {
            activity.txtSave.setTypeface(activity.txtSave.getTypeface(), Typeface.NORMAL);
        }
    }


    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(Constants.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void onSaveClick(View view) {
        Utils.hideSoftKeyboard(activity);
        saveUserRequest(getValues());
    }

    private void fillStates() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                stateList = appDatabase.statesDao().getAllStates();
                stateList.observe(activity, new Observer<List<States>>() {
                    @Override
                    public void onChanged(@Nullable List<States> states) {
                        Log.i(ProfileViewModel.class.getName(), "-------Before loadZonesRequest---" + stateList.getValue());
                        //activity.spinnerAdapter.notifyDataSetChanged();
                        if (states.size() == 0) {
                            loadZonesRequest();
                        }
                    }
                });

            }
        });
    }

    public void onChangePhotoClick() {
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            MyApplication.requestWriteExternalStoragePermission(activity);
        } else if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)) {
            MyApplication.requestCameraPermission(activity);
        } else {
            ImagePicker.pickImage(activity, "Select an image");
        }
    }

    public void onStateSpinnerClick() {

        final SpinnerSelectorFragment fragment = new SpinnerSelectorFragment();
        fragment.setDialogData(activity.statesList, "Select your state");
        fragment.setSelectedValue(selectedValue);
        fragment.setSelectorItemClickListener(new SelectorItemClickListener() {
            @Override
            public void itemClicked(int position) {
                fragment.dismiss();
                Log.i("Selected state ", activity.statesList.get(position).name);
                state_id = activity.statesList.get(position).state_id;
                selectedValue = position;
                activity.profileLayoutBinding.profileStateDropDown.setText(activity.statesList.get(position).name);
                activity.profileLayoutBinding.profileStateDropDown.setTextColor(activity.getResources().getColor(R.color.black));
            }

            @Override
            public void defaultSelection(int position) {

            }
        });


        fragment.show(activity.getSupportFragmentManager(), "state spinner");


    }

    public LiveData<List<States>> getStateListLiveData() {
        return stateList;
    }

    private void setUserState(final String userState) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                stateList = appDatabase.statesDao().getAllStates();
                stateList.observe(activity, new Observer<List<States>>() {
                    @Override
                    public void onChanged(@Nullable List<States> stateList) {
                        Log.i(ProfileViewModel.class.getName(), "-----------stateList---------" + stateList);
                        if (stateList != null) {
                            for (int i = 0; i < stateList.size(); i++) {
                                if (Utils.NullChecker(userState).equalsIgnoreCase(stateList.get(i).state_id)) {
                                    selectedValue = i;
                                    state_id = stateList.get(i).state_id;
                                    activity.profileLayoutBinding.profileStateDropDown.setText(stateList.get(i).name);
                                    activity.profileLayoutBinding.profileStateDropDown.setTextColor(activity.getResources().getColor(R.color.black));
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        });
    }

}
