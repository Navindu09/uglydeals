package org.nothingugly.uglydeals;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private Button buttonAccountFragmentLogout;
    private FirebaseAuth mAuth;



    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        //Get the firebase authentication instance
        mAuth = FirebaseAuth.getInstance();

        //Get the button from the layout
        buttonAccountFragmentLogout = view.findViewById(R.id.buttonAccountFragmentLogout);

        //When the logout button is clicked
        buttonAccountFragmentLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendToLogin();

            }
        });

        return view;
    }

    //Send to LoginActivity
    private void sendToLogin() {
        Intent loginintent = new Intent(getContext(), MainActivity.class);
        startActivity(loginintent);
        //gets the activity of the fragment and destroys the activity
        getActivity().finish();
    }

}
