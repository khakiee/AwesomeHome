package kjh.hantor.hantor_smarthome;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


class CustomButton {
    public CustomButton(Button con) {
        this.isButtonRed = isButtonRed;
        this.con = con;
    }

    private static final int CON_SIZE = 4;
    int OnOffStatus = 0;
    boolean isButtonRed = false;
    Button con;
}

public class Status extends Fragment {
    private static final int CON_SIZE = 4;
    CustomButton[] button = new CustomButton[4];

    public Status() {
    }

    private String makePOSTurl(String idx, String on_off) {
        String temp = "http://13.209.64.184/index.php?b" + idx + "=" + on_off;
        return temp;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* try {
            Connection init = new Connection("http://http://35.167.126.17/status");
            init_btn_stat = init.resp.body().string();
            //btn control
            for(int i = 0 ; i < CON_SIZE ; i ++){
                if(init_btn_stat.charAt(i) == 0){
                    con[i].setBackgroundColor(getResources().getColor(R.color.mb_red));
                    con[i].setText("OFF");
                    status[i] = false;

                }
                else{
                    con[i].setBackgroundColor(getResources().getColor(R.color.mb_blue));
                    con[i].setText("ON");
                    status[i] = true;
                }
            }
            Toast.makeText(this.getContext(), "Load Completed", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_status, container, false);
        layout.setBackgroundColor(getResources().getColor(R.color.dark_gray));

        HTTPgetUtils init_button = new HTTPgetUtils();
        try {
            init_button.execute("http://13.209.64.184/status.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        button[0] = new CustomButton(new Button(layout.getContext()));
        button[1] = new CustomButton(new Button(layout.getContext()));
        button[2] = new CustomButton(new Button(layout.getContext()));
        button[3] = new CustomButton(new Button(layout.getContext()));

        button[0].con = (Button) layout.findViewById(R.id.c_btn_1);
        button[1].con = (Button) layout.findViewById(R.id.c_btn_2);
        button[2].con = (Button) layout.findViewById(R.id.c_btn_3);
        button[3].con = (Button) layout.findViewById(R.id.c_btn_4);

        //request and initiate state of button
        Log.e("response_button", init_button.res.toString());
        for (int idx = 0; idx < button.length; idx++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (init_button.res.charAt(idx) == '1') {
                    button[idx].isButtonRed = true;
                    button[idx].OnOffStatus = 1;
                    button[idx].con.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.rad_button_off));

                } else {
                    button[idx].isButtonRed = false;
                    button[idx].OnOffStatus = 0;
                    button[idx].con.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.rad_button_on));

                }
            }
        }

        View.OnClickListener onclick = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                for (int idx = 0; idx < button.length; idx++) {
                    if (view.equals(button[idx].con)) {
                        if (button[idx].isButtonRed) {
                            HTTPgetUtils onclick = new HTTPgetUtils();
                            button[idx].isButtonRed = false;
                            button[idx].OnOffStatus = 0;
                            Log.d("POST req url : ", String.valueOf(idx + 1) + String.valueOf(button[idx].OnOffStatus));
                            onclick.execute(makePOSTurl(String.valueOf(idx + 1), String.valueOf(button[idx].OnOffStatus)));
                            view.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.rad_button_on));
                            //request
                        } else {
                            HTTPgetUtils onclick = new HTTPgetUtils();
                            button[idx].isButtonRed = true;
                            button[idx].OnOffStatus = 1;
                            Log.d("POST req url : ", String.valueOf(idx + 1) + String.valueOf(button[idx].OnOffStatus));
                            onclick.execute(makePOSTurl(String.valueOf(idx + 1), String.valueOf(button[idx].OnOffStatus)));
                            view.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.rad_button_off));
                            //request
                        }
                    }
                }
            }
        };

        for (int i = 0; i < CON_SIZE; i++) {
            button[i].con.setTextColor(getResources().getColor(R.color.mb_white));
            button[i].con.setOnClickListener(onclick);
        }

        new CountDownTimer(16069000, 5000) {

            public void onTick(long millisUntilFinished) {

                HTTPgetUtils init_button = new HTTPgetUtils();
                try {
                    init_button.execute("http://13.209.64.184/status.php").get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int idx = 0; idx < button.length; idx++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        if (init_button.res.charAt(idx) == '1') {
                            button[idx].isButtonRed = true;
                            button[idx].OnOffStatus = 1;
                            button[idx].con.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.rad_button_off));

                        } else {
                            button[idx].isButtonRed = false;
                            button[idx].OnOffStatus = 0;
                            button[idx].con.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.rad_button_on));

                        }
                    }
                }
                Toast.makeText(layout.getContext(),"seconds remaining: " + millisUntilFinished / 1000,Toast.LENGTH_SHORT);


            }

            public void onFinish() {
                Toast.makeText(layout.getContext(),"updated!!",Toast.LENGTH_SHORT);
            }

        }.start();

        return layout;

    }
}

class HTTPgetUtils extends AsyncTask<String, String, String> {

    String res = "";

    @Override
    protected String doInBackground(String... params) {
        try {
            URL obj = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            int retCode = conn.getResponseCode();

            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = br.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            Log.e("response", response.toString());
            br.close();

            res = response.toString();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    protected void onPostExecute(Long result) {

    }

}

