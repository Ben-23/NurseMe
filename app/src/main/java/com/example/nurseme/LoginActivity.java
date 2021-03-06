package com.example.nurseme;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText username,password;
    Button loginbtn;
    TextView invalidtxt;
    ProgressBar pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.username_txtbox);
        password=findViewById(R.id.password_txtbox);
        loginbtn=findViewById(R.id.login_btn);
        pg=findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
        invalidtxt=findViewById(R.id.invalid_txtview);
        }

        public void forgotpassword(View v)
        {String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            String usernamestr= String.valueOf(username.getText()).trim();
            if (usernamestr.matches(emailPattern) && usernamestr.length() > 0) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(usernamestr)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Log.d(TAG, "Email sent.");
                                Toast.makeText(LoginActivity.this, "A reset passwordlink is sent to the entered Email_ID", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
            else
            {
                Toast.makeText(this, "Enter a valid Email_ID", Toast.LENGTH_SHORT).show();
            }
        }

    public void login(View view)
    {
        loginbtn.setVisibility(View.GONE);
        pg.setVisibility(View.VISIBLE);
        final String usernamestr,passwordstr;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        passwordstr=password.getText().toString().trim();
        usernamestr= String.valueOf(username.getText()).trim();
        if(usernamestr.equals("admin"))
        {

        }
        if (usernamestr.matches(emailPattern)||usernamestr.equals("admin") && usernamestr.length() > 0){

            if(passwordstr.length()>2)
            {
                if(usernamestr.equals("admin"))

                login("bba@admin.com",passwordstr);
                else
                    login(usernamestr,passwordstr);
            }
            else
            {pg.setVisibility(View.GONE);
            loginbtn.setVisibility(View.VISIBLE);
                password.setError("Password Mismatch!");
                password.requestFocus();
            }

        }else
        {pg.setVisibility(View.GONE);
            loginbtn.setVisibility(View.VISIBLE);
            username.setError("Invalid Email!");
            username.requestFocus();
        }
    }
    public void login(final String usernamestr, String passwordstr)
    {

        mAuth.signInWithEmailAndPassword(usernamestr, passwordstr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pg.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Successfully Signed In", Toast.LENGTH_SHORT).show();
                            if(usernamestr.contains("nurse"))
                            {
                                startActivity(new Intent(LoginActivity.this,NurseDashboard.class));
                            }
                            if(usernamestr.contains("admin")){
                                startActivity(new Intent(LoginActivity.this,AdminDashboard.class));
                            }
                            else
                                startActivity(new Intent(LoginActivity.this,RelativeDashboard.class));
                        } else {
                            if(task.getException() instanceof FirebaseAuthInvalidUserException)
                            {                    Toast.makeText(LoginActivity.this, "Oops! You are not a registered user...\n you will be redirected to sign up page in 5 seconds!!", Toast.LENGTH_SHORT).show();
                                new Timer().schedule(new TimerTask(){
                                    public void run() {

                                        Intent i=new Intent(LoginActivity.this,SignupActivity.class);
                                        i.putExtra("email",usernamestr);
                                        i.putExtra("type","relative");
                                        startActivity(i);
                                    }
                                }, 5000);
                            }
                            else if(task.getException() instanceof FirebaseAuthWeakPasswordException){
                                {loginbtn.setVisibility(View.VISIBLE);
                                    Toast.makeText(LoginActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                                    password.setError("Incorrect Password!");
                                    password.setText("");
                                    password.requestFocus();
                                }}
                            else
                            {
                                loginbtn.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, "Some Error occured! Please try again!!", Toast.LENGTH_SHORT).show();
                            }
                            invalidtxt.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        System.exit(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginbtn.setVisibility(View.VISIBLE);
        pg.setVisibility(View.GONE);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (mAuth.getCurrentUser() != null) {
            finish();
            if(mAuth.getCurrentUser().getEmail().contains("nurse"))
            {
                startActivity(new Intent(this,NurseDashboard.class));
            }
            else if(mAuth.getCurrentUser().getEmail().contains("admin"))
            {
                startActivity(new Intent(this,AdminDashboard.class));
            }
            else
            startActivity(new Intent(this, RelativeDashboard.class));
    }}
    public void register(View v)
    {
        Intent i =new Intent(this,SignupActivity.class);
        i.putExtra("type","relative");
        startActivity(i);
    }
}
