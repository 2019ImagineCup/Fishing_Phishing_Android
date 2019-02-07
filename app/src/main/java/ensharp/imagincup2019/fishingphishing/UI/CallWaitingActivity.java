package ensharp.imagincup2019.fishingphishing.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.util.Random;

import ensharp.imagincup2019.fishingphishing.Common.Constants;
import ensharp.imagincup2019.fishingphishing.Database.Model.HistoryItem;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.GlideApp;

public class CallWaitingActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_INFORMATION = 0;

    private TextView phoneNumber;

    private String numberToReturn;
    private String durationToReturn;
    private String dateToReturn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callwaiting);

        FutureTarget<Bitmap> futureTarget =
                Glide.with(this)
                .asBitmap()
                .load(R.drawable.background)
                .submit(100,100);

        phoneNumber = findViewById(R.id.phone_number);
        generateNumber();

        ImageButton declineButton = findViewById(R.id.decline_call);

        GlideApp.with(this).load(R.drawable.item_refuse).into(declineButton);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNumber();
            }
        });


        ImageButton acceptButton = findViewById(R.id.get_call);
        GlideApp.with(this).load(R.drawable.item_get).into(acceptButton);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CallWaitingActivity.this, CallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("number", phoneNumber.getText().toString());
                Constants.call_type = "incoming";
                startActivityForResult(intent, REQUEST_CALL_INFORMATION);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CALL_INFORMATION && resultCode == Activity.RESULT_OK) {
            numberToReturn = data.getStringExtra("number").replace("-", "");
            durationToReturn = data.getStringExtra("duration");
            dateToReturn = data.getStringExtra("date");

            finishActivity();
        }
    }

    private void finishActivity() {
        setResult(Activity.RESULT_OK, new Intent().putExtra("number", numberToReturn).putExtra("duration", durationToReturn)
                .putExtra("date", dateToReturn));

        finish();
    }

    private long getDuration(String duration) {
        long minute = Long.parseLong(duration.substring(0, duration.indexOf(":")));
        long second = Long.parseLong(duration.substring(duration.indexOf(":") + 1));

        return minute * 60 + second;
    }

    public void generateNumber() {
        String number = "";

        Random random = new Random();
        number = "010-" + random.nextInt(10000) + "-" + random.nextInt(10000);
        if (number.length() != 13) {
            generateNumber();
        } else if (!phoneNumber.getText().equals(number)) {
            phoneNumber.setText(number);
        } else {
            generateNumber();
        }
    }
}
