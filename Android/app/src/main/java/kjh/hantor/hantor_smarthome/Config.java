package kjh.hantor.hantor_smarthome;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class Config extends Fragment {
    private static final int CON_SIZE = 4;
    CustomButton[] button_set = new CustomButton[4];
    Button btn_send;


    public Config() {
    }

    private String makePOSTurl(String idx, String on_off) {
        String temp = "http://13.125.227.46/write_input.php?b" + idx + "=" + on_off;
        return temp;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_config, container, false);
        layout.setBackgroundColor(getResources().getColor(R.color.dark_gray));

        HTTPgetUtils init_button = new HTTPgetUtils();
        try {
            init_button.execute("http://13.125.227.46/default_status.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        btn_send = (Button) layout.findViewById(R.id.c_btn_set);
        button_set[0] = new CustomButton(new Button(layout.getContext()));
        button_set[1] = new CustomButton(new Button(layout.getContext()));
        button_set[2] = new CustomButton(new Button(layout.getContext()));
        button_set[3] = new CustomButton(new Button(layout.getContext()));

        button_set[0].con = (Button) layout.findViewById(R.id.c_btn_1);
        button_set[1].con = (Button) layout.findViewById(R.id.c_btn_2);
        button_set[2].con = (Button) layout.findViewById(R.id.c_btn_3);
        button_set[3].con = (Button) layout.findViewById(R.id.c_btn_4);

        //request and initiate state of button
        Log.e("response_button", init_button.res.toString());
        for (int idx = 0; idx < button_set.length; idx++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (init_button.res.charAt(idx) == '1') {
                    button_set[idx].isButtonRed = true;
                    button_set[idx].OnOffStatus = 1;
                    button_set[idx].con.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.rad_button_off));

                } else {
                    button_set[idx].isButtonRed = false;
                    button_set[idx].OnOffStatus = 0;
                    button_set[idx].con.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.rad_button_on));

                }
            }
        }
        //button[4].con.setBackground(ContextCompat.getDrawable((layout.getContext(), R.drawable.~~)));

        View.OnClickListener onclick = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                for (int idx = 0; idx < button_set.length; idx++) {
                    if (view.equals(button_set[idx].con)) {
                        if (button_set[idx].isButtonRed) {
                            button_set[idx].isButtonRed = false;
                            button_set[idx].OnOffStatus = 0;
                            view.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.rad_button_on));
                            //request
                        } else {
                            button_set[idx].isButtonRed = true;
                            button_set[idx].OnOffStatus = 1;
                            view.setBackground(ContextCompat.getDrawable(layout.getContext(), R.drawable.rad_button_off));
                            //request
                        }
                    }
                }
            }
        };
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HTTPgetUtils send_set = new HTTPgetUtils();
                try {
                    send_set.execute("http://13.125.227.46/setting.php?"
                            +"b1=" +String.valueOf(button_set[0].OnOffStatus)
                            +"&b2="+String.valueOf(button_set[1].OnOffStatus)
                            +"&b3="+String.valueOf(button_set[2].OnOffStatus)
                            +"&b4="+String.valueOf(button_set[3].OnOffStatus)).get();
                    Log.i("setting send",send_set.res.toString());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for (int i = 0; i < CON_SIZE; i++) {
            button_set[i].con.setTextColor(getResources().getColor(R.color.mb_white));
            button_set[i].con.setOnClickListener(onclick);
        }

        return layout;

    }
}