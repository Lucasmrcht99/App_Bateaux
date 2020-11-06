package com.example.app_bateaux;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.animation.EasingFunction;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import req_rep_IOBREP.RequeteIOBREP;

public class AffPie extends Activity {

    private DatabaseManager db;
    private Context context = this;
    private boolean mode=false;
    ObjectOutputStream oos=null;
    private Socket cliSock;
    private boolean stop = true;
    private ImageButton btnReturn;
    private RadioButton r1;
    private android.widget.RadioButton r2;
    private PieChart pie;
    private Button aff ;
    private ArrayList<String> listtop;
    private ArrayList<String> dest;
    private ArrayList<PieEntry> containers;
    private EditText date;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(getColor(R.color.blue_app));
        cliSock = SocketHandler.getSock();
        btnReturn = (ImageButton) findViewById(R.id.imageButtonReturn8);
        aff = findViewById(R.id.buttonAfficher2);
        pie = findViewById(R.id.pie);
        r1 = findViewById(R.id.radioButton);
        r2 = findViewById(R.id.radioButton2);
        date= findViewById(R.id.editTextDate);
        r1.setChecked(true);


        pie.setUsePercentValues(true);

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

                dest = new ArrayList<String>();
                listtop = new ArrayList<String>();
                pie.getDescription().setEnabled(false);
                pie.setExtraOffsets(5, 10, 5, 5);
                pie.setDragDecelerationFrictionCoef(0.95f);
                if(r1.isChecked())
                {
                    mode = false;
                }
                else
                {
                    mode = true;
                }
                pie.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

                pie.setDrawHoleEnabled(true);
                pie.setHoleColor(getColor(R.color.blue_app));

                pie.setTransparentCircleColor(getColor(R.color.blue_app));
                pie.setTransparentCircleAlpha(110);

                StringTokenizer st = new StringTokenizer(date.getText().toString(),"/");
                String annee = st.nextToken();
                String mois = st.nextToken();
                int jour = Integer.parseInt(st.nextToken())+6;
                String chaine = annee+"/"+mois+"/"+jour;

                pie.setCenterText(date.getText().toString() +" -> "+chaine);
                pie.setCenterTextColor(Color.WHITE);

                pie.setHoleRadius(58f);
                pie.setTransparentCircleRadius(61f);

                pie.setDrawCenterText(true);

                pie.setRotationAngle(0);
                pie.setRotationEnabled(true);
                pie.setHighlightPerTapEnabled(true);

                Legend l = pie.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);
                l.setTextColor(Color.WHITE);
                l.setTextSize(10f);
                l.setDrawInside(false);
                l.setXEntrySpace(7f);
                l.setYEntrySpace(0f);
                l.setYOffset(0f);


                pie.animateY(1400, EaseInOutQuad);
                db = new DatabaseManager(context);
                dest =db.recupDatespie(mode,date.getText().toString());
                db.close();
                int taille;

                if(!dest.get(0).equals(" ")) {
                    taille = dest.size();
                    containers = new ArrayList<>();
                    int compteur;


                    for (int i = 0, j = 1; i < taille; j++) {
                        compteur = 1;
                        String dateverif = dest.get(i).toString();
                        listtop.add(dateverif);

                        i++;
                        while (i < taille && (dest.get(i).equalsIgnoreCase(dateverif))) {
                            compteur++;
                            i++;
                        }
                        containers.add(new PieEntry(compteur,dateverif));
                    }
                    PieDataSet dataSet;

                    dataSet = new PieDataSet(containers, "List destination");

                    ArrayList<Integer> colors = new ArrayList<>();
                    colors.add(Color.parseColor("#2ecc71"));
                    colors.add(Color.parseColor("#f1c40f"));
                    colors.add(Color.parseColor("#e74c3c"));
                    colors.add(Color.parseColor("#3498db"));
                    colors.add(Color.parseColor("#15B8A2"));
                    colors.add(Color.parseColor("#D56587"));
                    colors.add(Color.parseColor("#267213"));
                    colors.add(Color.parseColor("#748D21"));
                    colors.add(Color.parseColor("#CA801F"));
                    colors.add(Color.parseColor("#D00B4C"));
                    colors.add(Color.parseColor("#C90BD0"));
                    colors.add(Color.parseColor("#FE6F00"));


                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(colors);
                    dataSet.setValueLinePart1OffsetPercentage(80.f);
                    dataSet.setValueLinePart1Length(0.2f);
                    dataSet.setValueLinePart2Length(0.4f);

                    PieData data = new PieData(dataSet);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextSize(11f);
                    data.setValueTextColor(Color.WHITE);
                    pie.setData(data);

                    pie.setVisibility(View.VISIBLE);
                }
                else {
                    AfficheToast.Affiche("Aucun containers", context);
                    pie.setVisibility(View.INVISIBLE);
                }

            }
        });

    }
    private class MyAxisValueFormattery implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return listtop.get((int) value - 1);
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

    @Override
    public void onBackPressed() {
        stop=false;
        finish();
    }

    public static final EasingFunction EaseInOutQuad = new EasingFunction() {
        public float getInterpolation(float input) {
            input *= 2f;

            if (input < 1f) {
                return 0.5f * input * input;
            }

            return -0.5f * ((--input) * (input - 2f) - 1f);
        }
    };
}