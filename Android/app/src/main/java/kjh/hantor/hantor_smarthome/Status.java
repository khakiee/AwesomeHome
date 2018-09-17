package kjh.hantor.hantor_smarthome;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;

import static java.lang.System.in;

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
    private static final int RESULT_OK = -1;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    CustomButton[] button = new CustomButton[4];
    TextView voice;
    Button voice_btn;

    public Status() {
    }

    private String makePOSTurl(String idx, String on_off) {
        String temp = "http://13.125.227.46/write_input.php?b" + idx + "=" + on_off;
        return temp;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_status, container, false);
        layout.setBackgroundColor(getResources().getColor(R.color.dark_gray));

        HTTPgetUtils init_button = new HTTPgetUtils();
        try {
            init_button.execute("http://13.125.227.46/buffer/input").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        voice = (TextView) layout.findViewById(R.id.voice);
        voice_btn = (Button) layout.findViewById(R.id.voice_btn);

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
        voice_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

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

        new CountDownTimer(16069000, 1000) {

            public void onTick(long millisUntilFinished) {

                HTTPgetUtils init_button = new HTTPgetUtils();
                try {
                    init_button.execute("http://13.125.227.46/buffer/input").get();
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
                Toast.makeText(layout.getContext(), "seconds remaining: " + millisUntilFinished / 1000, Toast.LENGTH_SHORT);


            }

            public void onFinish() {
                Toast.makeText(layout.getContext(), "updated!!", Toast.LENGTH_SHORT);
            }

        }.start();

        return layout;

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getLayoutInflater().getContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("REQCODE",String.valueOf(requestCode));
        if(requestCode == REQ_CODE_SPEECH_INPUT) {
            Log.d("result",String.valueOf(resultCode));
            if (resultCode == RESULT_OK) {
                ArrayList<String> matches_text = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Log.e("Voice", matches_text.toString());
                Log.e("data->tostring",matches_text.toString());

                if(matches_text.contains("일본 거")||matches_text.contains("일번 꺼")||matches_text.contains("1번 꺼")||matches_text.contains("1번 꺼")){
                    Log.e("Voice-Off","OFF 1");
                    HTTPgetUtils voice = new HTTPgetUtils();
                    button[0].isButtonRed = true;
                    button[0].OnOffStatus = 1;
                    button[0].con.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rad_button_off));
                    voice.execute(makePOSTurl(String.valueOf(1), String.valueOf(button[0].OnOffStatus)));
                }
                else if(matches_text.contains("일번 켜")||matches_text.contains("일본 켜")||matches_text.contains("1본 켜")||matches_text.contains("1번 켜")) {
                    Log.e("Voice-Off", "ON 1");
                    HTTPgetUtils voice = new HTTPgetUtils();
                    button[0].isButtonRed = false;
                    button[0].OnOffStatus = 0;
                    button[0].con.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rad_button_on));
                    voice.execute(makePOSTurl(String.valueOf(1), String.valueOf(button[0].OnOffStatus)));
                }
                else if(matches_text.contains("이번 켜")||matches_text.contains("이본 켜")||matches_text.contains("2번 켜")||matches_text.contains("2본 켜")){
                    Log.e("Voice-Off","OFF 2");
                    HTTPgetUtils voice = new HTTPgetUtils();
                    button[1].isButtonRed = false;
                    button[1].OnOffStatus = 0;
                    button[1].con.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rad_button_on));
                    voice.execute(makePOSTurl(String.valueOf(2), String.valueOf(button[1].OnOffStatus)));

                }
                else if(matches_text.contains("이번 꺼")||matches_text.contains("이본 꺼")||matches_text.contains("2 본 꺼")||matches_text.contains("2번 꺼")){
                    Log.e("Voice-Off","OFF 2");
                    HTTPgetUtils voice = new HTTPgetUtils();
                    button[1].isButtonRed = true;
                    button[1].OnOffStatus = 1;
                    button[1].con.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rad_button_off));
                    voice.execute(makePOSTurl(String.valueOf(2), String.valueOf(button[1].OnOffStatus)));

                }
                else if(matches_text.contains("3번 꺼")||matches_text.contains("삼번 꺼")||matches_text.contains("3 번 꺼")||matches_text.contains("3 번 거")){
                    Log.e("Voice-Off","OFF 3");
                    HTTPgetUtils voice = new HTTPgetUtils();
                    button[2].isButtonRed = true;
                    button[2].OnOffStatus = 1;
                    button[2].con.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rad_button_off));
                    voice.execute(makePOSTurl(String.valueOf(3), String.valueOf(button[2].OnOffStatus)));

                }
                else if(matches_text.contains("3번 켜")|| matches_text.contains("삼번 켜")||matches_text.contains("3 번 켜")){
                    Log.e("Voice-Off","ON 3");
                    HTTPgetUtils voice = new HTTPgetUtils();
                    button[2].isButtonRed = false;
                    button[2].OnOffStatus = 0;
                    button[2].con.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rad_button_on));
                    voice.execute(makePOSTurl(String.valueOf(3), String.valueOf(button[2].OnOffStatus)));

                }
                else if(matches_text.contains("사번 꺼")||matches_text.contains("사본 거")||matches_text.contains("4번 꺼")||matches_text.contains("4 번 거")){
                    Log.e("Voice-Off","OFF 4");
                    HTTPgetUtils voice = new HTTPgetUtils();
                    button[3].isButtonRed = true;
                    button[3].OnOffStatus = 1;
                    button[3].con.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rad_button_off));
                    voice.execute(makePOSTurl(String.valueOf(4), String.valueOf(button[3].OnOffStatus)));

                }
                else if(matches_text.contains("사번 켜")||matches_text.contains("사본 켜")||matches_text.contains("4 번 켜")||matches_text.contains("사 번 켜")){
                    Log.e("Voice-Off","ON 4");
                    HTTPgetUtils voice = new HTTPgetUtils();
                    button[3].isButtonRed = false;
                    button[3].OnOffStatus = 0;
                    button[3].con.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rad_button_on));
                    voice.execute(makePOSTurl(String.valueOf(4), String.valueOf(button[3].OnOffStatus)));

                }
                else{
                    Toast.makeText(getContext(),"다시 말해 주세요",Toast.LENGTH_SHORT).show();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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


