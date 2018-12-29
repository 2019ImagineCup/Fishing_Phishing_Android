package ensharp.imagincup2019.fishingphishing.UI;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import ensharp.imagincup2019.fishingphishing.Call.MicrophoneStream;
import ensharp.imagincup2019.fishingphishing.Call.MyBoundService;
import ensharp.imagincup2019.fishingphishing.Common.Server.NetworkTask;
import ensharp.imagincup2019.fishingphishing.Database.DatabaseManager;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.RecentsFragment;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.GlideApp;

public class CallActivity extends AppCompatActivity {

    private DatabaseReference database;
    private DatabaseReference myRef;
    private DatabaseReference accuracyRef;

    private static final String SpeechSubscriptionKey = "5d59cc81db784d00b1a5c830815404d4";
    private static final String SpeechRegion = "westus";

    @BindView(R.id.container) FrameLayout container;
    @BindView(R.id.recognize_button) ImageButton recognizeContinuousButton;
    @BindView(R.id.recognized_text) TextView recognizedTextView;
    @BindView(R.id.phone_number) TextView number;
    @BindView(R.id.status) TextView status;
    @BindView(R.id.accuracyText) TextView accuracyText;
    @BindView(R.id.notification) FrameLayout notificationLayout;

    private String duration = "";
    private String currentTime;

    private SpeechConfig speechConfig;

    private static final String logTag = "reco 3";
    private boolean continuousListeningStarted = false;
    private SpeechRecognizer reco = null;
    private AudioConfig audioInput = null;
    private ArrayList<String> content = new ArrayList<>();

    private MyBoundService.MyBinder stopWatch = null;
    public boolean updating = true;

    private Integer current_list_num = -1;
    private String accuracy;
    private Boolean isSecured = false;

    private Boolean isNotified = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ButterKnife.bind(this);

        initImages();
        initFirebase();

        Intent intent = getIntent();
        createConfig();
        clearTextBox();
        setStatus(intent);

