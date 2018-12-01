package ensharp.imagincup2019.fishingphishing.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

import ensharp.imagincup2019.fishingphishing.R;

public class CallWaitingActivity extends AppCompatActivity {

    private TextView phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callwaiting);

        phoneNumber = findViewById(R.id.phone_number);
        generateNumber();

        ImageButton declineButton = findViewById(R.id.decline_call);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNumber();
            }
        });

        ImageButton acceptButton = findViewById(R.id.get_call);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CallWaitingActivity.this, CallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("number", phoneNumber.getText().toString());
                startActivity(intent);
            }
        });
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
