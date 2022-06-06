package com.mrq.paljobs.controller.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.net.Uri;

import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.Constants;

import org.jetbrains.annotations.NotNull;

public class MyService extends JobService {

    public MyService() {
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        String name = jobParameters.getExtras().getString("name");
        String path = jobParameters.getExtras().getString("path");
        String type = jobParameters.getExtras().getString("type");
        switch (type) {
            case Constants.TYPE_PHOTO:
                uploadImage(jobParameters, Uri.parse(path), name);
                break;
            case Constants.TYPE_FILE:
                uploadFile(jobParameters, Uri.parse(path), name);
                break;
        }
        return false;
    }

    private void uploadImage(JobParameters jobParameters, Uri imagePath, String imageName) {
        new ApiRequest<>().uploadImage(
                getBaseContext(),
                imagePath,
                imageName,
                Constants.TYPE_PHOTO,
                new Results<String>() {
                    @Override
                    public void onSuccess(String s) {
                        jobFinished(jobParameters, false);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                    }

                    @Override
                    public void onEmpty() {

                    }

                    @Override
                    public void onLoading(boolean loading) {
                    }
                });
    }

    private void uploadFile(JobParameters jobParameters, Uri filePath, String fileName) {
        new ApiRequest<>().uploadFile(
                getBaseContext(),
                filePath,
                fileName,
                "update",
                new Results<String>() {
                    @Override
                    public void onSuccess(String s) {
                        jobFinished(jobParameters, false);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                    }

                    @Override
                    public void onEmpty() {

                    }

                    @Override
                    public void onLoading(boolean loading) {
                    }
                });
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}