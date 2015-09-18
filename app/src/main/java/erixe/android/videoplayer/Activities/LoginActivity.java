package erixe.android.videoplayer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import erixe.android.videoplayer.EWebServices.GetIndexVideosTask;
import erixe.android.videoplayer.R;
import erixe.android.videoplayer.Utilities.EVideoView;

public class LoginActivity extends Activity {

    pageViewHolder vh = new pageViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setViewHolder();
    }

    public void setViewHolder()
    {
        vh.loginUsername = (EditText) findViewById(R.id.loginUsername);
        vh.loginPassword = (EditText) findViewById(R.id.loginPassword);
        vh.loginLogin = (Button) findViewById(R.id.loginLogin);
        setPageListeners();
    }

    public void setPageListeners()
    {
        vh.loginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("username", vh.loginUsername.getText().toString());
                intent.putExtra("password", vh.loginPassword.getText().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }

    public static class pageViewHolder {
        EditText loginUsername;
        EditText loginPassword;
        Button loginLogin;
    }

}