package jp.classmethod.android.sample.playsound;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.JetPlayer;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

    private SoundPool mSoundPool;
    private int mSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.sound_pool).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playFromSoundPool();
            }
        });
        findViewById(R.id.media_player).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playFromMediaPlayer();
            }
        });
        findViewById(R.id.audio_track).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playFromAudioTrack();
            }
        });
        findViewById(R.id.jet_player).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playFromJetPlayer();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 予め音声データを読み込む
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundId = mSoundPool.load(getApplicationContext(), R.raw.cat, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // リリース
        mSoundPool.release();
    }

    private void playFromSoundPool() {
        mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
    }

    private void playFromMediaPlayer() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.strings);
        mediaPlayer.start();
    }

    private void playFromAudioTrack() {
        try {
            // 音データを読み込む
            InputStream is = getResources().openRawResource(R.raw.bird);
            byte[] byteData = new byte[(int) is.available()];
            is.read(byteData);
            is.close();
            // バッファサイズを取得
            int bufSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            // AudioTrackインスタンスを生成
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bufSize, AudioTrack.MODE_STREAM);
            // 再生
            audioTrack.play();
            audioTrack.write(byteData, 0, byteData.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void playFromJetPlayer() {
        // JetPlayerインスタンスを取得
        JetPlayer jetPlayer = JetPlayer.getJetPlayer();
        // 既存のキューをクリア
        jetPlayer.clearQueue();
        // JETファイルの読み込み
        jetPlayer.loadJetFile(getResources().openRawResourceFd(R.raw.level1));
        // 指定したセグメントの音データを再生
        byte segmentId = 0;
        jetPlayer.queueJetSegment(1, 0, 1, 0, 0, segmentId);
        jetPlayer.play();
    }

}
