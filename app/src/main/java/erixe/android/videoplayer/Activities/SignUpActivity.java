package erixe.android.videoplayer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

import erixe.android.videoplayer.R;

public class SignUpActivity extends Activity {

    pageViewHolder vh = new pageViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setViewHolders();
    }

    public void setViewHolders()
    {
        vh.signUpUsername = (EditText) findViewById(R.id.signUpUsername);
        vh.signUpEMail = (EditText) findViewById(R.id.signUpEMail);
        vh.signUpPassword = (EditText) findViewById(R.id.signUpPassword);
        vh.signUpConfirmPassword = (EditText) findViewById(R.id.signUpConfirmPassword);
        vh.signUpRegister = (Button) findViewById(R.id.signUpRegister);
        setPageListeners();
    }

    public void setPageListeners()
    {
        vh.signUpUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(vh.signUpUsername.getText().toString().equals(""))
                    vh.signUpUsername.setBackgroundColor(getResources().getColor(R.color.e_red));
                else
                    vh.signUpUsername.setBackgroundColor(getResources().getColor(R.color.e_white));
            }
        });

        vh.signUpEMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(vh.signUpEMail.getText().toString()).matches()
                        || vh.signUpEMail.getText().toString().equals(""))
                    vh.signUpEMail.setBackgroundColor(getResources().getColor(R.color.e_red));
                else
                    vh.signUpEMail.setBackgroundColor(getResources().getColor(R.color.e_white));
            }
        });

        vh.signUpPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(vh.signUpPassword.getText().toString().equals(""))
                    vh.signUpPassword.setBackgroundColor(getResources().getColor(R.color.e_red));
                else
                    vh.signUpPassword.setBackgroundColor(getResources().getColor(R.color.e_white));
            }
        });

        vh.signUpConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!vh.signUpConfirmPassword.getText().toString().equals(vh.signUpPassword.getText().toString()))
                    vh.signUpConfirmPassword.setBackgroundColor(getResources().getColor(R.color.e_red));
                else
                    vh.signUpConfirmPassword.setBackgroundColor(getResources().getColor(R.color.e_white));
            }
        });

        vh.signUpRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public static class pageViewHolder {
        EditText signUpUsername;
        EditText signUpEMail;
        EditText signUpPassword;
        EditText signUpConfirmPassword;
        Button signUpRegister;
    }

}