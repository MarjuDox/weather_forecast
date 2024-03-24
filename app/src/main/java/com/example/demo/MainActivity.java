package com.example.demo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    TextView tvCityName,tvCondition,tvTemp;
    TextInputEditText edtCTName;
    ImageView ivSearch,ivIcon,ivBack;
    ProgressBar pbLoading;
    RelativeLayout rlHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        String cityDefault = "hanoi";
        getWeatherInfo(cityDefault);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city= edtCTName.getText().toString();
                if (city.isEmpty() ){
                    Toast.makeText(MainActivity.this, "Please enter city name", Toast.LENGTH_SHORT).show();
                }
                else{
                    getWeatherInfo(city);
                    tvCityName.setText(city);
                }
            }
        });
    }
    public void init(){
        tvCityName = findViewById(R.id.tvCityName);
        tvTemp = findViewById(R.id.tvTemperature);
        edtCTName = findViewById(R.id.edtCTName);
        ivSearch = findViewById(R.id.ivSearch);
        ivIcon = findViewById(R.id.ivIcon);
        ivBack = findViewById(R.id.ivBack);
        tvCondition = findViewById(R.id.tvCondition);
        pbLoading = findViewById(R.id.pbLoading);
        rlHome = findViewById(R.id.rlHome);
    }

    public void getWeatherInfo(String cityName) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName.toLowerCase() + "&appid=b5cdea94b17249f2d1b241d5389fdd1c";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pbLoading.setVisibility(View.GONE);
                rlHome.setVisibility(View.VISIBLE);
                tvCityName.setText(cityName);
                try {
                    // Get kelvin temperature
                    JSONObject arrayMain = response.getJSONObject("main");
                    String tempKV = arrayMain.getString("temp");
                    // icon weather
                    JSONArray arrayWeather = response.getJSONArray("weather");
                    String icon = arrayWeather.getJSONObject(0).getString("icon");
                    String weather = arrayWeather.getJSONObject(0).getString("main");
                    // Check day or night

                    String is_day = icon.substring(2);
                    if ( is_day.equals("d")){
                        ivBack.setImageResource(R.drawable.background_morning);
                    } else {
                        ivBack.setImageResource(R.drawable.background_night);
                    }
                    // Convert kelvin to celsius
                    Double celsius = Double.parseDouble(tempKV) - 273.15;
                    String temp = celsius.toString().substring(0,4) + (char) 0x00B0 + "C";
                    tvTemp.setText(temp);
                    // Get icon
                    String iconURL = "http://openweathermap.org/img/wn/" + icon + "@4x.png";
                    Picasso.get().load(iconURL).into(ivIcon);
                    // Get condition
                    tvCondition.setText(weather);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Errors of omission occur as a result of actions not taken", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}