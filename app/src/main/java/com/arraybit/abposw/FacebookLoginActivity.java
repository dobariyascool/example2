package com.arraybit.abposw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookLoginActivity extends AppCompatActivity {

    Button login_button, btnLogOut;
    CallbackManager callbackManager;
    LinearLayout informationLayout;
    ImageView ivPhoto;
    TextView txtName, txtEmail, txtBirth, txtLocation, txtGender, txtAboutMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_facebook_login);

        login_button = (Button) findViewById(R.id.login_button);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        informationLayout = (LinearLayout) findViewById(R.id.informationLayout);
        SetVisibility(false);

        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtBirth = (TextView) findViewById(R.id.txtBirth);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtAboutMe = (TextView) findViewById(R.id.txtAboutMe);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        final LoginButton loginButton = new LoginButton(this);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.setReadPermissions("public_profile", "email", "user_birthday", "user_location", "user_friends");
                loginButton.performClick();
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        SetVisibility(true);
                        //get basic information
                        //Profile objProfile = Profile.getCurrentProfile();
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        // Application code
                                        try {
                                            if (object.getString("name") != null && !object.getString("name").equals("")) {
                                                txtName.setText(object.getString("name"));
                                            }
                                            if (object.getString("email") != null && !object.getString("email").equals("")) {
                                                txtEmail.setText(object.getString("email"));
                                            }
                                            if (object.getString("birthday") != null && !object.getString("birthday").equals("")) {
                                                txtBirth.setText(object.getString("birthday"));
                                            }
                                            if (object.getString("gender") != null && !object.getString("gender").equals("")) {
                                                txtGender.setText(object.getString("gender"));
                                            }
                                            if (object.getString("id") != null && !object.getString("id").equals("")) {
                                                Picasso.with(FacebookLoginActivity.this)
                                                        .load("https://graph.facebook.com/" + object.getString("id") + "/picture?type=large")
                                                        .into(ivPhoto);
                                            }
                                            JSONObject jsonObject = object.getJSONObject("location");
                                            if(jsonObject!=null) {
                                                if (jsonObject.getString("name") != null && !jsonObject.getString("name").equals("")) {
                                                    txtLocation.setText(jsonObject.getString("name"));
                                                }
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(FacebookLoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "name,email,birthday,gender,id,location");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(FacebookLoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(FacebookLoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                SetVisibility(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void SetVisibility(boolean isUpdate) {
        if (isUpdate) {
            informationLayout.setVisibility(View.VISIBLE);
            login_button.setVisibility(View.GONE);
        } else {
            informationLayout.setVisibility(View.GONE);
            login_button.setVisibility(View.VISIBLE);
        }
    }

}
