package com.ranosys.fbLogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * This is class for facebook login and it use the facebook-android-sdk:4.0.0'
 * @Author Ranosys Technologies
 */
public class FbLogin extends AppCompatActivity {

    private static final String TAG = "FbLogin";
    //callback manager is responsible for passing result from onActivityResult to further
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inialize the facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getIntent().getStringExtra(FacebookConstants.FB_APPLICATION_ID));
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_facebook_integration);
        //Inialize the callbackManger
        mCallbackManager = CallbackManager.Factory.create();
        //registering login callback
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            //after successfull login json object is pass to the calling activity in RESULT_OK result code
            //json object contain the field fbId,name,email,gender,birthday,first_name,last_name
            @Override
            public void onSuccess(LoginResult loginResult) {

                //after successfull login json object is pass to the calling activity in RESULT_OK result code
                //json object contain the field fbId,name,email,gender,birthday,first_name,last_name
                //graph request for getting email address of user
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d(TAG, "Graph response is " + response.toString());
                                Log.d(TAG, "json object is  " + object.toString());

                                //after successfull login if no error obtain json object is pass to the calling activity in RESULT_OK result code
                                //json object contain the field fbId,name,email,gender,birthday,first_name,last_name
                                Intent returnIntent = new Intent();
                                if(response.getError()==null)
                                returnIntent.putExtra(FacebookConstants.FACEBOOK_JSON_OBJECT, object.toString());
                                else
                                //if after successfull login error is come during getting user data then it pass to the calling activity
                                returnIntent.putExtra(FacebookConstants.GRAPH_RESPONSE_ERROR,response.getError().toString());
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        });
                Bundle parameters = new Bundle();
                //these fields get in json object we can customize these fields
                parameters.putString("fields", "id,name,email,gender,birthday,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

            }

            //when user cancel the fbLogin then Activity.RESULT_CANCELEd is thrown to calling activity
            @Override
            public void onCancel() {
                Log.d(TAG, "cancel by user");
                //Intent is passing to the calling activity when user cancel login
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }

            //when error is occour during fb login then error is thrown to the calling activity
            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "error is occoured" + error.toString());
                //Intent is passing to the calling activity when error occour when connecting with facebook
                //and passing error with intent
                Intent returnIntent = new Intent();
                returnIntent.putExtra(FacebookConstants.FACEBOOK_LOGIN_ERROR, error.toString());
                setResult(FacebookConstants.RESULT_ERROR, returnIntent);
                finish();
            }
        });

        //start login to facebook with  read permissioin public profile ,email,user_birthday
        //here we customize read permission that we need for login
        LoginManager.getInstance().logInWithReadPermissions(
                FbLogin.this,
                Arrays.asList("public_profile", "email", "user_birthday", "user_friends")
        );

    }

    //this method is for facebook login to send data one activity to another
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        mCallbackManager.onActivityResult(requestCode, responseCode, intent);

    }

    /**
     * This is method use for logout from facebook
     *
     * @param context context of activity or application is passed
     */
    public static void logoutFromFacebook(Context context) {
        FacebookSdk.sdkInitialize(context);
        LoginManager.getInstance().logOut();
        Log.d(TAG, "logout from facebook");
    }
}
