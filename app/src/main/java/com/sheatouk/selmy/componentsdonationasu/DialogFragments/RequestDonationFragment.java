package com.sheatouk.selmy.componentsdonationasu.DialogFragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDonationFragment extends DialogFragment {
    @BindView(R.id.request_comp) TextView compNameView;
    @BindView(R.id.request_comp_distance) TextView distanceView;
    @BindView(R.id.request_caller_name) TextView userNameView;
    @BindView(R.id.request_donator_name) TextView donatorView;
    @BindView(R.id.request_due_date) TextView dueDateView;
    @BindView(R.id.request_confirm_btn) Button confirmBtn;

    public RequestDonationFragment() {
        // Required empty public constructor
    }
    public static RequestDonationFragment newInstance(String key) {
        RequestDonationFragment frag = new RequestDonationFragment();
        Bundle args = new Bundle();
        args.putString(Constant.FIREBASE__KEY, key);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.content_donation_request, container);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        String key = getArguments().getString(Constant.FIREBASE__KEY);
        // Show soft keyboard automatically and request focus to field
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        // mEditText.requestFocus();
        compNameView.setText(key);
    }

}
