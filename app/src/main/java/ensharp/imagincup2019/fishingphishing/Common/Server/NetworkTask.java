package ensharp.imagincup2019.fishingphishing.Common.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SegmentTabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import ensharp.imagincup2019.fishingphishing.Common.Constants;
import ensharp.imagincup2019.fishingphishing.Common.VO.CallLogVO;
import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.Fragments.LogFragment;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.AnalysisAdapter;
import ensharp.imagincup2019.fishingphishing.UI.UIElements.StickyAdapter;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    private String url;
    private JSONObject data;
    private Context context;
    private int requestMethod;
    private int ACTION;

    private AnalysisAdapter listViewAdapter;
    private StickyAdapter stickyAdapter;
    private RecyclerView list;
    private ExpandableStickyListHeadersListView stickyList;
    Constants constants;
    private ArrayList<CallLogVO> logList = new ArrayList<>();
    private SegmentTabLayout tabLayout;
    private LogFragment logFragment;


    @BindView(R.id.accuracyText)
    TextView accuracyText;
    @BindView(R.id.notification)
    FrameLayout notificationLayout;

    //POST 형식
    public NetworkTask(Context _context, String url, JSONObject data, int _requestMethod, int _ACTION,TextView _accuracyText,FrameLayout _notificationLayout) {
        this.context = _context;
        this.url = url;
        this.data = data;
        this.requestMethod = _requestMethod;
        this.ACTION = _ACTION;

        this.accuracyText = _accuracyText;
        this.notificationLayout = _notificationLayout;
    }

    //LogFragment
    public NetworkTask(Context _context, String url, JSONObject data, int _requestMethod, int _ACTION, AnalysisAdapter _listViewAdapter, StickyAdapter _stickyAdapter,RecyclerView _list,
                       ExpandableStickyListHeadersListView _stickyList, SegmentTabLayout _tabLayout, LogFragment _logFragment) {
        this.context = _context;
        this.url = url;
        this.data = data;
        this.requestMethod = _requestMethod;
        this.ACTION = _ACTION;

        this.listViewAdapter = _listViewAdapter;
        this.stickyAdapter = _stickyAdapter;
        this.list = _list;
        this. stickyList = _stickyList;
        constants = Constants.getInstance();
        this.tabLayout = _tabLayout;
        this.logFragment = _logFragment;

    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        RequestServer requestServer = new RequestServer(url,data);

        // POST 요청 방식
        if(requestMethod == Constants.REQUEST_POST){
            result = requestServer.request_POST();
        } else {    // GET 요청 방식
            result = requestServer.request_GET();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {


        //result 값이 null 일 때
        if (result == null) {
            Toast.makeText(context, "Check Network Status", Toast.LENGTH_LONG).show();
            return;
        }

        switch (ACTION){
            case Constants.SEND_TEXT:
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String real_result = jsonObject.getString("result");
                    if(!real_result.equals("fail")) {
                        if(!real_result.equals("success")) {
                            Constants.id = real_result;
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                            database.child("id").child("id"+real_result).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.e("accuracy",String.valueOf(dataSnapshot.getValue()));
                                    HashMap<String,Long> hash = (HashMap<String, Long>) dataSnapshot.getValue();
                                    setAccuracyText(String.valueOf(hash.get("accuracy")));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        Toast.makeText(context,"Success for send Message",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Fail for send Message",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("Error",e.toString());
                    e.printStackTrace();
                }
                break;
            case Constants.GET_LOG_LIST:
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String resultResponse = jsonObject.getString("result");
                    JSONArray resultObjectArray = new JSONArray(resultResponse);
                    Log.e("resultResponse",resultResponse);
                    ArrayList<CallLogVO> logList = new ArrayList<>();
                    if(!resultResponse.equals("fail")){
                        JSONObject tempObject;
                        for (int i=0; i < resultObjectArray.length(); i++){
                            tempObject = resultObjectArray.getJSONObject(i);
                            String my_phone_num = tempObject.getString("my_phone_num");
                            String opponent_phone_num = tempObject.getString("opponent_phone_num");
                            String accuracy = tempObject.getString("accuracy");
                            String text = tempObject.getString("text");
                            String type = tempObject.getString("type");
                            String date = tempObject.getString("date");
                            String period = tempObject.getString("period");
                            CallLogVO callLogVO = new CallLogVO(opponent_phone_num,type,date,date,period,type);
                            logList.add(callLogVO);
                        }

                        constants.setLogs(logList);
                        setLogList(tabLayout.getCurrentTab());

                    }
                } catch (JSONException e) {
                    Log.e("Error",e.toString());
                    e.printStackTrace();
                }
                break;
        }
    }

    private void setLogList(int position) {
        switch (position) {
            case 0:
                logList.clear();
                logList.addAll(constants.getLogs());
                setListViewAdapter(logList);
                break;
            case 1:
                ArrayList<CallLogVO> listToCopy = new ArrayList<>();
                listToCopy.addAll(constants.getLogs());
                logList.clear();
                logList.addAll(organizeLogList(0, listToCopy));
                setStickyListAdapter(logList);
                break;
        }
    }

    public void setListViewAdapter(List<CallLogVO> logList) {
        listViewAdapter = new AnalysisAdapter(logList, logFragment);
        list.setAdapter(listViewAdapter);
        list.setVisibility(View.VISIBLE);
        stickyList.setVisibility(View.GONE);
    }

    public void setStickyListAdapter(List<CallLogVO> logList) {
        stickyAdapter = new StickyAdapter(context, logFragment, logList);
        stickyList.setAdapter(stickyAdapter);
        stickyList.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);
    }

    private ArrayList<CallLogVO> organizeLogList(int round, ArrayList<CallLogVO> logList) {
        if (round == logList.size()) return logList;

        int count = 1;
        CallLogVO newLog;
        ArrayList<Integer> indexList = new ArrayList<>();
        ArrayList<CallLogVO> listToCopy = new ArrayList<>();

        logList.get(round).setTag(round);
        for (int i = logList.size() - 1; i > round; i--) {
            if (logList.get(i).getPhoneNumber().equals(logList.get(round).getPhoneNumber())) {
                count++;
                indexList.add(i);
                newLog = new CallLogVO(logList.get(i));
                newLog.setTag(round);
                listToCopy.add(newLog);
            }
        }

        for (int i = 0; i < indexList.size(); i++) {
            logList.remove(Integer.parseInt(String.valueOf(indexList.get(i))));
        }

        for (int i = listToCopy.size() - 1, j = round + 1; i >= 0; i--, j++) {
            logList.add(j, listToCopy.get(i));
        }

        round += count;
        return organizeLogList(round, logList);
    }

    private void setAccuracyText(String rawValue) {
        double value = Double.parseDouble(rawValue) * 100;

        Log.e("value",String.valueOf(value));

        if (value < 70)
            return;
        else {
            notificationLayout.setVisibility(View.VISIBLE);
            alarm_vibrator();
        }

        String announcement = value + "% chance of voice phishing!";

        accuracyText.setText(announcement);
    }

    //진동
    private void alarm_vibrator(){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }
}





