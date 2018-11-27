package ensharp.imagincup2019.fishingphishing.Common.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    //해당 화면 context 변수
    private Context context;

    //서버 요청할 주소, 데이터
    private String url;
    private JSONObject data;

    //NetworkTask 생성자
    public NetworkTask(Context context,String url,JSONObject data){
        this.context = context;
        this.url = url;
        this.data = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
        result = requestHttpURLConnection.request(url, data);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        //서버에 요청한 값이 null 일 때
        if(result == null){
            Toast.makeText(context,"인터넷 연결을 확인하세요",Toast.LENGTH_LONG).show();
            return;
        }

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }


}
