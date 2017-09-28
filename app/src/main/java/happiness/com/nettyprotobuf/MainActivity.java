package happiness.com.nettyprotobuf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  Button btnConnect;
  Button btnDisconnect;
  Button btnSend;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    btnConnect = (Button) this.findViewById(R.id.connect);
    btnDisconnect = (Button) this.findViewById(R.id.disconnect);
    btnSend = (Button) this.findViewById(R.id.send);

    btnConnect.setOnClickListener(this);
    btnDisconnect.setOnClickListener(this);
    btnSend.setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.connect:
        Client.getInstance().connect();
        break;
      case R.id.disconnect:
        Client.getInstance().disconnect();
        break;
      case R.id.send:
        Client.getInstance().send();
        break;
    }
  }
}
