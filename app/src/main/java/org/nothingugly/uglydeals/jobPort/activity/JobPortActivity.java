package org.nothingugly.uglydeals.jobPort.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.nothingugly.uglydeals.LogInActivity;
import org.nothingugly.uglydeals.MainActivity;
import org.nothingugly.uglydeals.R;
import org.nothingugly.uglydeals.SearchFragment;
import org.nothingugly.uglydeals.jobPort.fragments.AppliedFragment;
import org.nothingugly.uglydeals.jobPort.fragments.AppliedJobsFragment;
import org.nothingugly.uglydeals.jobPort.fragments.JobHomeFragment;
import org.nothingugly.uglydeals.jobPort.fragments.ProfileFragment;
import org.nothingugly.uglydeals.jobPort.fragments.SavedJobsFragment;
import org.nothingugly.uglydeals.jobPort.fragments.SystemAnalystFragment;
import org.nothingugly.uglydeals.jobPort.fragments.ViewPagerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class JobPortActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.job_container)
    FrameLayout jobContainer;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_deals)
    Button btnDeals;
    private JobHomeFragment jobHomeFragment;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_port);
        ButterKnife.bind(this);
        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Initialise Firebase app
        //FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        tvToolbarTitle.setText("Job Portal");
        jobHomeFragment = new JobHomeFragment();
        replaceFragment(jobHomeFragment, "Job Portal");
        //When the bottom navigation buttons are clicked
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //if home button clicked, replace fragment with home
                    case (R.id.jobBottomNavigationHome):
                        setTitle("Job Portal");
                        btnDeals.setVisibility(View.VISIBLE);
                        jobHomeFragment = new JobHomeFragment();
                        replaceFragment(jobHomeFragment, "");
                        return true;
                    //if Search button clicked, replace fragment with search
                    case (R.id.jobBottomNavigationNotificationApplication):
                        setTitle("Saved");
                        btnDeals.setVisibility(View.GONE);
                        ViewPagerFragment savedJobsFragment = new ViewPagerFragment();
                        replaceFragment(savedJobsFragment, "");
                        return true;
                    //if account button clicked, replace fragment with account
                    case (R.id.jobBottomNavigationSearch):
                        setTitle("Search");
                        btnDeals.setVisibility(View.GONE);
                        SearchFragment searchFragment = new SearchFragment();
                        replaceFragment(searchFragment, "");
                        return true;
                    //If Flashdeal logo is clicked replace fragment with flasdeal
                    case (R.id.jobBottomNavigationProfile):
                        setTitle("Profile");
                        btnDeals.setVisibility(View.GONE);
                        ProfileFragment profileFragment = new ProfileFragment();
                        replaceFragment(profileFragment, "");
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Retrieve the current logged in user
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        //If not user is logged in
        if (currentUser == null)
        //Open loginActivity to login
        {
            sendToLogin();
        }
    }

    //Send to LoginActivity
    private void sendToLogin() {
        Intent loginintent = new Intent(this, LogInActivity.class);
        startActivity(loginintent);
    }

    public void setTitle(String title) {
        tvToolbarTitle.setText(title);
    }

    public void replaceFragment(Fragment fragment, String title) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.job_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.job_container);
        if (f instanceof JobHomeFragment) {
            setTitle("Job Portal");
            btnDeals.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.GONE);
        } else if (f instanceof SystemAnalystFragment || f instanceof ViewPagerFragment
                || f instanceof AppliedFragment || f instanceof ProfileFragment || f instanceof SearchFragment) {
            btnDeals.setVisibility(View.GONE);
        }
        /*if (f instanceof AppliedFragment) {
            setTitle("Job Portal");
            btnDeals.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.GONE);
            jobHomeFragment = new JobHomeFragment();
            replaceFragment(jobHomeFragment, "Job Portal");
        }*/
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @OnClick(R.id.btn_deals)
    public void onDealsClicked() {
        Intent loginintent = new Intent(this, MainActivity.class);
        startActivity(loginintent);
        finish();
    }

    public void disableView() {
        btnDeals.setVisibility(View.GONE);
    }
}
