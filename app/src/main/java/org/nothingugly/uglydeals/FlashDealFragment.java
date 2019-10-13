package org.nothingugly.uglydeals;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FlashDealFragment extends Fragment {

    private TextView flashDealPartnerName;
    private ImageView flashDealImage;
    private TextView flashDealName;
    private TextView flashDealValidity;
    private TextView flashDealDescription;
    private Button flashDealRedeedmButton;
    private TextView flashDealTerms;
    //private ProgressBar progressBarSelectedItem;
    private TextView textViewAlreadyUsed;


    public FlashDealFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_flash_deal_fragment, container, false);

        flashDealName = (TextView) view.findViewById(R.id.flashDealName);
        flashDealImage = (ImageView) view.findViewById(R.id.flashDealImage);
        //flashDealPartnerName = (ImageView) view.findViewById(R.id.flashDealPartnerName);
        //flashDealValidity = (ImageView) view.findViewById(R.id.flashDealValidity);
        //flashDealImage = (ImageView) view.findViewById(R.id.flashDealImage);

        return view;

    }
}
