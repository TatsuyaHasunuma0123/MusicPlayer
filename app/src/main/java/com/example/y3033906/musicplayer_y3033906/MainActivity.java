package com.example.y3033906.musicplayer_y3033906;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer player = null;
    private ImageButton button_playback;
    private SeekBar seekbar;
    private Integer duration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_playback = findViewById(R.id.button_playback);
        button_playback.setOnClickListener(this);
    }


    @Override
    protected  void onResume(){
        super.onResume();
        player = MediaPlayer.create(this, R.raw.test);
        //ファイルの長さを取得
        duration = player.getDuration();
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(duration);
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //つまみがドラッグされると呼ばれる
                    @Override
                    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                        player.seekTo(progress);
                        //player.stop();
                    }
                    //つまみがタッチされた時に呼ばれる
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        player.pause();
                        button_playback.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    }
                    //つまみがリリースされた時に呼ばれる
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        player.start();
                        button_playback.setImageResource(R.drawable.ic_baseline_pause_24);
                    }


                });
    }

    @Override
    protected void onPause(){
        super.onPause();
        player.stop();
        player.release();
    }

    //終了処理(メモリの解放)
    @Override
    protected void  onDestroy(){
        super.onDestroy();
        player.release(); //メモリの解放
        player = null; //音楽プレイヤーを破棄
    }

    public  void onClick(View view){
        switch(view.getId()){
            case R.id.button_playback:
                if(player.isPlaying()){
                    player.pause();
                    button_playback.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                } else{
                    player.start();
                    button_playback.setImageResource(R.drawable.ic_baseline_pause_24);
                }
        }
    }

    public void setSeekBar(){
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(0);
        seekBar.setMax(duration);
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //つまみがドラッグされると呼ばれる
                    @Override
                    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                        player.seekTo(progress);
                        player.start();
                    }
                    //つまみがタッチされた時に呼ばれる
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        player.stop();
                    }
                    //つまみがリリースされた時に呼ばれる
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
    }
}