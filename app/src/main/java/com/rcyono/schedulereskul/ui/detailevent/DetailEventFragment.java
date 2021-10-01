package com.rcyono.schedulereskul.ui.detailevent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.rcyono.schedulereskul.R;
import com.rcyono.schedulereskul.model.event.Event;

public class DetailEventFragment extends Fragment {
    private Toolbar toolbar;
    private TextView tvTitle, tvDesc;
    private ImageView ivEvent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_event, container, false);
        toolbar = view.findViewById(R.id.main_toolbar);
        tvTitle = view.findViewById(R.id.tv_title_event);
        tvDesc = view.findViewById(R.id.tv_desc);
        ivEvent = view.findViewById(R.id.iv_event);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        Event event = DetailEventFragmentArgs.fromBundle(getArguments()).getEvent();
        if (event != null) {
            Glide.with(view.getContext())
                    .load(event.getImagePath())
                    .into(ivEvent);

            tvTitle.setText(event.getTitle());
            tvDesc.setText(event.getDesc());
        }
    }
}