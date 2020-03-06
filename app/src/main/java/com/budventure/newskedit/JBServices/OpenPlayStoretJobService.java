package com.budventure.newskedit.JBServices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.budventure.newskedit.Utils;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class OpenPlayStoretJobService extends JobService {
    private static final String TAG = "SyncService";
    @Override
    public boolean onStartJob(JobParameters params) {
        Utils.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
