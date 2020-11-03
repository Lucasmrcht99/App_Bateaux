package com.example.app_bateaux;

import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class DoGetDestinations  extends AsyncTask<Void, Void, Void> {

    private ViewContainersAcitivity viewContAct;

    public DoGetDestinations(ViewContainersAcitivity context)
    {
        viewContAct = context;
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
        RequeteIOBREP req = new RequeteIOBREP(RequeteIOBREP.GET_DESTINATIONS, "");
        Socket cliSock = SocketHandler.getSock();
        try {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            oos.flush();
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", viewContAct);
            viewContAct.suite = new Intent(viewContAct, LoginActivity.class);
            viewContAct.startActivity(viewContAct.suite);
            viewContAct.finish();
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
                    viewContAct.addItemsOnSpinner(rep.getChargeUtile());
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", viewContAct);
            viewContAct.suite = new Intent(viewContAct, LoginActivity.class);
            viewContAct.startActivity(viewContAct.suite);
            viewContAct.finish();
        }
        catch (ClassNotFoundException  e)
        {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
        }
        return null;
    }
}