package ensharp.imagincup2019.fishingphishing.UI;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import ensharp.imagincup2019.fishingphishing.Database.Model.HistoryItem;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.CallFragment;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.LogFragment;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.RecentsFragment;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.TestFragment;

public class MainActivity extends AppCompatActivity {

    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    private DatabaseReference database;

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

//        database = FirebaseDatabase.getInstance().getReference();
//        database.child("call").child("call_list").addChildEventListener(databaseEventListener);

        RecentsFragment recentsFragment = new RecentsFragment();
        CallFragment callFragment = new CallFragment(recentsFragment);
        LogFragment logFragment = new LogFragment();

        fragments = new Fragment[] {
                recentsFragment, callFragment, logFragment
        };

        bottomButtons = new ImageButton[] {
                findViewById(R.id.recentsButton),
                findViewById(R.id.callButton),
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
                bottomButtons[2].setImageResource(R.drawable.icon_analytics_normal);
                break;
            case 1:
                bottomButtons[0].setImageResource(R.drawable.icon_recents_normal);
                bottomButtons[1].setImageResource(R.drawable.icon_call_clicked);
                bottomButtons[2].setImageResource(R.drawable.icon_analytics_normal);
                break;
            case 2:
                bottomButtons[0].setImageResource(R.drawable.icon_recents_normal);
                bottomButtons[1].setImageResource(R.drawable.icon_call_normal);
                bottomButtons[2].setImageResource(R.drawable.icon_analytics_clicked);
                break;

        }
    }

    private ChildEventListener databaseEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.e("Data",dataSnapshot.getValue().toString());
            HashMap<String, Object> hash = (HashMap) dataSnapshot.getValue();
            Log.e("my_phone_num",hash.get("my_phone_num").toString());
            Log.e("opponent_phone_num",hash.get("opponent_phone_num").toString());
            Log.e("text",hash.get("text").toString());
            Log.e("flag",hash.get("flag").toString());
            Log.e("accuracy",hash.get("accuracy").toString());
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.e("change",dataSnapshot.getKey());
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            Log.e("remove",dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.e("move",dataSnapshot.getKey());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
