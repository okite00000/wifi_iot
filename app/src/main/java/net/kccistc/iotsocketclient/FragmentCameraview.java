//http://www.kccistc.net
// IOT : KSH

package net.kccistc.iotsocketclient;

import android.app.Fragment;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ToggleButton;

/**
 * Created by user on 2018-01-02.
 */

public class FragmentCameraview extends Fragment {
    View view;
    ImageView imageViewDisconnect;
    WebView webViewCamera;
    ToggleButton toggleButton;
    ToggleButton toggleButtonTimer;
    MainActivity mainActivity;

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_cameraview, container, false);
        mainActivity = (MainActivity) getActivity();
        imageViewDisconnect = (ImageView) view.findViewById(R.id.imageViewDisconnect);
        webViewCamera = (WebView) view.findViewById(R.id.webviewCamera);
        webViewCamera.setWebViewClient(new WebViewClient());
        WebSettings set = webViewCamera.getSettings();
        set.setJavaScriptEnabled(true);

        webViewCamera.setHorizontalScrollBarEnabled(false); //가로 스크롤
        webViewCamera.setVerticalScrollBarEnabled(false);   //세로 스크롤
        webViewCamera.getSettings().setBuiltInZoomControls(true); //zoom 허용
        webViewCamera.getSettings().setSupportZoom(true);         //zoom 허용
        webViewCamera.onPause();
//        webViewCamera.loadUrl("http://192.168.0.70:8080/?action=stream");
        webViewCamera.loadUrl("http://m.naver.com");
        toggleButton = (ToggleButton) view.findViewById(R.id.toggleButtonPlayStop);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggleButton.isChecked()) {
                    mainActivity.sendDataString("[" + mainActivity.sm7QtId + "]" + "CAMERASTART");
                    toggleButton.setChecked(false);
                } else {
                    mainActivity.sendDataString("[" + mainActivity.sm7QtId + "]" + "CAMERASTOP");
                    toggleButton.setChecked(true);
                }
            }
        });
        toggleButtonTimer = (ToggleButton) view.findViewById(R.id.toggleButtonTimer);
        toggleButtonTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggleButtonTimer.isChecked()) {
                    mainActivity.sendDataString("[" + mainActivity.sm7QtId + "]" + "TIMERSTART");
                    toggleButtonTimer.setChecked(false);
                } else {
                    mainActivity.sendDataString("[" + mainActivity.sm7QtId + "]" + "TIMERSTOP");
                    toggleButtonTimer.setChecked(true);
                }
            }
        });
        return view;
    }
    void updateToggleButtonTimer(String strCmd) {
        if(strCmd.equals("TIMERSTART")) {
            toggleButtonTimer.setChecked(true);
            toggleButtonTimer.setText("SM7 Tmer Stop");
        } else {
            toggleButtonTimer.setChecked(false);
            toggleButtonTimer.setText("SM7 Tmer Start");
        }
    }
    void updateWebviewCamera(String strCmd) {
        if(strCmd.equals("CAMERASTART")) {
            toggleButton.setChecked(true);
            webViewCamera.onResume();
            toggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_cam_off));
            webViewCamera.setVisibility(View.VISIBLE);
            imageViewDisconnect.setVisibility(View.INVISIBLE);
        } else {
            toggleButton.setChecked(false);
            webViewCamera.onPause();
            toggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_cam_on));
            webViewCamera.setVisibility(View.INVISIBLE);
            imageViewDisconnect.setVisibility(View.VISIBLE);
        }
    }
}
