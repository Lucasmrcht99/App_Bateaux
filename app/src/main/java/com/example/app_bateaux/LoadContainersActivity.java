package com.example.app_bateaux;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
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

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class LoadContainersActivity extends Activity {

    ObjectInputStream ois=null;
    ObjectOutputStream oos=null;
    private Socket cliSock;
    private Spinner spinner;
    private Button btnSubmit;
    private ImageButton btnReturn;
    private CheckBox checkBox;
    public Intent suite;
    private ListView mListView;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_containers);
        cliSock=SocketHandler.getSock();
        user = (String)this.getIntent().getExtras().get("User");
        addItemsOnSpinner();

        btnSubmit = (Button) findViewById(R.id.buttonRecherche);
        LoadContainersActivity context = this;
        checkBox = (CheckBox)findViewById(R.id.checkBox1);
        mListView = (ListView) findViewById(R.id.ListContainers);
        btnReturn = (ImageButton) findViewById(R.id.imageButtonReturn);

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
                suite = new Intent(context, MenuActivity.class);
                suite.putExtra("User", user);
                context.startActivity(suite);
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

    public void addItemsOnSpinner() {

        spinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("");

        RequeteIOBREP req = new RequeteIOBREP(RequeteIOBREP.GET_DESTINATIONS, "");
        try {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            oos.flush();
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", this);
            suite = new Intent(this, LoginActivity.class);
            this.startActivity(suite);
            this.finish();
        }
        // Lecture de la réponse
        ReponseIOBREP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseIOBREP)ois.readObject();

            if(rep.getCode() == ReponseIOBREP.OK)
            {
                if(!rep.getChargeUtile().equals(""))
                {
                    StringTokenizer st = new StringTokenizer(rep.getChargeUtile(), "&");
                    while(st.hasMoreTokens())
                        list.add(st.nextToken());
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", this);
            suite = new Intent(this, LoginActivity.class);
            this.startActivity(suite);
            this.finish();
        }
        catch (ClassNotFoundException  e)
        {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            System.out.println("YOOLOOOOOOOOOOO");
            RequeteIOBREP req2 = new RequeteIOBREP(RequeteIOBREP.CLOSE, "");
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req2);
            oos.flush();
        }
        catch (IOException e) {
            System.out.println("Connexion au serveur perdue");
        }
    }
}
