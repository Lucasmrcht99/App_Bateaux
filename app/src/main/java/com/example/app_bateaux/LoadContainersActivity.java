package com.example.app_bateaux;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import java.util.StringTokenizer;

import req_rep_IOBREP.RequeteIOBREP;

public class LoadContainersActivity extends Activity {

    ObjectInputStream ois=null;
    ObjectOutputStream oos=null;
    private Socket cliSock;
    private Spinner spinner;
    private Button btnSubmit;
    private ImageButton btnReturn;
    private Button btnLoadCont;
    private CheckBox checkBox;
    public Intent suite;
    private ListView mListView;
    private String containerId;
    private boolean stop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_containers);
        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(getColor(R.color.blue_app));
        cliSock=SocketHandler.getSock();
        btnSubmit = (Button) findViewById(R.id.buttonRecherche);
        LoadContainersActivity context = this;
        checkBox = (CheckBox)findViewById(R.id.checkBox1);
        mListView = (ListView) findViewById(R.id.ListContainers2);
        btnReturn = (ImageButton) findViewById(R.id.imageButtonReturn);
        btnLoadCont = (Button) findViewById(R.id.buttonLoadContainer);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.removeAllViewsInLayout();
                String mode="0";
                String chaine = "Flemalle";
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0; i < parent.getChildCount(); i++)
                {
                    parent.getChildAt(i).setBackgroundColor(Color.parseColor("#0b7990"));
                }
                parent.getChildAt(position).setBackgroundColor(Color.parseColor("#00384d"));
                Containers c = (Containers)mListView.getItemAtPosition(position);
                containerId = c.getId();
                System.out.println(containerId);
            }
        });

        btnLoadCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