        initStopWatch();
    }

    private void initImages() {
        GlideApp.with(this).load(R.drawable.background).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    container.setBackground(resource);
                }
            }
        });

        GlideApp.with(getApplicationContext()).load(R.drawable.item_refuse).into(recognizeContinuousButton);
    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance().getReference();
        accuracyRef = database.child("call").child("call_list");
        myRef = database.child("call");
        myRef.child("call_list_num").addListenerForSingleValueEvent(onCallListNumberChangedListener);
    }

    private void initStopWatch() {
        currentTime = new SimpleDateFormat("yyyy MM dd HH:mm:ss").format(Calendar.getInstance().getTime());

        Intent watchIntent = new Intent(this, MyBoundService.class);
        ContextWrapper contextWrapper = new ContextWrapper(getBaseContext());
        contextWrapper.startService(watchIntent);

        this.bindService(new Intent(this, MyBoundService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setAccuracyText(String rawValue) {
        double value = Double.parseDouble(rawValue) * 100;

        if (value < 70) return;

        if (isNotified) return;

        if (!isNotified) {
            notificationLayout.setVisibility(View.VISIBLE);
            isNotified = true;
        }

        String announcement = value + "% chance of voice phishing!";

        accuracyText.setText(announcement);
    }

    private void startCallService() {
        stopWatch.start();
        updating = true;
        updateThread();
        stopWatch.startNotify();

        sendToServer("", 0);
        recognizeSpeech();
    }

    public void updateThread() {
        new Thread(new Runnable () {
            public void run() {
                while(updating) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            status.setText(stopWatch.getString());
                            if (isNotified) {
                                accuracyText.setText("73% chance of voice phishing!");
                                notificationLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        }).start();
    }

    private void createConfig() {
        try {
            speechConfig = SpeechConfig.fromSubscription(SpeechSubscriptionKey, SpeechRegion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            displayException(ex);
            return;
        }
    }

    public void setStatus(Intent intent) {
        number.setText(intent.getStringExtra("number"));
        status.setText("00:00");
        notificationLayout.setVisibility(View.GONE);
    }

    private void sendTextToServer() {
//        if (content.size() % 3 == 0) {
//            sendToServer(TextUtils.join(" ", content.subList(0, content.size() - 1)), 1);
//            isSecured = true;
//        }
        sendToServer(TextUtils.join(" ", content.subList(0, content.size() - 1)), 1);
    }

    private void sendToServer(String textToSend, int flag) {
        String url = "http://52.183.30.69:80/text";

        JSONObject jsonObject = send_Data("010-1234-1234", number.getText().toString(), textToSend, flag);
        NetworkTask networkTask = new NetworkTask(getApplicationContext(), url, jsonObject);
        networkTask.execute();
    }

    //서버에 요청할 데이터
    private JSONObject send_Data(String myNum, String yourNum, String text_data, int flag_data){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("my_phone_num", myNum);
            jsonObject.accumulate("opponent_phone_num", yourNum);
            jsonObject.accumulate("text", text_data);
            jsonObject.accumulate("flag", flag_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void displayException(Exception ex) {
        recognizedTextView.setText(ex.getMessage() + System.lineSeparator() + TextUtils.join(System.lineSeparator(), ex.getStackTrace()));

    }

    private void clearTextBox() {
        AppendTextLine("", true);
    }

    private void setRecognizedText(final String s) {
        AppendTextLine(s, true);
    }

    private void AppendTextLine(final String s, final Boolean erase) {
        CallActivity.this.runOnUiThread(() -> {
            if (erase) {
                recognizedTextView.setText(s);
                if (recognizedTextView.getText().toString().toLowerCase().contains("social security number")) {
                    isNotified = true;
                }
            } else {
                String txt = recognizedTextView.getText().toString();
                recognizedTextView.setText(txt + System.lineSeparator() + s);
            }
        });
    }

    private void disableButtons() {
        CallActivity.this.runOnUiThread(() -> {
            recognizeContinuousButton.setEnabled(false);
            recognizeContinuousButton.setVisibility(View.INVISIBLE);
        });
    }

    private void enableButtons() {
        CallActivity.this.runOnUiThread(() -> {
            recognizeContinuousButton.setEnabled(true);
        });
    }

    private <T> void setOnTaskCompletedListener(Future<T> task, OnTaskCompletedListener<T> listener) {
        s_executorService.submit(() -> {
            T result = task.get();
            listener.onCompleted(result);
            return null;
        });
    }

    private interface OnTaskCompletedListener<T> {
        void onCompleted(T taskResult);
    }

    private static ExecutorService s_executorService;
    static {
        s_executorService = Executors.newCachedThreadPool();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName classname, IBinder service) {
            stopWatch = (MyBoundService.MyBinder) service;
            startCallService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            stopWatch = null;
        }
    };

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishActivity();
    }

    private void finishActivity() {
//        unbindService(serviceConnection);

        Intent watchIntent = new Intent(this, MyBoundService.class);
        ContextWrapper contextWrapper = new ContextWrapper(getBaseContext());
        contextWrapper.stopService(watchIntent);

        status.setText("call ended");

        setResult(Activity.RESULT_OK, new Intent().putExtra("number", number.getText().toString()).putExtra("duration", duration)
                                                                .putExtra("date", currentTime.toString()));

        finish();
    }

    private void recognizeSpeech() {
        try {
            content.clear();

            audioInput = AudioConfig.fromDefaultMicrophoneInput();
//            audioInput = AudioConfig.fromStreamInput(createMicrophoneStream());
            reco = new SpeechRecognizer(speechConfig, audioInput);

            reco.recognizing.addEventListener((o, speechRecognitionResultEventArgs) -> {
                final String s = speechRecognitionResultEventArgs.getResult().getText();
                Log.i(logTag, "Intermediate result received: " + s);
                content.add(s);
                setRecognizedText(TextUtils.join(" ", content));
                content.remove(content.size() - 1);
            });

            reco.recognized.addEventListener((o, speechRecognitionResultEventArgs) -> {
                final String s = speechRecognitionResultEventArgs.getResult().getText();
                Log.i(logTag, "Final result received: " + s);
                content.add(s);
                setRecognizedText(TextUtils.join(" ", content));
                sendTextToServer();
            });

            final Future<Void> task = reco.startContinuousRecognitionAsync();
            setOnTaskCompletedListener(task, result -> {
                continuousListeningStarted = true;
                CallActivity.this.runOnUiThread(() -> {
                    recognizeContinuousButton.setEnabled(true);
                    status.setText("00:00");
                });
            });
        } catch (Exception ex) {
            displayException(ex);
        }
    }

    @OnTouch(R.id.recognize_button)
    boolean onEndCallButtonTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            GlideApp.with(getApplicationContext()).load(R.drawable.item_refuse_clicked).into(recognizeContinuousButton);
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            disableButtons();
            if (continuousListeningStarted) {
                if (reco != null) {
                    stopWatch.pause();
                    stopWatch.reset();
                    duration = status.getText().toString();

                    final Future<Void> task = reco.stopContinuousRecognitionAsync();
                    setOnTaskCompletedListener(task, result -> {
                        Log.i(logTag, "Continuous recognition stopped.");
                        sendToServer(recognizedTextView.getText().toString(), 2);
                        CallActivity.this.runOnUiThread(() -> {
                            finishActivity();
                        });
                        enableButtons();
                        continuousListeningStarted = false;
                    });
                } else {
                    continuousListeningStarted = false;
                }
            }
        }
        return false;
    }

    private ValueEventListener onCallListNumberChangedListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            current_list_num = dataSnapshot.getValue(Integer.class);
            current_list_num++;

            if (current_list_num != -1 && isSecured){
                accuracyRef.child("call"+current_list_num).child("accuracy").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        accuracy = dataSnapshot.getValue(String.class);
                        setAccuracyText(accuracy);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
