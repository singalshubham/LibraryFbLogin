# LibraryFbLogin
 
The aLibraryFbLogin use for facebook integration in android app and this library used the facebook-android-sdk:4.28.0.
* if login is successfull to facebook then json object is pass to the calling activity in RESULT_OK result code.
* json object contain the field fbId,name,email,gender,birthday,first_name,last_name.
* if an error occour after successfull login in getting the data using graph request then reaon of error is thrown in the key FacebookConstants.GRAPH_RESPONSE_ERROR in RESULT_OK result code.
* if user cancel the login with facebook then result code RESULT_CANCELED is pass to the calling activity.
* if an error occour during login with facebook then result code FacebookConstants.RESULT_ERROR is pass to calling activity with reason of error in the key FacebookConstants.FACEBOOK_LOGIN_ERROR.
           

#### Impelementation steps

 - Copy folder in the project root directory.</br>
 for example </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Project_Name/fbLogin  
- Now go in the <span style="color:green">settings.gradle</span> file and replace 

```sh
include ':app'
```
with 

```sh
include ':app',':fbLogin'
```

- Open app's <span style="color:green">build.gradle</span> file and add the <span style="color:blue">compile project <span style="color:green">(path:':logger')</span></span> line in depencies module such as : </br> 

```sh
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:25.1.0'
    compile 'com.android.support:design:25.1.0'
    compile project(path: ':fbLogin')

}
```
#### How to use this library for integrating facebook in app

#### Step 1
 - Get a facebook app id from developer account 
 - and use this app id in passing in intent as shown in sample below 
 - change in your app menifest according to this app module menifest 

#### Step 2

#### For do login with facebook :

- for facebook login using this library user call activity for result startActivityForResult(intent,request_code)
- sample for making intent of facebook login given below

#### How to make intent
	
```sh
	    Intent facebook = new Intent(SampleActivity.this, fbLogin.class);
	    facebook.putExtra(FacebookConstants.FB_APPLICATION_ID,"399086980452410");
        startActivityForResult(facebook, facebook_login_request);
```
#### Do changes in the onActivityResult for facebook login	
	
    
```sh
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
```
#### Step 3

#### For logout from facebook :
- for logout from facebook function is defined in the library
- for logout call the function as logoutFromFacebook(context);
- sample of calling of logout function is given below

```sh
    fbLogin.logoutFromFacebook(SampleActivity.this);
```    
