//http://www.kccistc.net
// IOT : KSH

package net.kccistc.iotsocketclient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    static final int REQUEST_CODE_SETTING_ACTIVITY = 1001;
    FragmentHome fragmentHome;
    FragmentDashboard fragmentDashboard;
    FragmentCameraview fragmentCameraview;
    FragmentMessage fragmentMessage;
    int bottomNavigationIndex = 0;
    Handler handler;
    static Socket socket=null;
    static SocketClientThread socketClientThread;
    static String serverIp = "192.168.1.75";
    static String serverPort = "5000";
    static String clientId = "OTJSMT";

    static String clientPw = "PASSWD";
    static String ardId = "OTJARD";
    static String sm7QtId = "SM7QT";
    private TextView mTextMessage;
    boolean getstate_cmd_check = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    bottomNavigationIndex = 0;
                    mTextMessage.setText(R.string.title_home);
                    replaceFragment(fragmentHome);
                    return true;
                case R.id.navigation_dashboard:
                    bottomNavigationIndex = 1;
                    mTextMessage.setText(R.string.title_dashboard);
                    replaceFragment(fragmentDashboard);
                    return true;
                case R.id.navigation_notifications:
                    bottomNavigationIndex = 2;
                    mTextMessage.setText(R.string.title_notifications);
                    replaceFragment(fragmentCameraview);
                    return true;
                case R.id.navigation_message:
                    bottomNavigationIndex = 3;
                    mTextMessage.setText(R.string.title_message);
                    replaceFragment(fragmentMessage);
                    return true;
            }
            return false;
        }
    };
    protected void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.mainFrameLayout, fragment);
        ft.commit();
        if(bottomNavigationIndex == 0) {
            sendDataString("[" + ardId + "]" + "GETSTATEHOME");
        }
        else if(bottomNavigationIndex == 1) {
 /*           sendDataString("[" + ardId + "]" + "GETCONDITION");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            sendDataString("[" + ardId + "]" + "GETCONTROL");
            getstate_cmd_check = true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy smtp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(smtp);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentHome = new FragmentHome();
        fragmentDashboard = new FragmentDashboard();
        fragmentCameraview = new FragmentCameraview();
        fragmentMessage = new FragmentMessage();

        replaceFragment(fragmentHome);
        handler = new Handler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.login:
                Intent intent = new Intent(getBaseContext(),LoginActivity.class);
                intent.putExtra("ipaddr",serverIp);
                intent.putExtra("ipport",serverPort);
                intent.putExtra("id",clientId);
                intent.putExtra("pw",clientPw);
                startActivityForResult(intent,REQUEST_CODE_SETTING_ACTIVITY);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SETTING_ACTIVITY) {
            if(resultCode == RESULT_OK) {
                if(socket == null) {
                    serverIp = data.getStringExtra("ipaddr");
                    serverPort = data.getStringExtra("ipport");
                    clientId = data.getStringExtra("id");
                    clientPw = data.getStringExtra("pw");
                    Toast.makeText(getBaseContext(), "IP:" + serverIp + ",PORT:" + serverPort + ",ID:" + clientId + ",PW:" + clientPw, Toast.LENGTH_SHORT).show();

                    socketClientThread = new SocketClientThread();
                    socketClientThread.start();
                } else
                    Toast.makeText(getBaseContext(), "이미 접속 중!",Toast.LENGTH_SHORT).show();

            } else if(resultCode == RESULT_CANCELED) {
                if(socket != null) {
                    socketClientThread.runFlag = false;
                    socketClientThread.interrupt();
                    socket = null;
                    Log.d(TAG,"thread stop");
                    Toast.makeText(getBaseContext(),"접속 종료!",Toast.LENGTH_SHORT).show();
                    if(bottomNavigationIndex == 0) {
                        fragmentHome.colorImageButton();
                    }
                } else {
                    Toast.makeText(getBaseContext(),"이미 접속 종료!",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
    class SocketClientThread extends Thread {
        final String TAG = "SocketClientThread";
        String[] idStr = new String[]{"송신ID","명령","온도","습도","조도","","",} ;
        boolean runFlag = true;
        private InputStream inputstream;
        private OutputStream outputstream;
        public void run() {

            byte buf[] = new byte[100];
            int len=0;
            String recvStr;
            try {
                socket = new Socket(serverIp, Integer.valueOf(serverPort));
                Log.d(TAG,"client started ServerIp:"+serverIp+",ServerPort:"+serverPort);
                outputstream  = socket.getOutputStream();		// 메시지 송신
                inputstream = socket.getInputStream();		//메시지 수신

                sendDataString("["+clientId+":"+clientPw+"]");
                while(runFlag) {
                    try {
                        len = inputstream.read(buf);
                        if(len <= 0)
                            break;

                        recvStr = new String(buf,0,len,"UTF-8");
                        Log.d(TAG,"inputStr from server : (length:" +  recvStr.length() +")," + recvStr);

                        commandParsing(recvStr);
/*                        //					String[] splitLists = new String[5];
                        String[] splitLists = recvStr.toString().split("\\[|]|@");

                        //					if ((recvStr.indexOf("New con") != -1) || (recvStr.indexOf("Already") != 1) )
                        if ((recvStr.indexOf("SENSOR") != -1) )
                        {
                            for( int i=1;i<splitLists.length;i++ )
                            {
                                Log.d(TAG,idStr[i-1] +":" + splitLists[i]);
                            }
                        }*/
                    } catch (IOException e) {
                        Log.d(TAG,"server disconnected!");
                    }
                }
