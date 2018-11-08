package br.com.bossini.weatherforecastbycityfateccarapicuibamanha;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherAdapter extends ArrayAdapter <Weather> {

    private Map <String, Bitmap> cacheDeFiguras = new HashMap<>();
    private Context context;
    public WeatherAdapter (Context context, List <Weather> previsoes){
        super(context, -1, previsoes);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Weather caraDaVez = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.conditionImageView = convertView.findViewById(R.id.conditionImageView);
            viewHolder.dayTextView = convertView.findViewById(R.id.dayTextView);
            viewHolder.lowTextView = convertView.findViewById(R.id.lowTextView);
            viewHolder.highTextView = convertView.findViewById(R.id.highTextView);
            viewHolder.humidityTextView = convertView.findViewById(R.id.humidityTextView);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        if (cacheDeFiguras.containsKey(caraDaVez.iconName)){
            viewHolder.conditionImageView.setImageBitmap(cacheDeFiguras.get(caraDaVez.iconName));
        }
        else{
            new BaixaImagem(viewHolder.conditionImageView, caraDaVez.iconName).execute(context.getString(R.string.image_download_url, caraDaVez.iconName));
        }
        viewHolder.dayTextView.setText(context.
                getString(R.string.day_description, caraDaVez.dayOfWeek,
                        caraDaVez.description));
        viewHolder.lowTextView.setText(context.
                getString(R.string.low_temp, caraDaVez.minTemp));
        viewHolder.highTextView.setText(context.
                getString(R.string.high_temp, caraDaVez.maxTemp));
        viewHolder.humidityTextView.setText(context.
                getString(R.string.humidity, caraDaVez.humidity));
        return convertView;
    }

    private class ViewHolder{
        public ImageView conditionImageView;
        public TextView dayTextView;
        public TextView lowTextView;
        public TextView highTextView;
        public TextView humidityTextView;
    }

    private class BaixaImagem extends AsyncTask <String, Void, Bitmap>{

        private ImageView conditionImageView;
        private String iconName;
        public BaixaImagem (ImageView conditionImageView, String iconName){
            this.conditionImageView = conditionImageView;
            this.iconName = iconName;
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
           try{
               URL url = new URL (urls[0]);
               HttpURLConnection connection = (HttpURLConnection) url.openConnection();
               InputStream inputStream = connection.getInputStream();
               Bitmap figura = BitmapFactory.decodeStream(inputStream);
              return figura;
           }
           catch (Exception e){
               e.printStackTrace();
               return null;
           }
        }

        @Override
        protected void onPostExecute(Bitmap figura) {
            conditionImageView.setImageBitmap(figura);
            cacheDeFiguras.put(iconName, figura);
        }
    }
}
