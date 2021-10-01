package com.rcyono.schedulereskul.ui.home;

import com.rcyono.schedulereskul.model.schedule.Schedule;

/**
 * Created by Anggit Prayogo on 26,August,2021
 * GitHub : https://github.com/anggit97
 */
public interface ScheduleAdapterActionListener {
    void onClickDelete(Schedule post, int absoluteAdapterPosition);
    void onClickEdit(Schedule post);
}
