package ensharp.imagincup2019.fishingphishing.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;

import ensharp.imagincup2019.fishingphishing.R;

public class TestFragment extends Fragment {

    private DatabaseReference database;

    private View view;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, container, false);


        database = FirebaseDatabase.getInstance().getReference();
        database.child("test").addChildEventListener(databaseEventListener);
        database.child("test").child("id").setValue("Hello, World!");
        textView = view.findViewById(R.id.test);

        return view;
    }

    private ChildEventListener databaseEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.e("change aaa",dataSnapshot.getKey());

            Long text = (Long) dataSnapshot.getValue();

            textView.setText(text.toString());
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
