//http://www.kccistc.net
// IOT : KSH

package net.kccistc.iotsocketclient;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by user on 2017-12-27.
 */

public class FragmentHome extends Fragment {
    View view;
    MainActivity mainActivity;
    ImageButton imageButtonLight;
    ImageButton imageButtonPlug;
    boolean lightButtonState = false;
    boolean plugButtonState = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home,container,false);
        mainActivity = (MainActivity) getActivity();
        imageButtonLight = (ImageButton) view.findViewById(R.id.imageButtonLight);
        imageButtonPlug = (ImageButton) view.findViewById(R.id.imageButtonPlug);

        imageButtonLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lightButtonState) {
                    mainActivity.sendDataString("[" + mainActivity.ardId + "]" + "LIGHT_OFF");
                }
                else {
                    mainActivity.sendDataString("[" + mainActivity.ardId + "]" + "LIGHT_ON");
                }
            }
        });
        imageButtonPlug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(plugButtonState) {
                    mainActivity.sendDataString("[" + mainActivity.ardId + "]" + "PLUG_OFF");
                }
                else {
                    mainActivity.sendDataString("[" + mainActivity.ardId + "]" + "PLUG_ON");
                }
            }
        });
        colorImageButton();
        return view;
    }
    public void updateImageButton(String strCmd) {
//        Toast.makeText(getActivity(),"TEST: "+strCmd,Toast.LENGTH_SHORT).show();
        if(strCmd.equals("LIGHT_ON")) {
//            Toast.makeText(getActivity(),strCmd,Toast.LENGTH_SHORT).show();
            imageButtonLight.setImageResource(R.drawable.light_on);
            lightButtonState = true;
        }
        else if(strCmd.equals("LIGHT_OFF")) {
            imageButtonLight.setImageResource(R.drawable.light_off);
            lightButtonState = false;
        }
        else if(strCmd.equals("PLUG_ON")) {
            imageButtonPlug.setImageResource(R.drawable.plug_on);
            plugButtonState = true;
        }
        else if(strCmd.equals("PLUG_OFF")) {
            imageButtonPlug.setImageResource(R.drawable.plug_off);
            plugButtonState = false;
        }
    }
    public void colorImageButton() {
        if(mainActivity.socket != null) {
            imageButtonLight.setBackgroundColor(Color.GREEN);
            imageButtonPlug.setBackgroundColor(Color.GREEN);
        } else {
            imageButtonLight.setBackgroundColor(Color.LTGRAY);
            imageButtonPlug.setBackgroundColor(Color.LTGRAY);
        }

    }
}
