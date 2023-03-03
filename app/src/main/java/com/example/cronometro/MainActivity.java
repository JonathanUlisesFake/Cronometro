package com.example.cronometro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView chronometer;
    Button playPause, stop, lap;
    TextView cont;
    ScrollView scrollView;
    LinearLayout lOut;

    private boolean isResume;
    private boolean isStarted;
    Handler handler = new Handler();
    long tMillisec = 0L, tStart = 0L, tBuff = 0L, tUpdate = 0L;

    int secs, mins, milliseconds;

    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {

            tMillisec = SystemClock.uptimeMillis() - tStart;
            tUpdate = tBuff + tMillisec;
            secs = (int) (tUpdate / 1000);
            mins = secs / 60;
            secs %= 60;
            milliseconds = (int) (tUpdate % 100);
            chronometer.setText("" + mins + ":" + String.format("%02d", secs) + ":" + String.format("%02d", milliseconds));
            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = (TextView) findViewById(R.id.crono);
        playPause = (Button) findViewById(R.id.btnPlayPause);
        stop = (Button) findViewById(R.id.btnStop);
        lap = (Button) findViewById(R.id.btnLap);
        cont = (TextView) findViewById(R.id.tvTime);
        scrollView = (ScrollView) findViewById(R.id.sv);
        lOut = (LinearLayout) findViewById(R.id.container);
        lap.setVisibility(View.INVISIBLE);

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isResume) {
                    if (!isStarted) {
                        lap.setVisibility(View.VISIBLE);
                        isStarted = true;
                    }
                    tStart = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimerThread, 0);
                    isResume = true;
                    playPause.setText("");
                    playPause.setText("Pause");

                } else {

                    tBuff += tMillisec;
                    handler.removeCallbacks(updateTimerThread);
                    isResume = false;
                    playPause.setText("");
                    playPause.setText("Start");
                }
            }
        });

        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View addView = inflater.inflate(R.layout.row, null);
                TextView txtValue = (TextView) addView.findViewById(R.id.txtContent);
                txtValue.setText(chronometer.getText());
                lOut.addView(addView);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tBuff += tMillisec;
                tUpdate = 0;
                tMillisec = 0;
                tStart = 0;
                tBuff = 0;
                mins = 0;
                secs = 0;
                milliseconds = 0;
                playPause.setText("");
                playPause.setText("Start");
                lap.setVisibility(View.INVISIBLE);
                handler.removeCallbacks(updateTimerThread);
                isResume = false;
                isStarted = false;
                lOut.removeAllViews();
            }
        });
    }
}
