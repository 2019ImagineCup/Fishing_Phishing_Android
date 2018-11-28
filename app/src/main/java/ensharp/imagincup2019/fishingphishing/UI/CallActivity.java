package ensharp.imagincup2019.fishingphishing.UI;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ensharp.imagincup2019.fishingphishing.Call.MicrophoneStream;
import ensharp.imagincup2019.fishingphishing.Common.Server.NetworkTask;
import ensharp.imagincup2019.fishingphishing.R;

public class CallActivity extends AppCompatActivity {

    private static final String SpeechSubscriptionKey = "5d59cc81db784d00b1a5c830815404d4";
    private static final String SpeechRegion = "westus";

    private ImageButton recognizeContinuousButton;
    private TextView recognizedTextView;

    private SpeechConfig speechConfig;

    private static final String logTag = "reco 3";
    private boolean continuousListeningStarted = false;
    private SpeechRecognizer reco = null;
    private AudioConfig audioInput = null;
    private String buttonText = "";
    private ArrayList<String> content = new ArrayList<>();

    private MicrophoneStream microphoneStream;
    private MicrophoneStream createMicrophoneStream() {
        if (microphoneStream != null) {
            microphoneStream.close();
            microphoneStream = null;
        }

        microphoneStream = new MicrophoneStream();
        return microphoneStream;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        recognizeContinuousButton = findViewById(R.id.recognize_button);
        recognizeContinuousButton.setOnClickListener(onRecognizeButtonClickListener);
        recognizedTextView = findViewById(R.id.recognizedText);

        createConfig();
        clearTextBox();

        sendToServer("", 0);
        recognizeSpeech();
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

    private void sendToServer(String textToSend, int flag) {
        String url = "http://13.77.161.171:80/text";

        JSONObject jsonObject = send_Data("010-1234-1234", "010-5678-5678", textToSend, flag);
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

    @Override
    public void onBackPressed() {
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
                sendToServer(TextUtils.join(" ", content), 1);
            });

            final Future<Void> task = reco.startContinuousRecognitionAsync();
            setOnTaskCompletedListener(task, result -> {
                continuousListeningStarted = true;
                CallActivity.this.runOnUiThread(() -> {
                    recognizeContinuousButton.setImageResource(R.drawable.icon_call_clicked);
                    recognizeContinuousButton.setEnabled(true);
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
                            recognizeContinuousButton.setImageResource(R.drawable.icon_call_normal);
                            finish();
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
