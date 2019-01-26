package com.example.android.groupchatapp.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.android.groupchatapp.R;

public class HomeActivity extends AppCompatActivity {
   private DrawerLayout drawerLayout;
   private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new CreateGroupFragment()).commit();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
   /*
    *Enable the app bar's "home" button by calling setDisplayHomeAsUpEnabled(true),
   * and then change it to use the menu icon by calling setHomeAsUpIndicator(int), as shown here:
   * */
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                if(menuItem.getItemId()==R.id.profile_drawer){
                    Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
                    startActivity(intent);
                }

                if(menuItem.getItemId()==R.id.create_group){
                    Toast.makeText(HomeActivity.this,"Group Created",Toast.LENGTH_SHORT).show();
                }

                if(menuItem.getItemId()==R.id.logout){
                    Toast.makeText(HomeActivity.this,"logout",Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

    }

    //To open the navigation drawer when we click on the nav drawer button (three parallel lines)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
