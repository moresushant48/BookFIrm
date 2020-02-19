package com.example.bookfirm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookfirm.db.UserDatabaseHandler;
import com.example.bookfirm.models.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class RegistrationForm extends AppCompatActivity {

    private EditText username, email, address, mobileno, password, passConfirm;
    private CheckBox chkTerms;
    private ExtendedFloatingActionButton Register;
    private TextView tvGoToLogin;
    private UserDatabaseHandler dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        Objects.requireNonNull(getSupportActionBar()).hide();

        username = findViewById(R.id.et_register_full_name);
        email = findViewById(R.id.et_Email);
        address = findViewById(R.id.et_Address);
        mobileno = findViewById(R.id.et_Contact);
        password = findViewById(R.id.et_pass);
        passConfirm = findViewById(R.id.et_pass_confirm);
        chkTerms = findViewById(R.id.chk_terms);
        tvGoToLogin = findViewById(R.id.tv_goToLogin);
        Register = findViewById(R.id.btn_Register);

        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationForm.this, LoginForm.class));
            }
        });


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validation()) {

                    addUserToDB();

                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(RegistrationForm.this, LoginForm.class));
                        }
                    }, 2000);
                }
            }
        });
    }

    private void addUserToDB() {

        User user = new User(username.getText().toString().trim(),
                email.getText().toString().trim(),
                address.getText().toString().trim(),
                mobileno.getText().toString().trim(),
                password.getText().toString().trim());
        dbUser = new UserDatabaseHandler(this);
        dbUser.addUser(user);

        UserDatabaseHandler dbUser = new UserDatabaseHandler(this);
        ArrayList<User> userRead = dbUser.allUsers();
        for (User u : userRead) {
            System.out.println("=====================================================");
            Log.e("Name : ", u.getUsername());
            Log.e("Email : ", u.getEmail());
            System.out.println("=====================================================");
        }
    }

    public boolean Validation() {

        String txtUsername, txtEmail, txtAddress, txtMobileNo, txtPassword, txtPassConfirm;
        txtUsername = username.getText().toString().trim();
        txtEmail = email.getText().toString().trim();
        txtAddress = address.getText().toString().trim();
        txtMobileNo = mobileno.getText().toString().trim();
        txtPassword = password.getText().toString().trim();
        txtPassConfirm = passConfirm.getText().toString().trim();

        if (txtUsername.isEmpty()) {
            username.setError("Enter Full Name");
            return false;
        } else {
            username.setError(null);
        }

        if (txtEmail.isEmpty()) {
            email.setError("Enter Email Id");
            return false;
        } else {
            email.setError(null);
        }

        if (txtAddress.isEmpty()) {
            address.setError("Enter Address");
            return false;
        } else if (txtAddress.length() < 20) {
            address.setError("Address should be atleast of more than 20 characters.");
            return false;
        } else {
            address.setError(null);
        }

        if (txtMobileNo.isEmpty()) {
            mobileno.setError("Enter Mobile No");
            return false;
        } else if (txtMobileNo.length() != 10) {
            mobileno.setError("Invalid Mobile Number Range.");
            return false;
        } else {
            mobileno.setError(null);
        }

        if (txtPassword.isEmpty()) {
            password.setError("Enter Password");
            return false;
        } else if (txtPassword.length() < 6) {
            password.setError("Atleast 6 characters");
            return false;
        } else {
            password.setError(null);
        }

        if (txtPassConfirm.isEmpty()) {
            passConfirm.setError("Re-enter Password");
            return false;
        } else if (!txtPassConfirm.equals(txtPassword)) {
            passConfirm.setError("Password does not match");
            return false;
        } else {
            passConfirm.setError(null);
        }

        if (!chkTerms.isChecked()) {
            Toast.makeText(RegistrationForm.this, "Please read and accept terms & conditions.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}

