package ensharp.imagincup2019.fishingphishing.Common;

import android.content.Context;
import android.provider.Settings;
import android.telecom.Call;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import ensharp.imagincup2019.fishingphishing.Common.Model.CallHistory;
import ensharp.imagincup2019.fishingphishing.Common.VO.AnalysisVO;
import ensharp.imagincup2019.fishingphishing.Common.VO.CallLogVO;

public class Constants {
    private static final Constants ourInstance = new Constants();

    public static Constants getInstance() {
        return ourInstance;
    }

    private ArrayList<String> numbers;
    private ArrayList<CallLogVO> logs;

    private Constants() {

        numbers = new ArrayList<>(
                Arrays.asList(new String[] {
                    "010-1111-1111",
                    "010-2222-2222",
                    "010-3333-3333"
                })
        );

        logs = new ArrayList<>(
                Arrays.asList(new CallLogVO[] {
                        new CallLogVO("김예진", "휴대전화", "2018년 11월 5일", "오후 9:29", "착신 통화", "23초", "", new AnalysisVO()),
                        new CallLogVO("전세영", "휴대전화", "2018년 11월 5일", "오후 9:11", "발신 통화", "18분 3초", "", new AnalysisVO()),
                        new CallLogVO("이다인", "휴대전화", "2018년 11월 5일", "오후 7:21", "부재중 전화", "", "", new AnalysisVO()),
                        new CallLogVO("김예진", "휴대전화", "2018년 11월 5일", "오후 7:21", "착신 통화", "19초", "", new AnalysisVO()),
                        new CallLogVO("김예진", "휴대전화", "2018년 11월 5일", "오후 6:58", "부재중 전화", "", "", new AnalysisVO()),
                        new CallLogVO("전세영", "휴대전화", "2018년 11월 5일", "오후 6:00", "착신 통화", "10분 1초", "", new AnalysisVO())
                })
        );
    }

    public void deleteNumber(int position) {
        numbers.remove(position);
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public ArrayList<CallLogVO> getLogs() {
        return logs;
    }

    public void deleteLog(int position) {
        logs.remove(position);
    }

    public String getAndroid_id(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
