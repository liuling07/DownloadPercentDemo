package com.bbk.lling.downloadpercentdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;


public class MainActivity extends Activity {

    public final static int MSG_UPDATE = 1;
    public final static int MSG_FINISHED = 2;

    private DownloadPercentView mDownloadPercentView;
    private int mDownloadProgress = 0;
    private Handler mHandler = new InnerHandler();
    private boolean downloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDownloadPercentView = (DownloadPercentView) findViewById(R.id.downloadView);
        mDownloadPercentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDownloadPercentView.getStatus() == DownloadPercentView.STATUS_PEDDING
                        || mDownloadPercentView.getStatus() == DownloadPercentView.STATUS_PAUSED) {
                    downloading = true;
                    mDownloadPercentView.setStatus(DownloadPercentView.STATUS_DOWNLOADING);
                    //Ä£ÄâÏÂÔØ
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (downloading) {
                                if(mDownloadProgress == 100) {
                                    mHandler.sendEmptyMessage(MSG_FINISHED);
                                    return;
                                }
                                mDownloadProgress += 2;
                                mHandler.sendEmptyMessage(MSG_UPDATE);
                                try{
                                    Thread.sleep(500);
                                } catch (Exception e) {
                                }

                            }
                        }
                    }).start();
                } else if(mDownloadPercentView.getStatus() == DownloadPercentView.STATUS_DOWNLOADING){
                    downloading = false;
                    mDownloadPercentView.setStatus(DownloadPercentView.STATUS_PAUSED);
                }
            }
        });
    }

    class InnerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FINISHED:
                    mDownloadPercentView.setStatus(DownloadPercentView.STATUS_FINISHED);
                    break;
                case MSG_UPDATE:
                    mDownloadPercentView.setProgress(mDownloadProgress);
                    break;
            }
            super.handleMessage(msg);
        }
    }


}
