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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by user on 2017-12-29.
 */

public class FragmentDashboard extends Fragment {
    View view;
    MainActivity mainActivity;
    Switch switchLed;
    Switch switchBlinds;
    Switch switchAir;
    TextView textViewIllumination;
    TextView textViewTemp;
    TextView textViewHumi;
    ImageView imageViewLed;
    ImageView imageViewBlinds;
    ImageView imageViewAir;
    Button buttonCondition;
    Button buttonControl;
    boolean fragmentDashboardOnStop = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mainActivity = (MainActivity) getActivity();
        textViewIllumination = (TextView) view.findViewById(R.id.textViewIllumination);
        textViewTemp = (TextView) view.findViewById(R.id.textViewTemp);
        textViewHumi = (TextView) view.findViewById(R.id.textViewHumi);
        imageViewLed = (ImageView) view.findViewById(R.id.imageViewLed);
        imageViewBlinds = (ImageView) view.findViewById(R.id.imageViewBlinds) ;
        imageViewAir = (ImageView) view.findViewById(R.id.imageViewAir);

        buttonCondition = (Button) view.findViewById(R.id.buttonCondition);
        buttonCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.sendDataString("[" + mainActivity.ardId + "]" + "GETCONDITION");
                mainActivity.getstate_cmd_check = true;
            }
        });
        buttonControl = (Button) view.findViewById(R.id.buttonControl);
        buttonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.sendDataString("[" + mainActivity.ardId + "]" + "GETCONTROL");
                mainActivity.getstate_cmd_check = true;
            }
        });



        switchLed = (Switch)view.findViewById(R.id.switchLed);
        switchLed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecking) {
                if(!mainActivity.getstate_cmd_check) {
                    if(ischecking){
                        mainActivity.sendDataString("[" + mainActivity.ardId + "]" + "LED_ON");

                    }else {
                        mainActivity.sendDataString("[" + mainActivity.ardId + "]" + "LED_OFF");

                    }
               }
            }
        });
        switchBlinds = (Switch)view.findViewById(R.id.switchBlinds);
        switchBlinds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecking) {
                if(!mainActivity.getstate_cmd_check) {
                    if (ischecking) {
                        mainActivity.sendDataString("[" + mainActivity.ardId + "]" + "BLINDS_ON");
                    } else {
                        mainActivity.sendDataString("[" + mainActivity.ardId + "]" + "BLINDS_OFF");
                    }
                }
            }
        });
        switchAir = (Switch)view.findViewById(R.id.switchAir);
        switchAir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecking) {
                if(!mainActivity.getstate_cmd_check) {
                    if (ischecking) {
                        mainActivity.sendDataString("[" + mainActivity.ardId + "]" + "AIR_ON");
                    } else {
                        mainActivity.sendDataString("[" + mainActivity.ardId + "]" + "AIR_OFF");
                    }
                }
            }
        });
        return view;
    }
    public void updateImageView(String strCmd) {
        if (strCmd.equals("LED_ON")) {
            imageViewLed.setImageResource(R.drawable.led_on);
            switchLed.setChecked(true);
        } else if(strCmd.equals("LED_OFF")) {
            imageViewLed.setImageResource(R.drawable.led_off);
            switchLed.setChecked(false);
        } else if (strCmd.equals("BLINDS_ON")) {
            imageViewBlinds.setImageResource(R.drawable.blinds_on);
            switchBlinds.setChecked(true);
        } else if(strCmd.equals("BLINDS_OFF")) {
            imageViewBlinds.setImageResource(R.drawable.blinds_off);
            switchBlinds.setChecked(false);
        } else if (strCmd.equals("AIR_ON")) {
            imageViewAir.setImageResource(R.drawable.air_on);
            switchAir.setChecked(true);
        } else if(strCmd.equals("AIR_OFF")) {
            imageViewAir.setImageResource(R.drawable.air_off);
            switchAir.setChecked(false);
        }
    }
    public void updateTextView(String[] splitLists) {
        if(splitLists[2].equals("GETCONDITION")) {
            textViewIllumination.setText(splitLists[3]);    //Illumination
            textViewTemp.setText(splitLists[4]);            //temp
            textViewHumi.setText(splitLists[5]);            //humi
        } else if(splitLists[2].equals("GETCONTROL")) {
            updateImageView(splitLists[3]); //LEDON/LEDOFF
            updateImageView(splitLists[4]); //BLINDSON/BLINDOFF
            updateImageView(splitLists[5]); //AIRON/AIROFF
        }
    }
}
