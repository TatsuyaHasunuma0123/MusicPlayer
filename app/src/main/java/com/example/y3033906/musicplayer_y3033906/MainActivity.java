package com.example.y3033906.musicplayer_y3033906;

import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //再生する音楽のデータを入れるMusicPlayer
    private MediaPlayer player = null;
    //再生ボタンのフィールドを用意（ボタンの画像変更のため）
    private ImageButton music_playback;
    //再生する音楽の時間を管理するduration
    private Integer duration;
    //定期的にタスクを実行させるための変数time
    private Timer time = new Timer();
    //音楽の再生番号を管理
    private Integer musicNumber;
    //曲のタイトルを表示する
    private TextView title,length;
    private Integer musicFile[] = new Integer[3];

    class music_struct{
        Integer musicid[] = new Integer[musicFile.length];
        String music_name[] = new String[musicFile.length];
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicFile[1] = R.raw.sample1;
        musicFile[2] = R.raw.sample2;
        musicFile[3] = R.raw.sample3;

        //musicNumberを初期化
        musicNumber = 1;

        //textViewを初期化
        title = findViewById(R.id.textView_title);
        length = findViewById(R.id.textView_time);

        /*-----------------------------------イメージボタンの設定-----------------------------------*/
        //再生ボタン
        music_playback = findViewById(R.id.music_playback);
        music_playback.setOnClickListener(this);
        //「→→」ボタン
        ImageButton next = findViewById(R.id.button_next);
        next.setOnClickListener(this);
        //「←←」ボタン
        ImageButton back = findViewById(R.id.button_back);
        back.setOnClickListener(this);
        /*----------------------------------------------------------------------------------------*/

        /*--------------------------------------ボタンの設定---------------------------------------*/
        //「×0.5」ボタン
        Button speed0_5 = findViewById(R.id.speed0_5);
        speed0_5.setOnClickListener(this);
        //「×0.75」ボタン
        Button speed0_75 = findViewById(R.id.speed0_75);
        speed0_75.setOnClickListener(this);
        //「×1」ボタン
        Button speed1 = findViewById(R.id.speed1);
        speed1.setOnClickListener(this);
        //「×1.5」ボタン
        Button speed1_5 = findViewById(R.id.speed1_5);
        speed1_5.setOnClickListener(this);
        //「×2」ボタン
        Button speed2 = findViewById(R.id.speed2);
        speed2.setOnClickListener(this);
        //「↺10」ボタン
        Button skip10 = findViewById(R.id.sekip10);
        skip10.setOnClickListener(this);
        //「↻10」ボタン
        Button back10 = findViewById(R.id.back10);
        back10.setOnClickListener(this);
        /*----------------------------------------------------------------------------------------*/
    }

    @Override
    protected  void onResume(){
        super.onResume();

        //　res/raw　フォルダにある音楽ファイルを読み込み
        setMusic();

        //ファイルの長さを取得し、その値をseekBarの最大値とする
        duration = player.getDuration();
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(duration);

        //1秒毎にタスクを実行するtime.schedule
        time.schedule(new TimerTask() {
            //楽曲の再生している位置を読み込み、seekBarを動かす
            @Override
            public void run() {
                if(player.isPlaying()) {
                    Integer now = player.getCurrentPosition();
                    seekBar.setProgress(now);
                }
            }
        },10,1000);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //つまみがドラッグされると呼ばれる
                    @Override
                    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                        //ドラッグされた位置まで楽曲の再生位置を移動
                        player.seekTo(progress);
                    }
                    //つまみがタッチされた時に呼ばれる
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        //一時停止
                        player.pause();
                        //一時停止されているため、再生ボタンの画像をplayに変更
                        music_playback.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    }
                    //つまみがリリースされた時に呼ばれる
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        //音楽を再生
                        player.start();
                        //再生されるため、再生ボタンの画像をpauseに変更
                        music_playback.setImageResource(R.drawable.ic_baseline_pause_24);
                    }
                });
    }

    @Override
    protected void onPause(){
        super.onPause();
        player.stop();
        player.release();
    }

    public  void onClick(View view){
        //再生速度を変更するために必要な変数parms
        /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
        /*--------------------APIレベル23以上を要求!! 23未満ではビルドエラー!!------------------------*/
        //APIレベル23以上でなければコメントアウトをお願いします。
        PlaybackParams params = new PlaybackParams();
        /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/

        switch(view.getId()) {
            //再生ボタンが押された時
            case R.id.music_playback:
                //再生状態であれば一時停止
                if (player.isPlaying()) {
                    player.pause();
                    //画像変更
                    music_playback.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }
                //一時停止状態であれば再生
                else {
                    player.start();
                    //画像変更
                    music_playback.setImageResource(R.drawable.ic_baseline_pause_24);
                }
                break;
             /*--------------------------倍速再生のボタンが押された時--------------------------------*/
            //APIレベル23以上でなければコメントアウトをお願いします。
            case R.id.speed0_5:
                params.setSpeed(0.5f);
                player.setPlaybackParams(params);
                break;
            case R.id.speed0_75:
                params.setSpeed(0.75f);
                player.setPlaybackParams(params);
                break;
            case R.id.speed1:
                params.setSpeed(1.f);
                player.setPlaybackParams(params);
                break;
            case R.id.speed1_5:
                params.setSpeed(1.5f);
                player.setPlaybackParams(params);
                break;
            case R.id.speed2:
                params.setSpeed(2.f);
                player.setPlaybackParams(params);
                break;
             /*-----------------------------------------------------------------------------------*/

            /*---------------------10秒進める、戻すボタンが押された時---------------------------------*/
            case R.id.sekip10:
                if(player.isPlaying()) {
                    Integer now = player.getCurrentPosition();
                    Integer seekTime = now + 10000;
                    if(seekTime > duration)
                        seekTime = duration;
                    player.seekTo(seekTime);
                }
                break;
            case R.id.back10:
                if(player.isPlaying()) {
                    Integer now = player.getCurrentPosition();
                    Integer seekTime = now - 10000;
                    if(seekTime < 0)
                        seekTime = 0;
                    player.seekTo(seekTime);
                }
                break;
            /*------------------------------------------------------------------------------------*/

            case R.id.button_next:
                player.stop();
                setMusic();
                break;
            case R.id.button_back:
                duration = 0;
                player.seekTo(duration);
                break;
        }
    }

    public void setMusic(){
        player = MediaPlayer.create(this, musicFile[musicNumber]);
        musicNumber = musicNumber % musicFile.length + 1;
    }
}