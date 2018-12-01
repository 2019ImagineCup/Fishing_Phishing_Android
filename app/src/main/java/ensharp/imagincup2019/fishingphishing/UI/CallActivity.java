package ensharp.imagincup2019.fishingphishing.UI;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ensharp.imagincup2019.fishingphishing.Call.MicrophoneStream;
import ensharp.imagincup2019.fishingphishing.Call.MyBoundService;
import ensharp.imagincup2019.fishingphishing.Common.Server.NetworkTask;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.RecentsFragment;

public class CallActivity extends AppCompatActivity {

    private static final String SpeechSubscriptionKey = "5d59cc81db784d00b1a5c830815404d4";
    private static final String SpeechRegion = "westus";

    private ImageButton recognizeContinuousButton;
    private TextView recognizedTextView;
    private TextView number;
    private TextView status;
    private String duration = "";
    private Date currentTime;

    private SpeechConfig speechConfig;

    private static final String logTag = "reco 3";
    private boolean continuousListeningStarted = false;
    private SpeechRecognizer reco = null;
    private AudioConfig audioInput = null;
    private ArrayList<String> content = new ArrayList<>();

    private MyBoundService.MyBinder stopWatch = null;
    public boolean updating = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        Intent intent = getIntent();

        recognizeContinuousButton = findViewById(R.id.recognize_button);
        recognizeContinuousButton.setOnClickListener(onRecognizeButtonClickListener);
        recognizedTextView = findViewById(R.id.recognized_text);
        number = findViewById(R.id.phone_number);
        status = findViewById(R.id.status);

        createConfig();
        clearTextBox();
        setStatus(intent);

        currentTime = Calendar.getInstance().getTime();

        Intent watchIntent = new Intent(this, MyBoundService.class);
        ContextWrapper contextWrapper = new ContextWrapper(getBaseContext());
        contextWrapper.startService(watchIntent);

        this.bindService(new Intent(this, MyBoundService.class), serviceConnection, Context.BIND_AUTO_CREATE);
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
    }

    private void sendTextToServer() {
        if (content.size() % 3 == 0) {
            sendToServer(TextUtils.join(" ", content.subList(content.size() - 3, content.size() - 1)), 1);
        }
    }

    private void sendToServer(String textToSend, int flag) {
//        String url = "http://13.77.161.171:80/text";
//
//        JSONObject jsonObject = send_Data("010-1234-1234", "010-5678-5678", textToSend, flag);
//        NetworkTask networkTask = new NetworkTask(getApplicationContext(), url, jsonObject);
//        networkTask.execute();
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
            } else {
                String txt = recognizedTextView.getText().toString();
                recognizedTextView.setText(txt + System.lineSeparator() + s);
            }
        });
    }

    private void disableButtons() {
        CallActivity.this.runOnUiThread(() -> {
            recognizeContinuousButton.setEnabled(false);
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
    protected void onDestroy() {
        super.onDestroy();
        finishActivity();
    }

    private void finishActivity() {
//        unbindService(serviceConnection);

        Intent watchIntent = new Intent(this, MyBoundService.class);
        ContextWrapper contextWrapper = new ContextWrapper(getBaseContext());
        contextWrapper.stopService(watchIntent);

        setResult(Activity.RESULT_OK, new Intent().putExtra("number", number.getText().toString()).putExtra("duration", duration)
                                                                .putExtra("date", currentTime));

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

    private View.OnClickListener onRecognizeButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Button clickedButton = (Button) view;
            disableButtons();
            if (continuousListeningStarted) {
                if (reco != null) {
                    final Future<Void> task = reco.stopContinuousRecognitionAsync();
                    setOnTaskCompletedListener(task, result -> {
                        Log.i(logTag, "Continuous recognition stopped.");
                        sendToServer(recognizedTextView.getText().toString(), 2);
                        CallActivity.this.runOnUiThread(() -> {
                            duration = status.getText().toString();
                            status.setText("call ended");
                            stopWatch.reset();
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
    };
}
