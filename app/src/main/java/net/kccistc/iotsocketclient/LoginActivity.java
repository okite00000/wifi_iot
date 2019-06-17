//http://www.kccistc.net
// IOT : KSH

package net.kccistc.iotsocketclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText editTextIp;
    EditText editTextPort;
    EditText editTextId;
    EditText editTextPw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextIp = (EditText) findViewById(R.id.editTextIp);
        editTextPort = (EditText) findViewById(R.id.editTextPort);
        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextPw = (EditText) findViewById(R.id.editTextPw);
        Button buttonOk = (Button) findViewById(R.id.buttonOk);
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);

        Intent intent = getIntent();
        final String serverIp = intent.getStringExtra("ipaddr");
        final String serverPort = intent.getStringExtra("ipport");
        final String clientId = intent.getStringExtra("id");
        final String clientPw = intent.getStringExtra("pw");

        editTextIp.setHint(serverIp);
        editTextPort.setHint(serverPort);
        editTextId.setHint(clientId);
        editTextPw.setHint(clientPw);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strIp = editTextIp.getText().toString();
                String strPort = editTextPort.getText().toString();
                String strId = editTextId.getText().toString();
                String strPw = editTextPw.getText().toString();

                if(strIp.isEmpty())
                    strIp = serverIp;
                if(strPort.isEmpty())
                    strPort = serverPort;
                if(strId.isEmpty())
                    strId = clientId;
                if(strPw.isEmpty())
                    strPw = clientPw;
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ipaddr",strIp);
                resultIntent.putExtra("ipport",strPort);
                resultIntent.putExtra("id",strId);
                resultIntent.putExtra("pw",strPw);
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
}
