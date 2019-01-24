package ensharp.imagincup2019.fishingphishing.Common.Server;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestServer {

    private String url;
    private JSONObject data;

    public RequestServer(String _url, JSONObject _data){
        this.url = _url;
        this.data = _data;
    }

    //POST 방식 요청
    public String request_POST() {
        String result = null;
        BufferedReader reader = null;

        try {
            URL urlObject = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObject.openConnection();


            conn.setReadTimeout(150000); //10초동안 서버로부터 반응없으면 에러
            conn.setConnectTimeout(15000); // 접속하는 커넥션 타임 15초동안 접속안되면 접속 안되는 것으로 간주


            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Accept", "text/html");
            conn.setRequestProperty("Content-Type", "application/json"); //서버로 보내는 패킷이 어떤타입인지 선언

            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(data.toString().getBytes());

            InputStream stream = conn.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line ="";

            while ((line = reader.readLine()) != null)
                buffer.append(line);

            if(reader != null) {
                reader.close(); //버퍼를 닫아줌
            }

            result = buffer.toString();

            if(conn != null)
                conn.disconnect();

            return result;
        } catch (Exception e) {
            Log.e("Error",e.toString());
            e.printStackTrace();
        }

        return null;
    }

    //GET 방식 요청
    public String request_GET(){

        String result = null;
        BufferedReader br = null;

        try{
            URL urlObject = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObject.openConnection();

            conn.setRequestMethod("GET");

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));

            String line;

            while((line = br.readLine()) != null) {
                result = result + line + "\n";
            }

            if(conn != null)
                conn.disconnect();

            return result;
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
