package org.nothingugly.uglydeals.jobPort.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.nothingugly.uglydeals.R;
import org.nothingugly.uglydeals.jobPort.fragments.JobHomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class JobPortActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.job_container)
    FrameLayout jobContainer;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    private JobHomeFragment jobHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_port);
        ButterKnife.bind(this);
        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        jobHomeFragment = new JobHomeFragment();
        replaceFragment(jobHomeFragment, "Job Port");

        //When the bottom navigation buttons are clicked
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //if home button clicked, replace fragment with home
                    case (R.id.jobBottomNavigationHome):
                        jobHomeFragment = new JobHomeFragment();
                        replaceFragment(jobHomeFragment, "Job Port");
                        return true;
                    //if Search button clicked, replace fragment with search
                    case (R.id.jobBottomNavigationNotificationApplication):
                        Toast.makeText(JobPortActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
                        return true;
                    //if account button clicked, replace fragment with account
                    case (R.id.jobBottomNavigationSearch):
                        Toast.makeText(JobPortActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
                        return true;
                    //If Flashdeal logo is clicked replace fragment with flasdeal
                    case (R.id.jobBottomNavigationProfile):
                        Toast.makeText(JobPortActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment, String title) {
        tvToolbarTitle.setText(title);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.job_container, fragment);
        fragmentTransaction.commit();
    }
}
