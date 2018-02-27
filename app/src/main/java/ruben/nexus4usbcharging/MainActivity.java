package ruben.nexus4usbcharging;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import java.io.DataOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    TextView txt;
    Button btnDisableUSB,btnEnableUSB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDisableUSB = (Button)findViewById(R.id.btnDisableUSB);
        btnEnableUSB = (Button)findViewById(R.id.btnEnableUSB);
        txt = (TextView) findViewById(R.id.textView);

        btnEnableUSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Process su = Runtime.getRuntime().exec("su");
                    DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

                    outputStream.writeBytes("echo 0 > /sys/module/pm8921_charger/parameters/disabled\n");
                    outputStream.flush();

                    outputStream.writeBytes("exit\n");
                    outputStream.flush();
                    su.waitFor();

                    txt.setText(R.string.usbEnabled);
                } catch (IOException e) {
                    txt.setText(String.format("%s\n\n%s\n\n%s", getString(R.string.exception), e.getMessage(), getString(R.string.rootRequired)));
                } catch (InterruptedException e) {
                    txt.setText(e.getMessage());
                }
            }
        });

        btnDisableUSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Process su = Runtime.getRuntime().exec("su");
                    DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

                    outputStream.writeBytes("echo 1 > /sys/module/pm8921_charger/parameters/disabled\n");
                    outputStream.flush();

                    outputStream.writeBytes("exit\n");
                    outputStream.flush();
                    su.waitFor();

                    txt.setText(R.string.usbDisabled);
                } catch (IOException e) {
                    txt.setText(String.format("%s\n\n%s\n\n%s", getString(R.string.exception), e.getMessage(), getString(R.string.rootRequired)));
                } catch (InterruptedException e) {
                    txt.setText(e.getMessage());
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
