package com.example.bookfirm;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookfirm.db.UserDatabaseHandler;
import com.example.bookfirm.models.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginForm extends AppCompatActivity {

    private ScrollView scrollView;
    private TextInputEditText Email, Password;
    private TextView Register_now;
    private ExtendedFloatingActionButton Login;

    private UserDatabaseHandler dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        Objects.requireNonNull(getSupportActionBar()).hide();

        scrollView = findViewById(R.id.login_scroll_layout);

        dbUser = new UserDatabaseHandler(this);

        Email = findViewById(R.id.et_Login_email);
        Password = findViewById(R.id.et_Login_password);
        Register_now = findViewById(R.id.tv_Register_now);
        Login = findViewById(R.id.btn_Login);

        Register_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginForm.this, RegistrationForm.class);
                startActivity(i);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Email.setError("Please Enter your Email.");
                } else if (TextUtils.isEmpty(password)) {
                    Password.setError("Please enter Password");
                }

                Log.e("Entered Username : ", email);
                Log.e("Entered Password : ", password);

                /*
                 * Add user's data into shared preferences.
                 * */
                try {
                    User user = dbUser.login(email, password);

                    getSharedPreferences("user", MODE_PRIVATE).edit().putInt("id", user.getId()).apply();
                    getSharedPreferences("user", MODE_PRIVATE).edit().putString("email", user.getEmail()).apply();

                    if (user != null) {
                        Login.setVisibility(View.GONE);
                        Snackbar.make(scrollView, "Login Successful.", Snackbar.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                getSharedPreferences("user", MODE_PRIVATE).edit().putBoolean("isLoggedIn", true).apply();
                                getSharedPreferences("app", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
                                startActivity(new Intent(LoginForm.this, MainActivity.class));

                            }
                        }, 2000);
                    }
                } catch (CursorIndexOutOfBoundsException e) {
                    Snackbar.make(scrollView, "Wrong Credentials.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
