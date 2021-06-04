package com.example.y3033906.musicplayer_y3033906;

import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /*--------------------------MediaPlayer関連のフィールド-----------------------------------------*/
    //再生する音楽のデータを入れるMusicPlayer
    private MediaPlayer player;

    //再生する音楽の時間を管理する
    private Integer duration;
    //音楽の再生番号を管理
    private Integer musicNumber;
    //音楽の現在の再生位置を管理
    private Integer now;
    /*--------------------------------------------------------------------------------------------*/


    /*---------------------------------Layout関連--------------------------------------------------*/
    //曲のタイトル、時間、アイコン画像を表示する変数
    private TextView title,musicLength,nowProgress;
    private ImageView imageView;
    //再生ボタン（ボタンの画像変更のため）
    private ImageButton music_playback;
    /*--------------------------------------------------------------------------------------------*/


    //定期的にタスクを実行させるための変数time
    private Timer time = new Timer();


    /*-----------------楽曲の番号、タイトル、アイコン画像を管理するMusicクラス--------------------------*/
    private Music musics[] = new Music[3];
    class Music{
        //音楽の番号
        Integer id;
        //音楽のタイトル
        String title;
        //音楽のアイコン画像
        Integer image;
        Music(Integer id, String title, Integer image){
            this.id = id;
            this.title = title;
            this.image = image;
        }
    }
    /*---------------------------------------------------------------------------------------------*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //音楽の番号、タイトル、画像を設定
        musics[0] = new Music(R.raw.sample1,"Sample1",R.drawable.ic_launcher_round);
        musics[1] = new Music(R.raw.sample2,"Sample2", R.drawable.sample2);
        musics[2] = new Music(R.raw.sample3,"Sample3",R.drawable.sample3);


        //musicNumberを初期化
        musicNumber = 0;

        //textViewの設定
        title = findViewById(R.id.textView_title);
        musicLength = findViewById(R.id.textView_time);
        nowProgress = findViewById(R.id.textView_progress);

        //imageViewの設定
        imageView = findViewById(R.id.imageView);

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
        setMusic();
        changeImageButton();

        nowProgress.setText("0:00");

        //ファイルの長さを取得し、その値をseekBarの最大値とする
        duration = player.getDuration();
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(0);
        seekBar.setMax(duration);

        /*---------------------1秒毎にタスクを実行するtime.schedule----------------------------------*/
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                //楽曲の再生している位置を読み込み、seekBarを自動で動かす
                if(player.isPlaying()) {
                    now = player.getCurrentPosition();
                    seekBar.setProgress(now);

                    //曲が最後まで再生された場合、次の曲へ
                    if(presentTimeFormat(now).equals(presentTimeFormat(duration))) {
                        //musicNumberを次の曲に設定
                        musicNumber = (musicNumber + 1) % musics.length;

                        //UI変更のため、メインスレッドでの動作を行う。
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onResume();
                                player.start();
                                changeImageButton();
                            }
                        });

                    }
                }
            }
        },10,1000); //10msのdelay //1000ms = 1s 毎に実行
        /*----------------------------------------------------------------------------------------*/

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //つまみがドラッグされると呼ばれる
                    @Override
                    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                        //ドラッグされた位置まで楽曲の再生位置を移動
                        player.seekTo(progress);
                        //nowProgressに現在の再生位置を表示
                        nowProgress.setText(presentTimeFormat(progress));
                    }
                    //つまみがタッチされた時に呼ばれる
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        if(player.isPlaying()) {
                            player.pause();
                            changeImageButton();
                        }
                    }
                    //つまみがリリースされた時に呼ばれる
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        player.start();
                        changeImageButton();
                    }
                });
    }

    //メモリの解放
    @Override
    protected void onPause(){
        super.onPause();
        player.stop();
        player.release();
    }

    public  void onClick(View view){

        /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
        /*--------------------APIレベル23以上を要求!! 23未満ではビルドエラー!!------------------------*/
        //APIレベル23以上でなければコメントアウトをお願いします。
        //「MusicPlayer_y3033906/app/src/build.grade」のminSdkVersionを実機のAPIレベルに変更してください。
        //再生速度を変更するために必要な変数parms
        PlaybackParams params = new PlaybackParams();
        /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/

        switch(view.getId()) {
            /*-----------------------------再生ボタンが押された時-----------------------------------*/
            case R.id.music_playback:
                //再生状態であれば一時停止
                if (player.isPlaying()) {
                    player.pause();
                }
                //一時停止状態であれば再生
                else {
                    player.start();
                }
                changeImageButton();
                break;
            /*------------------------------------------------------------------------------------*/

             /*--------------------------倍速再生のボタンが押された時--------------------------------*/
            //APIレベル23以上でなければコメントアウトをお願いします。
            case R.id.speed0_5:
                params.setSpeed(0.5f);
                player.setPlaybackParams(params);
                //一時停止状態の場合でもそのまま再生されてしまうため、
                //一時停止の処理を行う。
                player.pause();
                break;
            case R.id.speed0_75:
                params.setSpeed(0.75f);
                player.setPlaybackParams(params);
                player.pause();
                break;
            case R.id.speed1:
                params.setSpeed(1.f);
                player.setPlaybackParams(params);
                player.pause();
                break;
            case R.id.speed1_5:
                params.setSpeed(1.5f);
                player.setPlaybackParams(params);
                player.pause();
                break;
            case R.id.speed2:
                params.setSpeed(2.f);
                player.setPlaybackParams(params);
                player.pause();
                break;
             /*-----------------------------------------------------------------------------------*/

            /*---------------------10秒進める、戻すボタンが押された時---------------------------------*/
            case R.id.sekip10:
                if(player.isPlaying()) {
                    //現在の再生位置を取得し、10000ms(10秒)進める
                    Integer now = player.getCurrentPosition();
                    Integer seekTime = now + 10000;

                    //曲の時間よりも長くなった場合、一番最後まで進める
                    if(seekTime > duration)
                        seekTime = duration;

                    player.seekTo(seekTime);
                }
                break;
            case R.id.back10:
                if(player.isPlaying()) {
                    //現在の再生位置を取得し、10000ms(10秒)戻す
                    Integer now = player.getCurrentPosition();
                    Integer seekTime = now - 10000;

                    //０よりも小さくなった場合、0に戻す
                    if(seekTime < 0)
                        seekTime = 0;

                    player.seekTo(seekTime);
                }
                break;
            /*------------------------------------------------------------------------------------*/

            //「→→」ボタン
            case R.id.button_next:
                //音楽を止め、musicNumberを次の曲に設定
                //再生位置を0に戻す
                player.stop();
                musicNumber = (musicNumber+1) % musics.length ;
                onResume();
                break;
            //「←←」ボタン
            case R.id.button_back:
                now = 0;
                player.seekTo(now);
                break;
        }
    }

    //曲を読み込み、画面に表示される曲タイトル、アイコン画像を変更する
    public void setMusic(){
        //曲を取得
        player = MediaPlayer.create(this, musics[musicNumber].id);
        //曲のタイトルを設定
        title.setText(musics[musicNumber].title);
        //曲の長さを設定
        musicLength.setText(presentTimeFormat(player.getDuration()));
        //曲のアイコンを設定
        imageView.setImageResource(musics[musicNumber].image);
    }

    //単位がmsのdurationを「〇：〇〇」という文字列に変換する
    public String presentTimeFormat(Integer duration) {
        //分単位の取得 60000ms = 60s
        Integer minutes = duration / 60000;
        //秒単位の取得 1000ms = 1s
        Integer seconds = (duration % 60000) / 1000;
        String progress =  minutes + ":" + String.format("%02d",seconds);
        return progress;
    }

    //中央の再生ボタンの画像を曲の再生状態に応じて変更
    public void changeImageButton(){
        if(player.isPlaying())
            music_playback.setImageResource(R.drawable.ic_baseline_pause_24);
        else if (!player.isPlaying())
            music_playback.setImageResource(R.drawable.ic_baseline_play_arrow_24);
    }
}