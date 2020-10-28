package com.example.app_bateaux;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class LoadContainers extends Activity {

    ObjectInputStream ois=null;
    ObjectOutputStream oos=null;
    private Socket cliSock;
    private Spinner spinner;
    private Button btnSubmit;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_containers);
        cliSock=SocketHandler.getSock();

        addItemsOnSpinner();


        btnSubmit = (Button) findViewById(R.id.buttonRecherche);
        LoadContainers context = this;

        checkBox = (CheckBox)findViewById(R.id.checkBox1);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mode="0";
                String chaine = String.valueOf(spinner.getSelectedItem());
                if(chaine.equals(""))
                {
                    //Toast
                }
                else
                {
                    if(checkBox.isSelected())
                    {
                        mode="1";
                    }


                }


                ListView mListView;
                ArrayList<Containers> ListContainers = new ArrayList<Containers>();
                Containers c = new Containers("5,2","C001","17/05/20","15");
                ListContainers.add(c);
                ListContainers.add(c);
                ListContainers.add(c);

                mListView = (ListView) findViewById(R.id.ListContainers);

                ContainerAdapter adapter = new ContainerAdapter(context,ListContainers);
                mListView.setAdapter(adapter);
            }
        });


    }

    public void addItemsOnSpinner() {

        spinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("");

        RequeteIOBREP req = new RequeteIOBREP(RequeteIOBREP.GET_DESTINATIONS, "");
        try
        {
            System.out.println("Envoi de la requete");
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            oos.flush();

            ReponseIOBREP rep = null;
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
