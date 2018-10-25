package br.com.bossini.weatherforecastbycityfateccarapicuibamanha;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText locationEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationEditText =
                findViewById(R.id.locationEditText);
        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String cidade =
                                locationEditText.getEditableText().toString();
                        String chave =
                                getString(R.string.api_key);
                        String units =
                                getString(R.string.units);
                        String end =
                                getString(R.string.web_service_url, cidade, chave, units);
                        try {
                            URL url = new URL(end);
                            HttpURLConnection connection =
                                    (HttpURLConnection) url.openConnection();
                            InputStream inputStream =
                                    connection.getInputStream();
                            InputStreamReader inputStreamReader =
                                    new InputStreamReader(inputStream);
                            BufferedReader reader =
                                    new BufferedReader(inputStreamReader);
                            String linha = null;
                            StringBuilder sb = new StringBuilder();
                            while ((linha = reader.readLine()) != null){
                                sb.append(linha);
                            }
                            reader.close();
                            runOnUiThread( ()-> {
                                Toast.makeText(MainActivity.this,
                                        sb.toString(), Toast.LENGTH_SHORT).show();
                            }
                         );
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }


}
