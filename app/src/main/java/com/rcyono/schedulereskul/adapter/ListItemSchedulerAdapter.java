package com.rcyono.schedulereskul.adapter;


import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rcyono.schedulereskul.R;
import com.rcyono.schedulereskul.model.schedule.Schedule;
import com.rcyono.schedulereskul.ui.home.HomeFragment;
import com.rcyono.schedulereskul.ui.home.ScheduleAdapterActionListener;

import java.util.ArrayList;

public class ListItemSchedulerAdapter extends RecyclerView.Adapter<ListItemSchedulerAdapter.ListItemSchedulerViewHoler> {
    private final ArrayList<Schedule> listScheduler = new ArrayList<>();
    private final ScheduleAdapterActionListener actionListener1;

    public ListItemSchedulerAdapter(ScheduleAdapterActionListener actionListener){
        this.actionListener1 = actionListener;
    }

    public void setAdapter(ArrayList<Schedule> schedulers) {
        if (schedulers.size() > 0) listScheduler.clear();
        listScheduler.addAll(schedulers);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListItemSchedulerViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_scheduler, parent, false);
        return new ListItemSchedulerViewHoler(view, actionListener1);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemSchedulerViewHoler holder, int position) {
        holder.bind(listScheduler.get(position));
    }

    @Override
    public int getItemCount() {
        return listScheduler.size();
    }

    public static class ListItemSchedulerViewHoler extends RecyclerView.ViewHolder {
        private final TextView tvType, tvDesc, tvPlace, tvDate, tvTime;
        private final ImageView ivIcon;
        private final ImageView ivVert;
        private final ScheduleAdapterActionListener actionListener1;


        public ListItemSchedulerViewHoler(@NonNull View itemView, ScheduleAdapterActionListener actionListener) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_title_type);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvPlace = itemView.findViewById(R.id.tv_place);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivIcon = itemView.findViewById(R.id.iv_icon_type);
            ivVert = itemView.findViewById(R.id.iv_vert);
            this.actionListener1 = actionListener;
        }

        public void bind(Schedule scheduler) {
            Glide.with(itemView.getContext())
                    .load(scheduler.getIconType())
                    .into(ivIcon);
            tvType.setText(scheduler.getTitleType());
            tvDesc.setText(scheduler.getDesc());
            tvPlace.setText(scheduler.getPlace());
            tvDate.setText(scheduler.getDate());
            String time = scheduler.getTimeStart() + "-" + scheduler.getTimeEnd();
            tvTime.setText(time);

            ivVert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(ivVert.getContext(), ivVert);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @SuppressLint("NonConstantResourceId")
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()){
                                case R.id.action_edit:
//                                    actionListener.onClickEdit(scheduler);
                                    return true;
                                case R.id.action_delete:
//                                    actionListener.onClickDelete(scheduler, getAbsoluteAdapterPosition());
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    //inflate your menu
                    popupMenu.inflate(R.menu.my_schedule_list_menu);
                    popupMenu.setGravity(Gravity.RIGHT);

                    popupMenu.show();
                }
            });
        }
    }
}
