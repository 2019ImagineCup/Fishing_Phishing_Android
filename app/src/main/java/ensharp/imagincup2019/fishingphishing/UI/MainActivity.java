package ensharp.imagincup2019.fishingphishing.UI;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.microsoft.cognitiveservices.speech.SpeechConfig;

import ensharp.imagincup2019.fishingphishing.Common.Constants;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.CallFragment;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.LogFragment;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.NumbersFragment;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.RecentsFragment;
import ensharp.imagincup2019.fishingphishing.R;

public class MainActivity extends AppCompatActivity {

    private ImageButton[] bottomButtons;
    private Fragment[] fragments;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragments = new Fragment[] {
                new RecentsFragment(), new CallFragment(), new NumbersFragment(), new LogFragment()
        };

        bottomButtons = new ImageButton[] {
                findViewById(R.id.recentsButton),
                findViewById(R.id.callButton),
                findViewById(R.id.numbersButton),
                findViewById(R.id.logButton)
        };

        for (int i = 0; i < fragments.length; i++) {
            final Fragment fragment = fragments[i];
            final int position = i;
            bottomButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
                    setBottomButtons(position);
                }
            });
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragments[0])
                .commit();

        String[] neededPermissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.RECORD_AUDIO
        };

        try {
            ActivityCompat.requestPermissions(this, neededPermissions,5);
            Log.e("SpeechSDK", "init sdk");
        } catch (Exception ex) {
            Log.e("SpeechSDK", "could not init sdk, " + ex.toString());
        }


    }

    public void setBottomButtons(int position) {
        switch (position) {
            case 0:
                bottomButtons[0].setImageResource(R.drawable.icon_recents_clicked);
                bottomButtons[1].setImageResource(R.drawable.icon_call_normal);
                bottomButtons[2].setImageResource(R.drawable.icon_numbers_normal);
                bottomButtons[3].setImageResource(R.drawable.icon_analytics_normal);
                break;
            case 1:
                bottomButtons[0].setImageResource(R.drawable.icon_recents_normal);
                bottomButtons[1].setImageResource(R.drawable.icon_call_clicked);
                bottomButtons[2].setImageResource(R.drawable.icon_numbers_normal);
                bottomButtons[3].setImageResource(R.drawable.icon_analytics_normal);
                break;
            case 2:
                bottomButtons[0].setImageResource(R.drawable.icon_recents_normal);
                bottomButtons[1].setImageResource(R.drawable.icon_call_normal);
                bottomButtons[2].setImageResource(R.drawable.icon_numbers_clicked);
                bottomButtons[3].setImageResource(R.drawable.icon_analytics_normal);
                break;
            case 3:
                bottomButtons[0].setImageResource(R.drawable.icon_recents_normal);
                bottomButtons[1].setImageResource(R.drawable.icon_call_normal);
                bottomButtons[2].setImageResource(R.drawable.icon_numbers_normal);
                bottomButtons[3].setImageResource(R.drawable.icon_analytics_clicked);
                break;

        }
    }
}
