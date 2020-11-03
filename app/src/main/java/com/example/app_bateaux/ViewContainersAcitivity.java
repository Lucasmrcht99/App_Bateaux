package com.example.app_bateaux;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import req_rep_IOBREP.RequeteIOBREP;

public class ViewContainersAcitivity extends AppCompatActivity {
    ObjectInputStream ois=null;
    ObjectOutputStream oos=null;
    private Socket cliSock;
    private Spinner spinner;
    private Button btnSubmit;
    private ImageButton btnReturn;
    private CheckBox checkBox;
    public Intent suite;
    private ListView mListView;
    private boolean stop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.view_containers);
        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(getColor(R.color.blue_app));
        cliSock=SocketHandler.getSock();
        btnSubmit = (Button) findViewById(R.id.buttonRecherche3);
        ViewContainersAcitivity context = this;
        checkBox = (CheckBox)findViewById(R.id.checkBox2);
        mListView = (ListView) findViewById(R.id.ListContainers2);
        btnReturn = (ImageButton) findViewById(R.id.imageButtonReturn3);

        DoGetDestinations doGetDest = new DoGetDestinations(this);
        doGetDest.doInBackground();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.removeAllViewsInLayout();
                String mode="0";
                String chaine = String.valueOf(spinner.getSelectedItem());
                if(chaine.equals(""))
                {
                    AfficheToast.Affiche("Selectionnez une destination", context);
                }
                else
                {
                    if(checkBox.isChecked())
                    {
                        mode="1";
                    }
                    System.out.println(mode);
                    DoGetContainers doGetCont = new DoGetContainers(chaine, mode, context);
                    doGetCont.doInBackground();
                }
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop=false;
                finish();
            }
        });
    }

    public void setListView(String str)
    {
        ArrayList<Containers> ListContainers = new ArrayList<Containers>();

        StringTokenizer st1 = new StringTokenizer(str, "&");
        while(st1.hasMoreElements())
        {
            StringTokenizer st2 = new StringTokenizer(st1.nextToken(), "|");
            Containers c = new Containers(st2.nextToken(),st2.nextToken(),st2.nextToken(),st2.nextToken());
            ListContainers.add(c);
        }
        mListView.removeAllViewsInLayout();
        ContainerAdapter adapter = new ContainerAdapter(this,ListContainers);
        mListView.setAdapter(adapter);
    }

    public void addItemsOnSpinner(String str) {

        spinner = (Spinner) findViewById(R.id.spinner3);
        List<String> list = new ArrayList<String>();
        list.add("");

        StringTokenizer st = new StringTokenizer(str, "&");
        while(st.hasMoreTokens())
            list.add(st.nextToken());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onBackPressed() {
        stop=false;
        finish();
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