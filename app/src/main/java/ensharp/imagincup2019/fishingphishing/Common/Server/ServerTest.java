package ensharp.imagincup2019.fishingphishing.Common.Server;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import ensharp.imagincup2019.fishingphishing.R;
import ensharp.imagincup2019.fishingphishing.UI.MainActivity;

public class ServerTest extends AppCompatActivity {
    //버튼 클릭시 데이터 전송
    Button send_data_button;

    //임시 데이터
    String temp_text;
    int temp_flag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp_text = "dain and daDDung are super very pig";
        temp_flag = 1;

        // 버튼이 있다는 가정 하에
//        send_data_button = (Button)findViewById(R.id.send_data);
//        send_data_button.setOnClickListener(new Button.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                //서버에 요청할 URL 주소
//                String url = "http://13.77.161.171:80/text";
//
//                //서버에 보낼 데이터
//                JSONObject jsonObject = send_Data(temp_text,temp_flag);
//
//                NetworkTask networkTask = new NetworkTask(MainActivity.this, url, jsonObject);
//                networkTask.execute();
//
//            }
//        });
    }

    //서버에 요청할 데이터
    private JSONObject send_Data(String text_data, int flag_data){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("text", text_data);
            jsonObject.accumulate("flag", flag_data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
