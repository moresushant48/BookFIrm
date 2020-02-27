package com.example.bookfirm.BottomSheets;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookfirm.R;
import com.example.bookfirm.db.UserDatabaseHandler;
import com.example.bookfirm.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class UpdateAddress extends BottomSheetDialogFragment implements View.OnClickListener {

    private User user;

    private UserDatabaseHandler dbUser;

    private TextInputEditText etNewAddress;
    private ExtendedFloatingActionButton btnUpdateAddress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbUser = new UserDatabaseHandler(getContext());
        int userId = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE).getInt("id", 0);
        user = dbUser.getUser(userId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_address_bsm, container, false);

        etNewAddress = view.findViewById(R.id.etNewAddress);
        btnUpdateAddress = view.findViewById(R.id.btnUpdateAddress);

        etNewAddress.setText(user.getAddress());
        btnUpdateAddress.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnUpdateAddress.getId()) {

            if (!TextUtils.isEmpty(Objects.requireNonNull(etNewAddress.getText()).toString().trim())) {

                if (etNewAddress.getText().toString().trim().length() < 20) {

                    etNewAddress.setError("Address should be atleast of more than 20 characters.");
                } else {

                    dbUser.updateAddress(user.getId(), etNewAddress.getText().toString().trim());
                    Toast.makeText(getContext(), "Address updated.", Toast.LENGTH_LONG).show();
                    this.dismiss();
                }
            } else {
                Toast.makeText(getContext(), "Empty Address field.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
