package com.openin.listed;

import static com.github.mikephil.charting.data.LineDataSet.Mode.CUBIC_BEZIER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private LinkAdapter mAdapter;
    private final List<LinkModel> linkslist = new ArrayList<>();

    public LineChart lineChartDownFill;

    RecyclerView recyclerView;
    ArrayList<String> months = new ArrayList<String>();
    ArrayList<Entry> entrydata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MaterialButton tl = (MaterialButton) findViewById(R.id.tl_btn);
        MaterialButton rl = (MaterialButton) findViewById(R.id.rl_btn);
        TextView greet = findViewById(R.id.greeting);
         recyclerView = findViewById(R.id.recyclerView);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(current_hour>0 && current_hour<12){
            greet.setText("Good morning");
        }
        else if(current_hour>12 && current_hour<3){
            greet.setText("Good afternoon");
        }
        else if(current_hour>3 && current_hour<8){
            greet.setText("Good evening");
        }
        else{
            greet.setText("Good Night");
        }

        tl.setBackgroundColor(Color.parseColor("#0E6FFF"));

        tl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.topLinkFragment);
                tl.setTextColor(Color.WHITE);
                rl.setTextColor(Color.parseColor("#999CA0"));
                rl.setBackgroundColor(Color.parseColor("#00000000"));
                tl.setBackgroundColor(Color.parseColor("#0E6FFF"));

            }
        });

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.recentLinkFragment);
                rl.setTextColor(Color.WHITE);
                tl.setTextColor(Color.parseColor("#999CA0"));
                rl.setBackgroundColor(Color.parseColor("#0E6FFF"));
                tl.setBackgroundColor(Color.parseColor("#00000000"));
            }
        });


        lineChartDownFill = findViewById(R.id.getTheGraph);
        lineChartDownFill.setTouchEnabled(true);
        lineChartDownFill.setDragEnabled(true);
        lineChartDownFill.setScaleEnabled(true);
        lineChartDownFill.setPinchZoom(true);
        lineChartDownFill.setDrawGridBackground(false);
        lineChartDownFill.getDescription().setEnabled(false);
        lineChartDownFill.setMaxHighlightDistance(0);

            requestWithSomeHttpHeaders();
    }





    private void lineChartDownFillWithData() {
        LineDataSet lineDataSet = new LineDataSet(entrydata, "");

        lineDataSet.setLineWidth(3f);
        lineDataSet.setColor(Color.parseColor("#0E6FFF"));

        lineDataSet.setDrawValues(false);
        lineDataSet.setCircleRadius(5f);

        ArrayList<String> xAxisValues = new ArrayList<String>();



        for (String key : months) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd");
            Date parsedDate = null;
            try {
                parsedDate = inputFormat.parse(key);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = outputFormat.format(Objects.requireNonNull(parsedDate));
            xAxisValues.add(formattedDate);

        }


       lineChartDownFill.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));

        XAxis xAxis = lineChartDownFill.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxis = lineChartDownFill.getAxisRight();
        yAxis.setEnabled(false);

        lineDataSet.setCubicIntensity(0.2f);


        lineDataSet.setDrawFilled(true);

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.gradient);
        lineDataSet.setFillDrawable(drawable);


        Legend legend = lineChartDownFill.getLegend();
        legend.setEnabled(false);


        lineDataSet.setDrawCircles(false);



        ArrayList<ILineDataSet> iLineDataSetArrayList = new ArrayList<>();
        iLineDataSetArrayList.add(lineDataSet);


        LineData lineData = new LineData(iLineDataSetArrayList);
        lineData.setValueTextSize(13f);
        lineData.setValueTextColor(Color.BLACK);

        lineChartDownFill.setData(lineData);
        lineChartDownFill.invalidate();


    }

    public void requestWithSomeHttpHeaders()  {
        RequestQueue queue = Volley.newRequestQueue(this);
        entrydata = new ArrayList<>();
        String url = "https://api.inopenapp.com/api/v1/dashboardNew";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject array = new JSONObject(response);
                            String tod_click= array.getString("today_clicks");
                            String tot_links= array.getString("total_links");
                            String tot_clicks= array.getString("total_clicks");
                            String source= array.getString("top_source");
                            String location= array.getString("top_location");

                            JSONObject data = array.getJSONObject("data");
                            JSONObject dates = data.getJSONObject("overall_url_chart");


                            for(int i = 0; i< Objects.requireNonNull(dates.names()).length(); i++){
                                Object p =  dates.get(Objects.requireNonNull(dates.names()).getString(i));
                                float y = ((Integer) p).floatValue();
                                months.add(Objects.requireNonNull(dates.names()).getString(i));
                                entrydata.add(new Entry(i, y));
                                Log.i("TAG", "key = " + Objects.requireNonNull(dates.names()).getString(i) + " value = " + dates.get(Objects.requireNonNull(dates.names()).getString(i)));
                            }

                            if(location.equals("")){
                                location="N/A";
                            }

                            if(source.equals("")){
                                source = "N/A";
                            }

                            LinkModel link = new LinkModel("Today's Clicks", tod_click);
                            linkslist.add(link);
                            link = new LinkModel("Total Links", tot_links);
                            linkslist.add(link);
                            link = new LinkModel("Total Clicks", tot_clicks);
                            linkslist.add(link);
                            link = new LinkModel("Top Source", source);
                            linkslist.add(link);
                            link = new LinkModel("Top Location", location);
                            linkslist.add(link);

                            lineChartDownFillWithData();


                            mAdapter = new LinkAdapter(linkslist,getApplicationContext());
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);






                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI");
                return params;
            }
        };
        queue.add(getRequest);

    }
}