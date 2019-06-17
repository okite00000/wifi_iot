//http://www.kccistc.net
// IOT : KSH

package net.kccistc.iotsocketclient;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2017-12-26.
 */

public class FragmentMessage extends Fragment {
    MainActivity mainActivity;
    View view;
    EditText editTextSend;
    Button buttonSend;
    ScrollView scrollView;
    TextView textViewRecv;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd hh:mm:ss");
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message,container,false);
        mainActivity = (MainActivity) getActivity();
        editTextSend = (EditText) view.findViewById(R.id.editTextSend);
        buttonSend = (Button) view.findViewById(R.id.buttonSend);
        scrollView = (ScrollView) view.findViewById(R.id.scrollViewRecv);
        textViewRecv = (TextView) view.findViewById(R.id.textViewRecv);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendString = editTextSend.getText().toString();
                if(!sendString.isEmpty()){
                    mainActivity.sendDataString(sendString);
                    editTextSend.setText("");
                }
            }
        });

       return view;
    }
    public void appendTextView(String recvStr)
    {
        Date date = new Date();
        String strDate = dateFormat.format(date);
        strDate = strDate +"->" +recvStr;
        textViewRecv.append(strDate);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
