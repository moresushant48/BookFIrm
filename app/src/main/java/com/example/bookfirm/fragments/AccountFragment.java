package com.example.bookfirm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bookfirm.R;
import com.example.bookfirm.db.UserDatabaseHandler;
import com.example.bookfirm.models.User;

import java.util.Objects;

public class AccountFragment extends Fragment {

    UserDatabaseHandler dbUser;

    private TextView txtUsername, txtEmail, txtAddress, txtMobileNo;
    private int userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getInt("id", 0);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(getString(R.string.nav_account));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_account_fragment, container, false);

        txtUsername = view.findViewById(R.id.txtUsername);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtMobileNo = view.findViewById(R.id.txtMobileNo);

        dbUser = new UserDatabaseHandler(getContext());

        User user = dbUser.getUser(userId);
        txtUsername.setText(user.getUsername());
        txtEmail.setText(user.getEmail());
        txtAddress.setText(user.getAddress());
        txtMobileNo.setText(user.getMobileno());

        return view;
    }
}
