package ensharp.imagincup2019.fishingphishing.Common;

import android.content.Context;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.Arrays;

import ensharp.imagincup2019.fishingphishing.Common.VO.AnalysisVO;
import ensharp.imagincup2019.fishingphishing.Common.VO.CallLogVO;

public class Constants {
    private static final Constants ourInstance = new Constants();

    public static Constants getInstance() {
        return ourInstance;
    }

    private ArrayList<CallLogVO> logs;

    private Constants() {

        logs = new ArrayList<>(
                Arrays.asList(new CallLogVO[] {
                        new CallLogVO("1234", "Cell Phone", "2018.11.5", "PM 9:29", "incoming", "23sec", "", new AnalysisVO()),
                        new CallLogVO("4321", "Cell Phone", "2018.11.5", "PM 9:11", "outgoing", "18min 3sec", "", new AnalysisVO()),
                        new CallLogVO("5678", "Cell Phone", "2018.11.5", "PM 7:21", "missed", "", "", new AnalysisVO()),
                        new CallLogVO("1234", "Cell Phone", "2018.11.5", "PM 7:21", "incoming", "19sec", "", new AnalysisVO()),
                        new CallLogVO("1234", "Cell Phone", "2018.11.5", "PM 6:58", "missed", "", "", new AnalysisVO()),
                        new CallLogVO("1234", "Cell Phone", "2018.11.4", "AM 11:09", "incoming", "10min 1sec", "", new AnalysisVO()),
                        new CallLogVO("4321", "Cell Phone", "2018.11.5", "PM 6:00", "incoming", "10min 1sec", "", new AnalysisVO()),
                        new CallLogVO("1234", "Cell Phone", "2018.11.4", "AM 4:19", "missed", "", "", new AnalysisVO()),
                        new CallLogVO("1234", "Cell Phone", "2018.11.3", "PM 2:13", "outgoing", "21min 30sec", "", new AnalysisVO()),
                        new CallLogVO("4321", "Cell Phone", "2018.11.3", "AM 9:11", "outgoing", "8min 24sec", "", new AnalysisVO()),
                        new CallLogVO("5678", "Cell Phone", "2018.11.2", "PM 5:24", "outgoing", "6min", "", new AnalysisVO()),
                        new CallLogVO("5678", "Cell Phone", "2018.11.2", "PM 3:21", "incoming", "11min 1sec", "", new AnalysisVO()),
                        new CallLogVO("4321", "Cell Phone", "2018.11.2", "AM 11:39", "outgoing", "9sec", "", new AnalysisVO()),
                        new CallLogVO("1234", "Cell Phone", "2018.11.2", "AM 11:34", "incoming", "6sec", "", new AnalysisVO())
                })
        );
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

    public static final int REQUEST_POST = 0 ;
    public static final int REQUEST_GET = 1;
}
