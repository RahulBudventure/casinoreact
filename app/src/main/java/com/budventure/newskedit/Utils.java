package com.budventure.newskedit;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.budventure.newskedit.JBServices.OpenPlayStoretJobService;

public class Utils {
    public static void scheduleJob(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.example.android"));
        context.startActivity(intent);
    }
}
