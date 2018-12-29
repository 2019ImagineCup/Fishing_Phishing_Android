package ensharp.imagincup2019.fishingphishing.UI.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import ensharp.imagincup2019.fishingphishing.Database.DatabaseManager;
import ensharp.imagincup2019.fishingphishing.Database.Model.HistoryItem;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.GlideApp;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.CallActivity;
import ensharp.imagincup2019.fishingphishing.UI.CallWaitingActivity;

@SuppressLint("ValidFragment")
public class CallFragment extends Fragment {

    private static final int REQUEST_CALL_INFORMATION = 0;

    private View view;

    private RecentsFragment recentsFragment;

    private ImageButton[] numberButtons;

    @BindView(R.id.phone_number) TextView phoneNumber;
    @BindView(R.id.delete) ImageButton delete;
    @BindView(R.id.star) ImageButton star;
    @BindView(R.id.sharp) ImageButton sharp;
    @BindView(R.id.call) ImageButton call;
    @BindView(R.id.scam) ImageButton scam;

    @SuppressLint("ValidFragment")
    public CallFragment(RecentsFragment recentsFragment) {
        this.recentsFragment = recentsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_call, container, false);
        ButterKnife.bind(this, view);

        initializeNumbers();
        initializeSymbols();
        initializeCallSymbols();
        initializeDeleteButton();

        phoneNumber.addTextChangedListener(numberTextWatcher);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        delete.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CALL_INFORMATION && resultCode == Activity.RESULT_OK) {
            HistoryItem callHistory = new HistoryItem();
            String number = data.getStringExtra("number");
            String duration = data.getStringExtra("duration");
            String date = data.getStringExtra("date");

            callHistory.number = number;
            callHistory.duration = getDuration(duration);
            callHistory.date = date;

            recentsFragment.addCallHistory(callHistory);

            phoneNumber.setText("");
        }
    }

    private long getDuration(String duration) {
        long minute = Long.parseLong(duration.substring(0, duration.indexOf(":")));
        long second = Long.parseLong(duration.substring(duration.indexOf(":") + 1));

        return minute * 60 + second;
    }

    private View.OnTouchListener onCallTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                switch (v.getId()) {
                    case R.id.scam:
                        GlideApp.with(getContext()).load(R.drawable.item_scam_clicked).into(scam);
                        break;
                    case R.id.call:
                        GlideApp.with(getContext()).load(R.drawable.item_get_clicked).into(call);
                        break;
                }
            }
            if(event.getAction() == MotionEvent.ACTION_UP){
                Intent intent;
                switch (v.getId()) {
                    case R.id.scam:
                        GlideApp.with(getContext()).load(R.drawable.item_scam).into(scam);
                        intent = new Intent(getActivity(), CallWaitingActivity.class);
                        break;
                    case R.id.call:
                        GlideApp.with(getContext()).load(R.drawable.item_get).into(call);

                        if (phoneNumber.getText().length() == 0) {
                            if (DatabaseManager.getItems().size() == 0) {
                                return false;
                            }

                            phoneNumber.setText(DatabaseManager.getItems().get(0).number);
                            return false;
                        }

                        intent = new Intent(getActivity(), CallActivity.class);
                        intent.putExtra("number", phoneNumber.getText().toString());
                        break;
                    default:
                        intent = new Intent();
                        break;
                }
                phoneNumber.setText("");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, REQUEST_CALL_INFORMATION);
            }
            return false;
        }
    };

    private View.OnTouchListener onDeleteTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                GlideApp.with(getContext()).load(R.drawable.item_delete_clicked).into(delete);
            }
            if(event.getAction() == MotionEvent.ACTION_UP){
                GlideApp.with(getContext()).load(R.drawable.item_delete).into(delete);

                if (phoneNumber.getText().length() == 0)
                    return false;

                String number = phoneNumber.getText().toString().substring(0, phoneNumber.getText().length() - 1);
                phoneNumber.setText(getNumberText(number));
            }
            return false;
        }
    };

    private String getNumberText(String number) {
        number = number.replace("-", "");
        StringBuilder stringBuilder = new StringBuilder(number);

        return stringBuilder.toString();
    }

    private void initializeNumbers() {
        numberButtons = new ImageButton[] {
                view.findViewById(R.id.zero),
                view.findViewById(R.id.one), view.findViewById(R.id.two), view.findViewById(R.id.three),
                view.findViewById(R.id.four), view.findViewById(R.id.five), view.findViewById(R.id.six),
                view.findViewById(R.id.seven), view.findViewById(R.id.eight), view.findViewById(R.id.nine)
        };

        GlideApp.with(this).load(R.drawable.item_0).into(numberButtons[0]);
        GlideApp.with(this).load(R.drawable.item_1).into(numberButtons[1]);
        GlideApp.with(this).load(R.drawable.item_2).into(numberButtons[2]);
        GlideApp.with(this).load(R.drawable.item_3).into(numberButtons[3]);
        GlideApp.with(this).load(R.drawable.item_4).into(numberButtons[4]);
        GlideApp.with(this).load(R.drawable.item_5).into(numberButtons[5]);
        GlideApp.with(this).load(R.drawable.item_6).into(numberButtons[6]);
        GlideApp.with(this).load(R.drawable.item_7).into(numberButtons[7]);
        GlideApp.with(this).load(R.drawable.item_8).into(numberButtons[8]);
        GlideApp.with(this).load(R.drawable.item_9).into(numberButtons[9]);

        for (int i = 0; i < numberButtons.length; i++) {
            final int position = i;
            numberButtons[i].setOnTouchListener(new View.OnTouchListener() {
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
                        GlideApp.with(getView()).load(clickedImages[position]).into((ImageButton) view);
                    }
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        String number = phoneNumber.getText() + String.valueOf(position);
                        phoneNumber.setText(getNumberText(number));
                        GlideApp.with(getView()).load(normalImages[position]).into((ImageButton) view);
                    }
                    return false;
                }
            });
        }
    }

    private void initializeSymbols() {
        GlideApp.with(this).load(R.drawable.item_star).into(star);
        GlideApp.with(this).load(R.drawable.item_sharp).into(sharp);
    }

    private void initializeCallSymbols() {
        GlideApp.with(this).load(R.drawable.item_scam).into(scam);
        GlideApp.with(this).load(R.drawable.item_get).into(call);

        scam.setOnTouchListener(onCallTouchListener);
        call.setOnTouchListener(onCallTouchListener);
    }

    private void initializeDeleteButton() {
        GlideApp.with(this).load(R.drawable.item_delete).into(delete);

        delete.setOnTouchListener(onDeleteTouchListener);
    }

    private TextWatcher numberTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                delete.setVisibility(View.INVISIBLE);
            } else {
                delete.setVisibility(View.VISIBLE);
            }
        }
    };
}
