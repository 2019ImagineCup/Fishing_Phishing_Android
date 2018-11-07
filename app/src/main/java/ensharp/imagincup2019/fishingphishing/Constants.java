package ensharp.imagincup2019.fishingphishing;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
    private static final Constants ourInstance = new Constants();

    public static Constants getInstance() {
        return ourInstance;
    }

    private ArrayList<RecentCallVO> recentCalls;
    private ArrayList<String> numbers;

    private Constants() {
        recentCalls = new ArrayList<>(
                Arrays.asList(new RecentCallVO[] {
                        new RecentCallVO("En# 15기 남민수", "휴대전화", "오후 9:29"),
                        new RecentCallVO("서성범 군대", "휴대전화", "오후 9:11"),
                        new RecentCallVO("En# 16기 김태석", "휴대전화", "오후 7:21"),
                        new RecentCallVO("En# 15기 남민수", "휴대전화", "오후 7:21"),
                        new RecentCallVO("En# 15기 남민수", "휴대전화", "오후 6:58"),
                        new RecentCallVO("En# 16기 김태석", "휴대전화", "오후 6:00")
                })
        );

        numbers = new ArrayList<>(
                Arrays.asList(new String[] {
                    "010-1111-1111",
                    "010-2222-2222",
                    "010-3333-3333"
                })
        );
    }

    public void deleteRecentCall(int position) {
        recentCalls.remove(position);
    }

    public ArrayList<RecentCallVO> getRecentCalls() {
        return recentCalls;
    }

    public void deleteNumber(int position) {
        numbers.remove(position);
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }
}
