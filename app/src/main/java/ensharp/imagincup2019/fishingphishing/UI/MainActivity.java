package ensharp.imagincup2019.fishingphishing.UI;

import android.Manifest;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.client.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import ensharp.imagincup2019.fishingphishing.UI.Fragments.LogFragment;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.NumbersFragment;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.RecentsFragment;
import ensharp.imagincup2019.fishingphishing.R;

public class MainActivity extends AppCompatActivity {

//    FirebaseFirestore db;
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

//        db = FirebaseFirestore.getInstance();

        fragments = new Fragment[] {
                new RecentsFragment(), new NumbersFragment(), new LogFragment()
        };

        bottomButtons = new ImageButton[] {
                findViewById(R.id.recentsButton),
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
                Manifest.permission.WRITE_CONTACTS
        };

        ActivityCompat.requestPermissions(this, neededPermissions,0);
    }

    public void setBottomButtons(int position) {
        switch (position) {
            case 0:
                bottomButtons[0].setImageResource(R.drawable.icon_recents_clicked);
                bottomButtons[1].setImageResource(R.drawable.icon_numbers_normal);
                bottomButtons[2].setImageResource(R.drawable.icon_analytics_normal);
                break;
            case 1:
                bottomButtons[0].setImageResource(R.drawable.icon_recents_normal);
                bottomButtons[1].setImageResource(R.drawable.icon_numbers_clicked);
                bottomButtons[2].setImageResource(R.drawable.icon_analytics_normal);
                break;
            case 2:
                bottomButtons[0].setImageResource(R.drawable.icon_recents_normal);
                bottomButtons[1].setImageResource(R.drawable.icon_numbers_normal);
                bottomButtons[2].setImageResource(R.drawable.icon_analytics_clicked);
                break;

        }
    }
}
