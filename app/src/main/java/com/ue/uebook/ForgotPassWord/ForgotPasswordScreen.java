package com.ue.uebook.ForgotPassWord;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BaseActivity;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;

import java.io.IOException;

public class ForgotPasswordScreen extends BaseActivity implements View.OnClickListener {
    private ImageButton back_btn;
    private EditText email_user;
    private Button sendPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);
        back_btn=findViewById(R.id.back_forgot);
        email_user=findViewById(R.id.email_forgot);
        sendPassword=findViewById(R.id.sendpassword);
        sendPassword.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        if (view==sendPassword){
            if (!email_user.getText().toString().isEmpty()){
                forgotPassword(email_user.getText().toString());
            }
            else {
                email_user.setEnabled(true);
                email_user.setError("Enter your Email");
                email_user.requestFocus();
            }
        }
        else if (view==back_btn){
            finish();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void forgotPassword( String email) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforforgotPassword( email, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final Response form = gson.fromJson(myResponse, Response.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (form.getError().equals("false")){
                            Toast.makeText(getApplicationContext(),"Successfuly password send",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please Enter valid Email",Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });
    }



}
