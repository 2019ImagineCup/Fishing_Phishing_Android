package ensharp.imagincup2019.fishingphishing.UI.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import ensharp.imagincup2019.fishingphishing.Common.Model.CallHistory;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.CallActivity;
import ensharp.imagincup2019.fishingphishing.UI.CallWaitingActivity;

@SuppressLint("ValidFragment")
public class CallFragment extends Fragment {

    private static final int REQUEST_CALL_INFORMATION = 0;

    private RecentsFragment recentsFragment;

    private TextView phoneNumber;
    ImageButton delete;

    @SuppressLint("ValidFragment")
    public CallFragment(RecentsFragment recentsFragment) {
        this.recentsFragment = recentsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, container, false);

        phoneNumber = view.findViewById(R.id.phone_number);
        delete = view.findViewById(R.id.delete);

        ImageButton one = view.findViewById(R.id.one);
        ImageButton two = view.findViewById(R.id.two);
        ImageButton three = view.findViewById(R.id.three);
        ImageButton four = view.findViewById(R.id.four);
        ImageButton five = view.findViewById(R.id.five);
        ImageButton six = view.findViewById(R.id.six);
        ImageButton seven = view.findViewById(R.id.seven);
        ImageButton eight = view.findViewById(R.id.eight);
        ImageButton nine = view.findViewById(R.id.nine);
        ImageButton zero = view.findViewById(R.id.zero);
        ImageButton star = view.findViewById(R.id.star);
        ImageButton sharp = view.findViewById(R.id.sharp);
        ImageButton call = view.findViewById(R.id.call);
        ImageButton scam = view.findViewById(R.id.scam);

        one.setOnTouchListener(onNumberTouchListener);
        two.setOnTouchListener(onNumberTouchListener);
        three.setOnTouchListener(onNumberTouchListener);
        four.setOnTouchListener(onNumberTouchListener);
        five.setOnTouchListener(onNumberTouchListener);
        six.setOnTouchListener(onNumberTouchListener);
        seven.setOnTouchListener(onNumberTouchListener);
        eight.setOnTouchListener(onNumberTouchListener);
        nine.setOnTouchListener(onNumberTouchListener);
        zero.setOnTouchListener(onNumberTouchListener);

//        star.setOnClickListener(onNumberClickListener);
//        sharp.setOnClickListener(onNumberClickListener);

        scam.setOnClickListener(onCallClickListener);
        call.setOnClickListener(onCallClickListener);

        delete.setOnClickListener(onDeleteClickListener);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CALL_INFORMATION && resultCode == Activity.RESULT_OK) {
            CallHistory callHistory = new CallHistory();
            String number = data.getStringExtra("number");
            String duration = data.getStringExtra("duration");
            String date = data.getStringExtra("date");

            callHistory.setPhoneNumber(number);
//            callHistory.setDuration(duration);
//            callHistory.setDate(date);

//            this.phoneNumber = phoneNumber;
//            this.callType = callType;
//            this.phoneType = phoneType;
//            this.date = date;
//            this.duration = duration;
            recentsFragment.addCallHistory(callHistory);
        }
    }

    private String getNumberText(String number) {
        number = number.replace("-", "");
        StringBuilder stringBuilder = new StringBuilder(number);

        return stringBuilder.toString();
    }

    private View.OnTouchListener onNumberTouchListener = new View.OnTouchListener() {
        int[] normalImages = { R.drawable.item_0, R.drawable.item_1, R.drawable.item_2, R.drawable.item_3,
                                R.drawable.item_4, R.drawable.item_5, R.drawable.item_6, R.drawable.item_7,
                                R.drawable.item_8, R.drawable.item_9 };
        int[] clickedImages = { R.drawable.item_0_clicked, R.drawable.item_1_clicked, R.drawable.item_2_clicked,
                                R.drawable.item_3_clicked, R.drawable.item_4_clicked, R.drawable.item_5_clicked,
                                R.drawable.item_6_clicked, R.drawable.item_7_clicked, R.drawable.item_8_clicked,
                                R.drawable.item_9_clicked };
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                ((ImageButton) view).setImageResource(clickedImages[Integer.parseInt(view.getTag().toString())]);
            }
            if(event.getAction() == MotionEvent.ACTION_UP){
                String number = phoneNumber.getText() + view.getTag().toString();
                phoneNumber.setText(getNumberText(number));
                ((ImageButton) view).setImageResource(normalImages[Integer.parseInt(view.getTag().toString())]);

                if (phoneNumber.getText().length() == 1) {
                    delete.setVisibility(View.VISIBLE);
                }
            }

            return false;
        }
    };

    private View.OnClickListener onNumberClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String number = phoneNumber.getText() + view.getTag().toString();
            phoneNumber.setText(getNumberText(number));

            if (phoneNumber.getText().length() == 1) {
                delete.setVisibility(View.VISIBLE);
            }
        }
    };

    private View.OnClickListener onCallClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;

            switch (view.getTag().toString()) {
                case "scam":
                    intent = new Intent(getActivity(), CallWaitingActivity.class);
                    break;
                case "call":
                    intent = new Intent(getActivity(), CallActivity.class);
                    intent.putExtra("number", phoneNumber.getText().toString());
                    break;
                default:
                    intent = new Intent();
                    break;
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, REQUEST_CALL_INFORMATION);
        }
    };

    private View.OnClickListener onDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (phoneNumber.getText().length() == 0)
                return;

            String number = phoneNumber.getText().toString().substring(0, phoneNumber.getText().length() - 1);
            phoneNumber.setText(getNumberText(number));

            if (phoneNumber.getText().length() == 0) {
                delete.setVisibility(View.INVISIBLE);
            }
        }
    };
}