//				socket.close();
                Log.d(TAG,"쓰레드  종료");
            } catch (IOException e) {
                //e.printStackTrace();
                Log.d(TAG,"서버를 확인하세요!!");
            }
        }
        private void commandParsing(String cmdStr) {
            final String recvStr = cmdStr;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if ((recvStr.indexOf("New con") != -1) ) {
                        //						sendMessage("[IDLIST]\n");
                        if(bottomNavigationIndex == 0) {
                            sendDataString("[" + ardId + "]" + "GETSTATEHOME");
                            fragmentHome.colorImageButton();
                        }
                        return;
                    }

                    mTextMessage.setText(recvStr.substring(0,recvStr.length() -1 ));
                    if(bottomNavigationIndex == 3)
                        fragmentMessage.appendTextView(recvStr);

                    String[] splitLists = recvStr.toString().split("\\[|]|@|\\n");
                    if(bottomNavigationIndex == 0)
                    {
    //                    Toast.makeText(getBaseContext(),splitLists[2],Toast.LENGTH_SHORT).show();
                        if(splitLists.length >= 5 && splitLists[2].equals("GETSTATEHOME"))
                        {
                            fragmentHome.updateImageButton(splitLists[3]);
                            fragmentHome.updateImageButton(splitLists[4]);
                        }
                        else if(splitLists.length >= 3)
                            fragmentHome.updateImageButton(splitLists[2]);
                    }
                    else if (bottomNavigationIndex == 1) {
                        if(splitLists[2].equals("GETCONDITION") || splitLists[2].equals("GETCONTROL") ) {
                            fragmentDashboard.updateTextView(splitLists);
                            getstate_cmd_check = false;
                        }
                        else {

                            fragmentDashboard.updateImageView(splitLists[2]);
                        }
                    }
                    else if (bottomNavigationIndex == 2) {
                        if(splitLists[2].equals("TIMERSTART") || splitLists[2].equals("TIMERSTOP") ) {
                            fragmentCameraview.updateToggleButtonTimer(splitLists[2]);
                        } else if(splitLists[2].equals("CAMERASTART") || splitLists[2].equals("CAMERASTOP") ) {
                            fragmentCameraview.updateWebviewCamera(splitLists[2]);
                        }

                    }
                }
            });
        }
        public void interrupt() {
            try {
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            super.interrupt();
        }
        public void sendDataByte(byte[] pktmsg) {
            try {
                outputstream.write(pktmsg);
                outputstream.flush();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendDataString(String strMsg) {
        byte[] sendMsg = new byte[100];

        if(strMsg.charAt(0) != '[')
            strMsg = "[ALLMSG]"+strMsg;

        strMsg = strMsg + '\n';
        if(socket != null) {
            try {
                sendMsg = strMsg.getBytes("utf-8");
                socketClientThread.sendDataByte(sendMsg);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
