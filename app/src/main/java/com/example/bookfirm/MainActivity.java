package com.example.bookfirm;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bookfirm.fragments.AboutUsFragment;
import com.example.bookfirm.fragments.AccountFragment;
import com.example.bookfirm.fragments.AddProductFragment;
import com.example.bookfirm.fragments.MainFragment;
import com.example.bookfirm.fragments.MyProductsFragment;
import com.example.bookfirm.fragments.MyPurchasesFragment;
import com.example.bookfirm.models.Book;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainFragment.OnBookDetailListener, MainFragment.OnMyBookDetailListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new MainFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new MainFragment()).commit();
                menuItem.setCheckable(true);
                break;

            case R.id.nav_my_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new AccountFragment()).commit();
                menuItem.setCheckable(true);
                break;

            case R.id.nav_my_purchase:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new MyPurchasesFragment()).commit();
                menuItem.setCheckable(true);
                break;

            case R.id.nav_my_products:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new MyProductsFragment()).commit();
                menuItem.setCheckable(true);
                break;

            case R.id.nav_add_product:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new AddProductFragment()).commit();
                menuItem.setCheckable(true);
                break;

            case R.id.nav_about_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new AboutUsFragment()).commit();
                menuItem.setCheckable(true);
                break;

            case R.id.nav_logout:
                logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void logout() {

        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you really want to logout.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getSharedPreferences("user", MODE_PRIVATE).edit().putBoolean("isLoggedIn", false).apply();
                        startActivity(new Intent(MainActivity.this, LoginForm.class));
                    }
                })
                .setNegativeButton("No", null)
                .create().show();
    }

    @Override
    protected void onResume() {

        boolean isLoggedIn = getSharedPreferences("user", MODE_PRIVATE).getBoolean("isLoggedIn", false);

        boolean isFirstRun = getSharedPreferences("app", MODE_PRIVATE).getBoolean("isFirstRun", true);

        if (isFirstRun || !isLoggedIn) {
            startActivity(new Intent(MainActivity.this, LoginForm.class));
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                Toast.makeText(this, "Your orders cannot be placed without SMS permission. Please Accept SMS permission from app settings.", Toast.LENGTH_LONG).show();
                finish();

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1000);

            }
        }

        super.onResume();
    }

    @Override
    public void onBookSent(Book book) {
        Intent intent = new Intent(this, BookExtendedView.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // continue
                } else {
                    Toast.makeText(this, "Your orders cannot be placed without SMS permission. Please Accept SMS permission from app settings.", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
        }
    }

    @Override
    public void onMyBookSent(Book book) {
        Intent intent = new Intent(this, MyProductExtendedView.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }
}
