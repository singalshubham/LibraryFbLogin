package com.ranosys.facebookloginsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ranosys.fbLogin.FacebookConstants;
import com.ranosys.fbLogin.FbLogin;

import org.json.JSONException;
import org.json.JSONObject;

public class SampleActivity extends AppCompatActivity {

    private static final String TAG ="SampleActivity" ;
    private final int FACEBOOK_LOGIN_REQUEST = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
    }

    //this function is called when press login with facebook button
    public void facebookLogin(View v) {
        Intent facebook = new Intent(SampleActivity.this, FbLogin.class);
        facebook.putExtra(FacebookConstants.FB_APPLICATION_ID,"399086980452410");
        startActivityForResult(facebook, FACEBOOK_LOGIN_REQUEST);
    }

    //this function is called when press logout with facebook button
    public void facebookLogout(View v) {
        FbLogin.logoutFromFacebook(SampleActivity.this);
        Toast.makeText(this, "logout from facebook", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if request for facebook login
        if (requestCode == FACEBOOK_LOGIN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                String graphResponseError = data.getStringExtra(FacebookConstants.GRAPH_RESPONSE_ERROR);
                if (TextUtils.isEmpty(graphResponseError)) {
                    String jsonObjectString = data.getStringExtra(FacebookConstants.FACEBOOK_JSON_OBJECT);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(jsonObjectString);
                        Log.d(TAG, "Json object is " + jsonObject.toString());
                    } catch (JSONException e) {
                        Log.d(TAG, "No data is get from facebook login");
                        e.printStackTrace();
                    }
                    try {
                        String email = null;
                        String fId = null;
                        String name = null;
                        String gender = null;
                        String bithday = null;
                        String firstName = null;
                        String lastName = null;

                        assert jsonObject != null;
                        if (jsonObject.has("email")) {
                            email = jsonObject.getString("email");
                        }
                        if (jsonObject.has(("id"))) {
                            fId = jsonObject.getString("id");
                        }
                        if (jsonObject.has("name")) {
                            name = jsonObject.getString("name");
                        }
                        if (jsonObject.has("gender")) {
                            gender = jsonObject.getString("gender");
                        }
                        if (jsonObject.has("birthday")) {
                            bithday = jsonObject.getString("birthday");
                        }
                        if (jsonObject.has("first_name")) {
                            firstName = jsonObject.getString("first_name");
                        }
                        if (jsonObject.has("last_name")) {
                            lastName = jsonObject.getString("last_name");
                        }

                        Log.d(TAG, "json object from facebook" + jsonObject.toString());
                        Log.d(TAG, "name is  " + name + "  gender is  " + gender + " birthday " + bithday
                                + " first name is  " + firstName + " last name is  " + lastName);
                        //here email can be null
                        if (!TextUtils.isEmpty(email)) {
                            Log.d(TAG, "email is  " + email);
                        } else
                            Log.d(TAG, "email is  null");
                    } catch (JSONException | NullPointerException e) {
                        //email is null if user does not give permission to access his/her email
                        //or in some other cases email can also null
                        Log.d(TAG, "Exception is  " + e);
                    }
                } else {
                    Log.d(TAG, "error is come during login response" + graphResponseError);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "cancel by user");
            } else if (resultCode == FacebookConstants.RESULT_ERROR) {
                Log.d(TAG, "error is occoured" + data.getStringExtra(FacebookConstants.FACEBOOK_LOGIN_ERROR));
            }
        }
    }
}
