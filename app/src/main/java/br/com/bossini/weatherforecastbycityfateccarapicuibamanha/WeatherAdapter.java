package br.com.bossini.weatherforecastbycityfateccarapicuibamanha;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WeatherAdapter extends ArrayAdapter <Weather> {

    private Context context;
    public WeatherAdapter (Context context, List <Weather> previsoes){
        super(context, -1, previsoes);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Weather caraDaVez = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        TextView lowTextView = view.findViewById(R.id.lowTextView);
        return null;
    }
}
