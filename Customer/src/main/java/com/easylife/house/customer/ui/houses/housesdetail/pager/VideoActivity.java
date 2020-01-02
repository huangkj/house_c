package com.easylife.house.customer.ui.houses.housesdetail.pager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.easylife.house.customer.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity {

    @Bind(R.id.standplayer)
    StandardGSYVideoPlayer standplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("VIDEO_URL");
        standplayer.setLockLand(true);
        standplayer.setNeedLockFull(true);
        standplayer.setRotateViewAuto(false);
        standplayer.setShowFullAnimation(false);
//        standplayer.setUp("http://baobab.wdjcdn.com/14564977406580.mp4",false);
        standplayer.setUp(url,false);
        standplayer.startPlayLogic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        standplayer.onVideoPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}