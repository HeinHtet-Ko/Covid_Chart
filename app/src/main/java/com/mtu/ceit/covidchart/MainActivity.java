package com.mtu.ceit.covidchart;


import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mtu.ceit.covidchart.Models.Chart;
import com.mtu.ceit.covidchart.Models.Globe;
import com.mtu.ceit.covidchart.Repositories.ChartHolderApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.ibrahimsn.lib.SmoothBottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;
    private ViewFlipper viewFlipper;
    private SmoothBottomBar smoothBottomBar;
    private FloatingActionButton fab;
    private TextView global_desc;
    private ArrayList<String> conditions;
    private ProgressDialog pd;
    private AutoCompleteTextView autoCompleteTextView ;
    private boolean viewflag;
    private List<String> strArr;
    private Legend legend;
    private Boolean isNightMode;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewFlipper = findViewById(R.id.view_flipper);
        pieChart = findViewById(R.id.pie_Chart);
        barChart = findViewById(R.id.bar_Chart_Global);
        smoothBottomBar= findViewById(R.id.bottomBar);
        fab = findViewById(R.id.FAB);
        global_desc = findViewById(R.id.global_description);
        pd = new ProgressDialog(MainActivity.this);
        isNightMode=false;

        autoCompleteTextView = new AutoCompleteTextView(MainActivity.this);

        conditions = new ArrayList<>();
        conditions.add("Recovered  ");
        conditions.add("Active     " );
        conditions.add("Deaths    ");


        getSingleCountryApi("Burma");
        getGlobal();
        countryNamesArray();
        viewflag = true;

        smoothBottomBar.setOnItemSelectedListener(i ->
        {
            if(i==0)
            {
                viewflag=true;
                viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
                viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
                fab.setImageResource(R.drawable.ic_search_black_24dp);
                global_desc.setVisibility(View.INVISIBLE);
                viewFlipper.showPrevious();
            }else
            {
                viewflag=false;
                viewFlipper.setInAnimation(this,R.anim.slight_right_in);
                viewFlipper.setOutAnimation(this,R.anim.slight_left_out);
                barChart.animateY(2000);
                fab.setImageResource(R.drawable.refresh);
                global_desc.setVisibility(View.VISIBLE);
                viewFlipper.showNext();
            }

        });
    }




    @Override
    protected void onStart() {
        super.onStart();
        legend= pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextSize(17f);
        legend.setYOffset(15f);
        pieChart.setCenterText("Myanmar");
    }

    void barChart_setUp(int recovered, int active, int deaths)
    {
        ArrayList<BarEntry> global_stats = new ArrayList<>();
        global_stats.add(new BarEntry(0,recovered));
        global_stats.add(new BarEntry(1,active));
        global_stats.add(new BarEntry(2,deaths));

        BarDataSet barDataSet = new BarDataSet(global_stats," ");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);

        Legend legend = barChart.getLegend();
        legend.setTextSize(15f);
        LegendEntry legendEntry_One = new LegendEntry();
        legendEntry_One.label="Recovered";
        LegendEntry legendEntry_Two = new LegendEntry();
        legendEntry_Two.label="Active";
        LegendEntry legendEntry_Three = new LegendEntry();
        legendEntry_Three.label="Deaths";


        LegendEntry[] legendEntries= new LegendEntry[] {legendEntry_One,legendEntry_Two,legendEntry_Three};
        for(int i=0;i<=2;i++)
        {
            legendEntries[i].formColor =  barDataSet.getColor(i);
        }
        legend.setCustom(legendEntries);

        Description description = barChart.getDescription();
        description.setEnabled(false);


        barChart.setData(barData);
        barChart.invalidate();
    }


    void getGlobal()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://covid19.mathdro.id/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ChartHolderApi chartHolderApi = retrofit.create(ChartHolderApi.class);
        Call<Globe> call = chartHolderApi.getGlobalData();
        call.enqueue(new Callback<Globe>() {
            @Override
            public void onResponse(Call<Globe> call, Response<Globe> response) {
                Globe gl = response.body();

                int recovered = gl.getRecovered().getValue();
                int deaths = gl.getDeaths().getValue();
                int active = gl.getConfirmed().getValue()-recovered-deaths;
                barChart_setUp(recovered,active,deaths);
                            }

            @Override
            public void onFailure(Call<Globe> call, Throwable t) {
                Toast.makeText(MainActivity.this, " Check Your Internet Connection ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void countryNamesArray()
    {
        String str ="Afghanistan\n" +
                "Albania\n" +
                "Algeria\n" +
                "Andorra\n" +
                "Angola\n" +
                "Antigua and Barbuda\n" +
                "Argentina\n" +
                "Armenia\n" +
                "Australia\n" +
                "Austria\n" +
                "Azerbaijan\n" +
                "Bahamas\n" +
                "Bahrain\n" +
                "Bangladesh\n" +
                "Barbados\n" +
                "Belarus\n" +
                "Belgium\n" +
                "Belize\n" +
                "Benin\n" +
                "Bhutan\n" +
                "Bolivia\n" +
                "Bosnia and Herzegovina\n" +
                "Botswana\n" +
                "Brazil\n" +
                "Brunei\n" +
                "Bulgaria\n" +
                "Burkina Faso\n" +
                "Burma\n" +
                "Burundi\n" +
                "Cabo Verde\n" +
                "Cambodia\n" +
                "Cameroon\n" +
                "Canada\n" +
                "Central African Republic\n" +
                "Chad\n" +
                "Chile\n" +
                "China\n" +
                "Colombia\n" +
                "Comoros\n" +
                "Congo (Brazzaville)\n" +
                "Congo (Kinshasa)\n" +
                "Costa Rica\n" +
                "Cote d'Ivoire\n" +
                "Croatia\n" +
                "Cuba\n" +
                "Cyprus\n" +
                "Czechia\n" +
                "Denmark\n" +
                "Diamond Princess\n" +
                "Djibouti\n" +
                "Dominica\n" +
                "Dominican Republic\n" +
                "Ecuador\n" +
                "Egypt\n" +
                "El Salvador\n" +
                "Equatorial Guinea\n" +
                "Eritrea\n" +
                "Estonia\n" +
                "Eswatini\n" +
                "Ethiopia\n" +
                "Fiji\n" +
                "Finland\n" +
                "France\n" +
                "Gabon\n" +
                "Gambia\n" +
                "Georgia\n" +
                "Germany\n" +
                "Ghana\n" +
                "Greece\n" +
                "Grenada\n" +
                "Guatemala\n" +
                "Guinea\n" +
                "Guinea-Bissau\n" +
                "Guyana\n" +
                "Haiti\n" +
                "Holy See\n" +
                "Honduras\n" +
                "Hungary\n" +
                "Iceland\n" +
                "India\n" +
                "Indonesia\n" +
                "Iran\n" +
                "Iraq\n" +
                "Ireland\n" +
                "Israel\n" +
                "Italy\n" +
                "Jamaica\n" +
                "Japan\n" +
                "Jordan\n" +
                "Kazakhstan\n" +
                "Kenya\n" +
                "Korea, South\n" +
                "Kosovo\n" +
                "Kuwait\n" +
                "Kyrgyzstan\n" +
                "Laos\n" +
                "Latvia\n" +
                "Lebanon\n" +
                "Lesotho\n" +
                "Liberia\n" +
                "Libya\n" +
                "Liechtenstein\n" +
                "Lithuania\n" +
                "Luxembourg\n" +
                "MS Zaandam\n" +
                "Madagascar\n" +
                "Malawi\n" +
                "Malaysia\n" +
                "Maldives\n" +
                "Mali\n" +
                "Malta\n" +
                "Mauritania\n" +
                "Mauritius\n" +
                "Mexico\n" +
                "Moldova\n" +
                "Monaco\n" +
                "Mongolia\n" +
                "Montenegro\n" +
                "Morocco\n" +
                "Mozambique\n" +
                "Namibia\n" +
                "Nepal\n" +
                "Netherlands\n" +
                "New Zealand\n" +
                "Nicaragua\n" +
                "Niger\n" +
                "Nigeria\n" +
                "North Macedonia\n" +
                "Norway\n" +
                "Oman\n" +
                "Pakistan\n" +
                "Panama\n" +
                "Papua New Guinea\n" +
                "Paraguay\n" +
                "Peru\n" +
                "Philippines\n" +
                "Poland\n" +
                "Portugal\n" +
                "Qatar\n" +
                "Romania\n" +
                "Russia\n" +
                "Rwanda\n" +
                "Saint Kitts and Nevis\n" +
                "Saint Lucia\n" +
                "Saint Vincent and the Grenadines\n" +
                "San Marino\n" +
                "Sao Tome and Principe\n" +
                "Saudi Arabia\n" +
                "Senegal\n" +
                "Serbia\n" +
                "Seychelles\n" +
                "Sierra Leone\n" +
                "Singapore\n" +
                "Slovakia\n" +
                "Slovenia\n" +
                "Somalia\n" +
                "South Africa\n" +
                "South Sudan\n" +
                "Spain\n" +
                "Sri Lanka\n" +
                "Sudan\n" +
                "Suriname\n" +
                "Sweden\n" +
                "Switzerland\n" +
                "Syria\n" +
                "Taiwan\n" +
                "Tajikistan\n" +
                "Tanzania\n" +
                "Thailand\n" +
                "Timor-Leste\n" +
                "Togo\n" +
                "Trinidad and Tobago\n" +
                "Tunisia\n" +
                "Turkey\n" +
                "US\n" +
                "Uganda\n" +
                "Ukraine\n" +
                "United Arab Emirates\n" +
                "United Kingdom\n" +
                "Uruguay\n" +
                "Uzbekistan\n" +
                "Venezuela\n" +
                "Vietnam\n" +
                "West Bank and Gaza\n" +
                "Western Sahara\n" +
                "Yemen\n" +
                "Zambia\n" +
                "Zimbabwe\n" +
                "Myanmar\n" +
                "United States\n" +
                "UK\n" +
                "Vatican City\n" +
                "UAE";

      strArr =  Arrays.asList(str.split("\n"));

      ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_list_item_1, strArr);
        autoCompleteTextView.setAdapter(adapter);

    }
    public void getSingleCountryApi(String country)
    {
        pd.show();
        ArrayList<Integer> pieData = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://covid19.mathdro.id/api/countries/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ChartHolderApi chartHolderApi = retrofit.create(ChartHolderApi.class);
        Call<Chart> call = chartHolderApi.getSingleCountry(country);
        call.enqueue(new Callback<Chart>() {
            @Override
            public void onResponse(Call<Chart> call, Response<Chart> response) {


                Chart c = response.body();
                    if(!c.equals(null))
                    {
                        pieData.add(c.getRecovered_cases().getValue());
                        pieData.add(c.getConfirmed_cases().getValue()-c.getRecovered_cases().getValue()-c.getDeath_cases().getValue());
                        pieData.add(c.getDeath_cases().getValue());
                        pieChart_setUp(pieData);
                        pieChart.setCenterText(country);
                    }

                pd.dismiss();

            }
            @Override
            public void onFailure(Call<Chart> call, Throwable t) {

                Toast.makeText(MainActivity.this, " Check Your Internet Connection and Try Again! ", Toast.LENGTH_SHORT).show();

            }
        });

    }
    
    void pieChart_setUp(ArrayList<Integer> myData)
    {

        List<PieEntry> pe = new ArrayList<>();
        int i=0;
        for(int a:myData)
        {

            pe.add(new PieEntry(a,conditions.get(i++)+"- "+a));
        }

        PieDataSet pds = new PieDataSet(pe,"  ");
        pds.setUsingSliceColorAsValueLineColor(false);
        pds.setSelectionShift(20);
        pds.setColors(ColorTemplate.MATERIAL_COLORS);
        pds.setValueLineColor(Color.BLACK);
        pds.setSliceSpace(5);
        pds.setValueLineColor(R.color.colorAccent);
        pds.isUsingSliceColorAsValueLineColor();
        pds.setValueFormatter(new PercentFormatter());



        PieData pd = new PieData(pds);
        pd.setValueTextSize(15f);

        pieChart.setData(pd);
        pieChart.setCenterTextColor(R.color.colorPrimaryDark);
        pieChart.setCenterTextSize(18f);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDragDecelerationFrictionCoef(0.4f);
        pieChart.animateY(1500, Easing.EaseInCubic);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setTransparentCircleRadius(70f);
        pieChart.invalidate();
    }

    public void show_dialog(View view)
    {
        if(viewflag)
        {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.addView(autoCompleteTextView);
            linearLayout.setPadding(40,30,30,30);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 20, 20, 20);
            autoCompleteTextView.setLayoutParams(params);
            new MaterialAlertDialogBuilder(this)
                    .setView(linearLayout)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            linearLayout.removeAllViews();
                            autoCompleteTextView.setText("");
                        }
                    })
                    .setTitle("Search For Each Country")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    })
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String countryName = autoCompleteTextView.getText().toString();


                           boolean flag=true;
                           if(!strArr.contains(countryName))
                           {
                               flag=false;
                               Snackbar.make(view,"Check Your Spelling And Try Again!! ",3000)
                                       .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                       .show();
                               //Toast.makeText(MainActivity.this, "Check Your Spelling Again!!!", Toast.LENGTH_LONG).show();
                           }
                            if(flag)
                            {

                                switch (countryName)
                                {
                                    case "Myanmar":
                                        countryName="Burma";
                                        break;
                                    case "United States":
                                        countryName="US";
                                        break;
                                    case "UK":
                                        countryName="United Kingdom";
                                        break;
                                    case "Vatican City":
                                        countryName="Holy See";
                                        break;
                                    case "UAE":
                                        countryName="United Arab Emirates";
                                        break;
                                }
                                getSingleCountryApi(countryName);
                            }



                        }
                    })
                    .show();

        }
        else {


            barChart.animateY(1500);
            getGlobal();
        }

    }


}
