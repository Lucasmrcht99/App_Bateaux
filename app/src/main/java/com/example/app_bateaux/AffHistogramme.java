package com.example.app_bateaux;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import req_rep_IOBREP.RequeteIOBREP;

public class AffHistogramme extends Activity implements SeekBar.OnSeekBarChangeListener {

    private Button aff ;
    private BarChart bar;
    private DatabaseManager db;
    private Context context = this;
    private boolean mode=false;
    private RadioButton r1;
    private RadioButton r2;
    ObjectOutputStream oos=null;
    private Socket cliSock;
    private boolean stop = true;
    private ImageButton btnReturn;
    private ArrayList<String> listtop;
    private SeekBar seekBarX;
    private ArrayList<String> dates;
    private ArrayList<BarEntry> containers;
    private int max;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogramme);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(getColor(R.color.blue_app));
        cliSock=SocketHandler.getSock();

        aff = findViewById(R.id.buttonAfficher);
        bar = findViewById(R.id.Barchart);
        r1 = findViewById(R.id.radioButton);
        r2 = findViewById(R.id.radioButton2);
        r1.setChecked(true);
        btnReturn = (ImageButton) findViewById(R.id.imageButtonReturn7);
        seekBarX = findViewById(R.id.seekBar);
        seekBarX.setOnSeekBarChangeListener(this);
        seekBarX.setVisibility(View.INVISIBLE);






        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop=false;
                finish();
            }
        });

        aff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                max =1;
                db = new DatabaseManager(context);
                dates = new ArrayList<String>();
                listtop = new ArrayList<String>();
                if(r1.isChecked())
                {
                    mode = false;
                }
                else
                {
                    mode = true;
                }

                dates =db.recupDates(mode);
                db.close();
                int taille;

                if(!dates.get(0).equals(" ")) {
                    taille = dates.size();
                    containers = new ArrayList<>();
                    int compteur;


                    for (int i = 0, j = 1; i < taille; j++) {
                        compteur = 1;
                        String dateverif = dates.get(i).toString();
                        listtop.add(dateverif);

                        i++;
                        max++;
                        while (i < taille && (dates.get(i).equalsIgnoreCase(dateverif))) {
                            compteur++;
                            i++;
                        }
                        containers.add(new BarEntry(j, compteur));
                    }
                    seekBarX.setMax(max - 5);
                    seekBarX.setProgress(0);
                    seekBarX.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.VISIBLE);
                }
                else {
                    seekBarX.setVisibility(View.INVISIBLE);
                    AfficheToast.Affiche("Aucun containers", context);
                    bar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        stop=false;
        finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        btnReturn.setEnabled(false);
        BarDataSet data;

        XAxis xAxis = bar.getXAxis();
        xAxis.setValueFormatter(new MyAxisValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(11f);
        xAxis.setLabelCount(3,false);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        bar.getXAxis().setAxisMinimum(seekBarX.getProgress()+0.5f);
        bar.getXAxis().setAxisMaximum(seekBarX.getProgress()+4.5f);

        YAxis leftAxis = bar.getAxisLeft();
        leftAxis.setValueFormatter(new MyAxisValueFormattery());
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.WHITE);

        YAxis rightAxis = bar.getAxisRight();
        rightAxis.setValueFormatter(new MyAxisValueFormattery());
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setTextColor(Color.WHITE);

        if(mode==false)
        {
            data = new BarDataSet(containers, "Containeur déchargé par jour");
        }
        else
        {
            data = new BarDataSet(containers, "Containeur chargé par jour");
        }


        data.setColors(Color.WHITE);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(16f);

        Legend l = bar.getLegend();
        l.setTextSize(14f);
        l.setTextColor(Color.WHITE);

        BarData bardata = new BarData(data);
        bar.setPinchZoom(false);
        bar.setDoubleTapToZoomEnabled(false);
        bar.setData(bardata);
        bar.getDescription().setText("");
        bar.animateY(2000);
        btnReturn.setEnabled(true);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private class MyAxisValueFormatter implements IAxisValueFormatter{
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (listtop.size() < 4 && value>= 4)
            {
                return " ";
            }
            else
            {
                return listtop.get((int) value - 1);
            }
        }
    }

    private class MyAxisValueFormattery implements IAxisValueFormatter{
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            DecimalFormat mFormat = new DecimalFormat("###,###,###,##0.0");
            return mFormat.format(value);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(stop==true) {
            try {
                RequeteIOBREP req2 = new RequeteIOBREP(RequeteIOBREP.CLOSE, "");
                oos = new ObjectOutputStream(cliSock.getOutputStream());
                oos.writeObject(req2);
                oos.flush();
            } catch (IOException e) {
                System.out.println("Connexion au serveur perdue");
            }
        }

    }
}
