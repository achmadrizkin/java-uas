package com.rcyono.schedulereskul.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rcyono.schedulereskul.R;
import com.rcyono.schedulereskul.adapter.ListItemSchedulerAdapter;
import com.rcyono.schedulereskul.adapter.SliderImageEventAdapter;
import com.rcyono.schedulereskul.model.schedule.Schedule;
import com.rcyono.schedulereskul.model.user.User;
import com.rcyono.schedulereskul.preferences.AppPreferences;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeFragment extends Fragment {
    private SweetAlertDialog alertDialog;
    private RecyclerView rvSchedule;
    private ImageView ivAvatar;
    private ProgressBar progressBar;
    private HomeFragment adapter;

    private SliderView sliderEventView;
    private SliderImageEventAdapter sliderImageEventAdapter;

    private ListItemSchedulerAdapter listItemSchedulerAdapter;

    private User user;
    public static int REQUEST_CODE_CREATE_UPDATE_SUCCESS = 200;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sliderEventView = view.findViewById(R.id.auto_slider_event);
        rvSchedule = view.findViewById(R.id.rv_schedule);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        progressBar = view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initRecyclerview();

        AppPreferences preferences = new AppPreferences(requireActivity());
        user = preferences.getUser();

        HomeViewModel homeViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);
        homeViewModel.getEvent().observe(requireActivity(), event -> {
            sliderImageEventAdapter.setAdapter(event.getImage());
            sliderEventView.setSliderAdapter(sliderImageEventAdapter);
        });

        homeViewModel.getAllSchedule(user.getId());
        homeViewModel.getSchedule().observe(requireActivity(), schedule -> {
            if (schedule.getSchedule() != null) {
                listItemSchedulerAdapter.setAdapter((ArrayList<Schedule>) schedule.getSchedule());
            }
        });

        setAlertDialog();

        homeViewModel.isLoading().observe(requireActivity(), load -> {
            if (load) {
                progressBar.setVisibility(View.VISIBLE);
            }else {
                progressBar.setVisibility(View.GONE);
            }
        });

        setSlideEvent();
        setupRecylerSchedule();
        setAvatar(view);
    }

    private void initRecyclerview() {
        sliderImageEventAdapter = new SliderImageEventAdapter();
        listItemSchedulerAdapter = new ListItemSchedulerAdapter(null);
    }

    private void setSlideEvent() {
        sliderEventView.setIndicatorAnimation(IndicatorAnimationType.DROP);
        sliderEventView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderEventView.setIndicatorSelectedColor(getResources().getColor(R.color.blue_main));
        sliderEventView.setIndicatorUnselectedColor(getResources().getColor(R.color.white));
        sliderEventView.startAutoCycle();
        sliderEventView.setOnIndicatorClickListener(position -> sliderEventView.setCurrentPagePosition(position));
    }

    private void setupRecylerSchedule() {
        rvSchedule.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvSchedule.setHasFixedSize(true);
        rvSchedule.setAdapter(listItemSchedulerAdapter);
    }

    private void setAlertDialog() {
        alertDialog = new SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE);
        alertDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_main));
        alertDialog.setTitleText("Loading");
        alertDialog.setCancelable(false);
    }

    private void setAvatar(View view) {
        Glide.with(view.getContext())
                .load(user.getImagePath())
                .into(ivAvatar);
    }

//    @Override
//    public void onClickEdit(Schedule post) {
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        startActivityForResult(intent, REQUEST_CODE_CREATE_UPDATE_SUCCESS);
//    }
//
//    @Override
//    public void onClickDelete(Schedule post, int absoluteAdapterPosition) {
//        showPopupDelete(post, absoluteAdapterPosition);
//    }

    private void showPopupDelete(Schedule post, int absoluteAdapterPosition) {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Delete Post")
                .setMessage("Are you sure to delete post \"" + post.getDesc() + "\"?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        deletePostFromServer(post, absoluteAdapterPosition);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

//    private void deletePostFromServer(Schedule post, int absoluteAdapterPosition) {
//        progressBar.setVisibility(View.VISIBLE);
//        ApiService client = ApiConfig.createService(ApiService.class);
//        client.deletePost(String.valueOf(post.getId())).enqueue(new Callback<DeletePostResponse>() {
//            @Override
//            public void onResponse(Call<DeletePostResponse> call, Response<DeletePostResponse> response) {
//                progressBar.setVisibility(View.GONE);
//                if (response.isSuccessful()) {
//                    adapter.removePost(post, absoluteAdapterPosition);
//                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "Delete post is failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DeletePostResponse> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}