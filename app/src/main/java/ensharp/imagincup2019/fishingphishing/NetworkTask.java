package ensharp.imagincup2019.fishingphishing;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import ensharp.imagincup2019.fishingphishing.Common.Constants;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    private String url;
    private JSONObject data;
    private Context context;
    private int requestMethod;

    public NetworkTask(Context _context, String url, JSONObject data, int _requestMethod) {
        this.context = _context;
        this.url = url;
        this.data = data;
        this.requestMethod = _requestMethod;
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
            Toast.makeText(context, "인터넷 연결을 확인하세요", Toast.LENGTH_LONG).show();
            return;
        }
    }

}





