package com.example.java_sticker.group;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.annotations.GwtCompatible;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent.ListenableFuture;

import javax.security.auth.callback.Callback;
//
//public class MyWorker extends ListenableWorker {
//
//    public MyWorker(@NonNull Context appContext, @NonNull WorkerParameters params) {
//        super(appContext, params);
//    }
//
//    @NonNull
//    @Override
////    @Target({ElementType.TYPE,ElementType.METHOD})
//    @GwtCompatible
////    public com.google.common.util.concurrent.ListenableFuture startWork() {
////
////            Callback callback = new Callback() {
////                int successes = 0;
////
//////                @Override
//////                public void onFailure(Call call, IOException e) {
//////                    completer.setException(e);
//////                }
//////
//////                @Override
//////                public void onResponse(Call call, Response response) {
//////                    ++successes;
//////                    if (successes == 100) {
//////                        completer.set(Result.success());
//////                    }
//////                }
////            };
////
////            for (int i = 0; i < 100; ++i) {
////                downloadAsynchronously("https://www.google.com", callback);
////            }
////            return (ListenableFuture<Result>) callback;
////
////    }
//
//    private void downloadAsynchronously(String s, Callback callback) {
//    }
//
//
//    @Override
//    public void onStopped() {
//        // Cleanup because you are being stopped.
//    }
//}