package br.com.bossini.weatherforecastbycityfateccarapicuibamanha;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText locationEditText;
    private List <Weather> previsoes;
    private ListView weatherListView;
    private WeatherAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationEditText =
                findViewById(R.id.locationEditText);
        previsoes = new ArrayList<>() ;
        weatherListView =
                findViewById(R.id.weatherListView);
        adapter = new WeatherAdapter(this, previsoes);
        weatherListView.setAdapter(adapter);
        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cidade =
                        locationEditText.getEditableText().toString();
                String chave =
                        getString(R.string.api_key);
                String units =
                        getString(R.string.units);
                String end =
                        getString(R.string.web_service_url, cidade, chave, units);
                new ObtemTemperaturas().execute(end);
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                }).start();*/

            }
        });
    }

    private class ObtemTemperaturas extends AsyncTask <String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
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
                return sb.toString();
                /*runOnUiThread( ()-> {
                            Toast.makeText(MainActivity.this,
                                    sb.toString(), Toast.LENGTH_SHORT).show();
                        }
                );*/
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonS) {
            /*Toast.makeText(MainActivity.this,
                    json, Toast.LENGTH_SHORT).show();*/
            previsoes.clear();
            try {
                JSONObject json = new JSONObject(jsonS);
                JSONArray list = json.getJSONArray("list");
                for (int i = 0; i < list.length(); i++){
                    JSONObject previsao = list.getJSONObject(i);
                    long dt = previsao.getLong("dt");
                    JSONObject main = previsao.getJSONObject("main");
                    double temp_min = main.getDouble("temp_min");
                    double temp_max = main.getDouble("temp_max");
                    int humidity = main.getInt("humidity");
                    JSONObject unicoNoWeather = previsao.getJSONArray("weather").getJSONObject(0);
                    String description = unicoNoWeather.getString("description");
                    String icon = unicoNoWeather.getString("icon");
                    Weather w = new Weather(dt, temp_min, temp_max, humidity, description, icon);
                    previsoes.add(w);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
