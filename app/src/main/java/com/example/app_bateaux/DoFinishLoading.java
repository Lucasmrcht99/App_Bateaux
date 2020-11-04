package com.example.app_bateaux;

import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class DoFinishLoading extends AsyncTask<Void, Void, Void> {

    private String List;
    private String Trans;
    private LoadContainersActivity loadAct;

    public DoFinishLoading(String list,String trans, LoadContainersActivity context)
    {
        List = list;
        loadAct = context;
        Trans=trans;

    }

    @Override protected void onPreExecute() { super.onPreExecute(); }

    @Override protected void onProgressUpdate(Void... arg1)
    {
        super.onProgressUpdate();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        ObjectInputStream ois=null;
        ObjectOutputStream oos=null;
        RequeteIOBREP req = new RequeteIOBREP(RequeteIOBREP.END_CONTAINER_OUT, "");
        Socket cliSock = SocketHandler.getSock();
        try
        {
            req.setChargeUtile(Trans+"&"+List);
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            oos.flush();
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
                AfficheToast.Affiche( "Connexion au serveur perdue", loadAct);
                loadAct.suite = new Intent(loadAct, LoginActivity.class);
                loadAct.startActivity(loadAct.suite);
                loadAct.finish();



            return null;
        }
        // Lecture de la réponse
        ReponseIOBREP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseIOBREP)ois.readObject();

                if (rep.getCode() == ReponseIOBREP.OK) {
                    loadAct.finish();
                    MenuActivity.setBoatNull();
                    AfficheToast.Affiche(rep.getChargeUtile(), MenuActivity.getContext());

                }
                else {
                    loadAct.finish();
                    MenuActivity.setBoatNull();
                    AfficheToast.Affiche(rep.getChargeUtile(), MenuActivity.getContext());
                }
        }
        catch (ClassNotFoundException  e)
        {
            System.out.println("Erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
                AfficheToast.Affiche( "Connexion au serveur perdue", loadAct);
                loadAct.suite = new Intent(loadAct, LoginActivity.class);
                loadAct.startActivity(loadAct.suite);
                loadAct.finish();
        }
        return null ;
    }
}
