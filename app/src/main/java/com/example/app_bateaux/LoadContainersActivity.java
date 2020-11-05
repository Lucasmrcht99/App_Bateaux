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
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class LoadContainersActivity extends Activity {

    ObjectInputStream ois=null;
    ObjectOutputStream oos=null;
    private Socket cliSock;
    private Spinner spinner;
    private Button btnSubmit;
    private ImageButton btnReturn;
    private Button btnLoadCont;
    private TextView boatInfo;
    private TextView boatCapacite;
    private CheckBox checkBox;
    public Intent suite;
    private ListView mListView;
    private String containerId;
    private boolean stop = true;
    private LoadContainersActivity context = this;
    private Containers selectedContainer=null;
    private String containersLoad="";
    private String mode="0";
    private Bateau boat;
    private int pos;
    ArrayList<Containers> ListContainers;
    private int nombre=1;
    private int capacite;

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
        checkBox = (CheckBox)findViewById(R.id.checkBox1);
        mListView = (ListView) findViewById(R.id.ListContainers2);
        btnReturn = (ImageButton) findViewById(R.id.imageButtonReturn);
        btnLoadCont = (Button) findViewById(R.id.buttonLoadContainer);
        boatInfo = (TextView) findViewById(R.id.textViewBoatInfo);
        boatCapacite = (TextView) findViewById(R.id.textViewBoatInfo2);

        boat = MenuActivity.getBoat();
        boatInfo.setText(boat.getId() + " - " + boat.getDestination());
        boatCapacite.setText("Places restantes : " + boat.getCapacite());
        capacite = Integer.parseInt(boat.getCapacite());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.removeAllViewsInLayout();
                if(checkBox.isChecked())
                {
                    mode="1";
                }
                else
                {
                    mode="0";
                }
                System.out.println(mode);
                DoGetContainers doGetCont = new DoGetContainers(boat.getDestination(), mode, context);
                doGetCont.doInBackground();
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop=false;
                finish();
            }
        });

        btnLoadCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnLoadCont.getText().toString().equalsIgnoreCase("ANNULER CHARGEMENT"))
                {
                    boat=null;
                    MenuActivity.setBoatNull();
                    stop=false;
                    finish();
                }
                else if(btnLoadCont.getText().toString().equalsIgnoreCase("CHARGER UN CONTAINER"))
                {
                    if(selectedContainer!=null)
                    {
                        if(capacite > 0)
                        {
                            DoHandleContainerOut doHandleContainerOut = new DoHandleContainerOut(context, selectedContainer.getId(), mode, boat.getDestination(), nombre);
                            int code = doHandleContainerOut.doInBackground();
                            if(code == ReponseIOBREP.OK)
                            {
                                checkBox.setEnabled(false);
                                btnSubmit.setEnabled(false);
                                capacite--;
                                boatCapacite.setText("Places restantes : " + capacite);
                                if(capacite==0) {
                                    btnLoadCont.setText("Finir chargement");
                                }
                                if(containersLoad.equals("")) {
                                    containersLoad = selectedContainer.getId();
                                }
                                else {
                                    containersLoad = containersLoad +  "&" + selectedContainer.getId();
                                }
                                removeList();
                                selectedContainer = null;
                                nombre++;
                                AfficheToast.Affiche("Container chargé !", context);
                            }
                            else
                            {
                                if(code == ReponseIOBREP.FAIL)
                                {
                                    AfficheToast.Affiche("Not first container !", context);
                                }
                                else
                                {
                                    AfficheToast.Affiche("Container inexistant !", context);
                                    removeList();
                                }
                            }
                        }
                        else
                        {
                            AfficheToast.Affiche("Plus de place disponible dans le bateau !", context);
                        }
                    }
                }
                else if(btnLoadCont.getText().toString().equalsIgnoreCase("Finir chargement"))
                {
                    DoFinishLoading doFinishLoading = new DoFinishLoading(containersLoad,boat.getId(),context,boat.getDestination());
                    doFinishLoading.doInBackground();
                    stop=false;
                }
            }
        });
    }

    public void setListView(String str)
    {
        ListContainers = new ArrayList<Containers>();

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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedContainer = (Containers) adapter.getItem(position);
                pos = position;
            }
        });
    }

    public void removeList()
    {
        ListContainers.remove(pos);
        mListView.removeAllViewsInLayout();
        ContainerAdapter adapter;
        if(ListContainers.size() == 0)
        {
            btnLoadCont.setText("Finir chargement");
            adapter = new ContainerAdapter(this, new ArrayList<Containers>());
            mListView.setAdapter(adapter);
            selectedContainer = null;
        }
        else
        {
            adapter = new ContainerAdapter(this,ListContainers);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedContainer = (Containers) adapter.getItem(position);
                    pos = position;
                }
            });
        }
    }

    public void setBtnTxt()
    {
        btnLoadCont.setText("ANNULER CHARGEMENT");
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
