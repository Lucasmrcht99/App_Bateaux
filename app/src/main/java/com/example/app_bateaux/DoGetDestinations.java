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

    private ViewContainersActivity viewContAct;
    private BoatActivity boatAct;
    private UnloadContainersActivity unloadContAct;

    public DoGetDestinations(ViewContainersActivity context)
    {
        viewContAct = context;
        boatAct = null;
        unloadContAct = null;
    }

    public DoGetDestinations(BoatActivity context)
    {
        viewContAct = null;
        boatAct = context;
        unloadContAct = null;
    }

    public DoGetDestinations(UnloadContainersActivity context)
    {
        viewContAct = null;
        boatAct = null;
        unloadContAct = context;
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
            if(viewContAct == null && unloadContAct == null)
            {
                AfficheToast.Affiche( "Connexion au serveur perdue", boatAct);
                boatAct.suite = new Intent(boatAct, LoginActivity.class);
                boatAct.startActivity(boatAct.suite);
                boatAct.finish();
            }
            else if(boatAct == null && unloadContAct == null)
            {
                AfficheToast.Affiche( "Connexion au serveur perdue", viewContAct);
                viewContAct.suite = new Intent(viewContAct, LoginActivity.class);
                viewContAct.startActivity(viewContAct.suite);
                viewContAct.finish();
            }
            else if(boatAct == null && viewContAct == null)
            {
                AfficheToast.Affiche( "Connexion au serveur perdue", unloadContAct);
                unloadContAct.suite = new Intent(unloadContAct, LoginActivity.class);
                unloadContAct.startActivity(unloadContAct.suite);
                unloadContAct.finish();
            }
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
                    if(viewContAct == null && unloadContAct == null)
                    {
                        boatAct.addItemsOnSpinner(rep.getChargeUtile());
                    }
                    else if(boatAct == null && unloadContAct == null)
                    {
                        viewContAct.addItemsOnSpinner(rep.getChargeUtile());
                    }
                    else if(boatAct == null && viewContAct == null)
                    {
                        unloadContAct.addItemsOnSpinner(rep.getChargeUtile());
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            if(viewContAct == null && unloadContAct == null)
            {
                AfficheToast.Affiche( "Connexion au serveur perdue", boatAct);
                boatAct.suite = new Intent(boatAct, LoginActivity.class);
                boatAct.startActivity(boatAct.suite);
                boatAct.finish();
            }
            else if(boatAct == null && unloadContAct == null)
            {
                AfficheToast.Affiche( "Connexion au serveur perdue", viewContAct);
                viewContAct.suite = new Intent(viewContAct, LoginActivity.class);
                viewContAct.startActivity(viewContAct.suite);
                viewContAct.finish();
            }
            else if(boatAct == null && viewContAct == null)
            {
                AfficheToast.Affiche( "Connexion au serveur perdue", unloadContAct);
                unloadContAct.suite = new Intent(unloadContAct, LoginActivity.class);
                unloadContAct.startActivity(unloadContAct.suite);
                unloadContAct.finish();
            }
        }
        catch (ClassNotFoundException  e)
        {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
        }
        return null;
    }
}
