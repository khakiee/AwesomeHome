package kjh.hantor.hantor_smarthome;

import android.util.Log;

import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Connection extends Thread{
    String URL;
    OkHttpClient client;
    FormBody body;
    Request req;
    Response resp;
    String params[];

    public void CloseAll(){
        URL = null;
        client = null;
        body = null;
        req = null;
        resp = null;
        params = null;
    }
    public Connection(String url,String...strs){
        this.URL = url;
        params = strs;
    }

    @Override
    public void run(){
        client = new OkHttpClient();
        FormBody.Builder temp = new FormBody.Builder();
        for(int i = 0 ; i < params.length ; i += 2){
            temp.add(params[i],params[i+1]);
        }
        body = temp.build();
        req = new Request
                .Builder()
                .url(URL)
                .post(body)
                .build();
        try {

            this.resp = client.newCall(req).execute();
            Log.d("con_resp",this.resp.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
