package com.rcyono.schedulereskul.ui.add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textfield.TextInputEditText;
import com.rcyono.schedulereskul.R;
import com.rcyono.schedulereskul.model.event.Event;
import com.rcyono.schedulereskul.model.schedule.Schedule;
import com.rcyono.schedulereskul.model.type.Type;
import com.rcyono.schedulereskul.model.user.User;
import com.rcyono.schedulereskul.network.ApiConfig;
import com.rcyono.schedulereskul.network.ApiService;
import com.rcyono.schedulereskul.preferences.AppPreferences;

import java.util.ArrayList;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSchedulerFragment extends Fragment implements View.OnClickListener {
    private Toolbar toolbar;
    private final ArrayList<String> arrayTypeDrop = new ArrayList<>();
    private AutoCompleteTextView tvTypeEskul, tvDate, tvTimeStart, tvTimeEnd;
    private TextInputEditText edtDesc, edtPlace;
    private Button btnSave;
    private boolean editMode = false;
    private Schedule schedule;
    public static String POST_KEY = "POST_KEY";
    private Schedule post;

    private AddViewModel addViewModel;
    private User user;

    private SweetAlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_scheduler, container, false);
        toolbar = view.findViewById(R.id.main_toolbar);
        tvTypeEskul = view.findViewById(R.id.tv_title_eskul);
        edtDesc = view.findViewById(R.id.edt_desc);
        edtPlace = view.findViewById(R.id.edt_place);
        tvDate = view.findViewById(R.id.edt_date);
        tvTimeStart = view.findViewById(R.id.edt_time_start);
        tvTimeEnd = view.findViewById(R.id.edt_time_end);
        btnSave = view.findViewById(R.id.btn_save);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        btnSave.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTimeStart.setOnClickListener(this);
        tvTimeEnd.setOnClickListener(this);

        AppPreferences preferences = new AppPreferences(requireActivity());
        user = preferences.getUser();

        // check schedule if not null -> editMode false -> edit
        // if null -> post
        handleIntent();
    }

    private void handleIntent() {
        if (schedule != null) {
            editMode = true;

            tvTypeEskul.setText(schedule.getTitleType());
            edtDesc.setText(schedule.getDesc());
            edtPlace.setText(schedule.getPlace());
            tvDate.setText(schedule.getDate());
            tvTimeStart.setText(schedule.getTimeStart());
            tvTimeEnd.setText(schedule.getTimeEnd());
        }

        sendDataToServer();
    }

    private void sendDataToServer() {
        if (editMode) {
            editPost();
        } else {
            createPost();
        }
    }

    private void editPost() {
        Schedule request = new Schedule();
                schedule.getIdUser();
                schedule.getTitleType();
                schedule.getDesc();
                schedule.getPlace();
                schedule.getDate();
                schedule.getTimeStart();
                schedule.getTimeEnd();

        ApiService client = ApiConfig.createService(ApiService.class);
        client.editPost(request, String.valueOf(post.getId())).enqueue(new Callback<Schedule>() {
            @Override
            public void onResponse(Call<Schedule> call, Response<Schedule> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getApplicationContext(), "Failed send data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Schedule> call, Throwable t) {

            }
        });
    }

    private void createPost() {
        addViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.NewInstanceFactory()).get(AddViewModel.class);
        addViewModel.getType().observe(requireActivity(), type -> {
            for (Type arr : type.getType()) {
                arrayTypeDrop.add(arr.getTitleType());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), R.layout.item_list_drop, arrayTypeDrop);
                tvTypeEskul.setAdapter(adapter);
            }
        });

        addViewModel.addSchedule().observe(requireActivity(), response -> {
            if (response.getSuccess() == 1) {
                alertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE);
                alertDialog.setTitleText("Register Success!");
                alertDialog.setContentText("Next to continue");
            } else {
                alertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE);
                alertDialog.setTitleText("Register Failed!");
                alertDialog.setContentText("Unable to retrieve any data from server");
            }
            alertDialog.show();
            alertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setVisibility(View.GONE);
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save) {
            Schedule schedule = new Schedule();
            schedule.setIdUser(user.getId());
            schedule.setTitleType(tvTypeEskul.getText().toString());
            String date = tvDate.getText().toString();
            String timeStart = tvTimeStart.getText().toString();
            String timeEnd = tvTimeEnd.getText().toString();
            boolean isEmptyField = false;

            if (Objects.requireNonNull(edtDesc.getText()).toString().isEmpty()) {
                edtDesc.setError(requireActivity().getResources().getString(R.string.field_not_empty));
                isEmptyField = true;
            } else if (Objects.requireNonNull(edtPlace.getText()).toString().isEmpty()) {
                edtPlace.setError(requireActivity().getResources().getString(R.string.field_not_empty));
                isEmptyField = true;
            } else if (date.isEmpty() || timeStart.isEmpty() || timeEnd.isEmpty()) {
                isEmptyField = true;
            }

            schedule.setDesc(Objects.requireNonNull(edtDesc.getText().toString()));
            schedule.setPlace(Objects.requireNonNull(edtPlace.getText()).toString());
            schedule.setDate(date);
            schedule.setTimeStart(timeStart);
            schedule.setTimeEnd(timeEnd);

            if (!isEmptyField) {
                addViewModel.addSchedule(schedule);
            }

        } else if (view.getId() == R.id.edt_date) {
            Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireActivity(), (datePicker, years, months, days) -> {
                months = months + 1;
                String date = days + "/" + months + "/" + years;
                tvDate.setText(date);
            }, year, month, day);

            datePickerDialog.show();

        } else if (view.getId() == R.id.edt_time_start) {
            getTime(1);

        } else if (view.getId() == R.id.edt_time_end) {
            getTime(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getTime(int action) {
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(), (timePicker, hours, minutes) -> {
            String time = hours + ":" + minutes;
            if (action == 1) {
                tvTimeStart.setText(time);
            } else {
                tvTimeEnd.setText(time);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }


}