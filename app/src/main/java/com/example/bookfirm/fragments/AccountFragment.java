package com.example.bookfirm.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bookfirm.BottomSheets.UpdateAddress;
import com.example.bookfirm.R;
import com.example.bookfirm.db.UserDatabaseHandler;
import com.example.bookfirm.models.User;

import java.util.Objects;

public class AccountFragment extends Fragment implements View.OnClickListener {

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

        txtAddress.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == txtAddress.getId()){

            openUpdateAddressBottomSheet();

        }
    }

    private void openUpdateAddressBottomSheet() {

        new AlertDialog.Builder(getContext())
                .setTitle("Update Address ?")
                .setMessage("Do you really want to change your address ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UpdateAddress updateAddress = new UpdateAddress();
                        updateAddress.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "UpdateAddress");
                    }
                })
                .setNegativeButton("No", null)
                .create().show();

    }
}
