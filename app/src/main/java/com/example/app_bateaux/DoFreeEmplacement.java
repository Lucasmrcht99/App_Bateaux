package com.example.app_bateaux;

import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class DoFreeEmplacement extends AsyncTask<Void, Void, Void> {

    private UnloadContainersActivity unloadAct;
    private ArrayList<Containers> listConainters;

    public DoFreeEmplacement(UnloadContainersActivity context, ArrayList<Containers> list)
    {
        unloadAct = context;
        listConainters = list;
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
        RequeteIOBREP req = new RequeteIOBREP(RequeteIOBREP.FREE_EMPLACEMENT, "");
        Socket cliSock = SocketHandler.getSock();
        try
        {
            String coord = "";
            int taille = listConainters.size();
            for(int i=0; i<taille; i++)
            {
                if(coord.equals(""))
                {
                    coord = listConainters.get(i).getCoord();
                }
                else
                {
                    coord = coord + "&" + listConainters.get(i).getCoord();
                }
            }
            req.setChargeUtile(coord);
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            oos.flush();
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", unloadAct);
            unloadAct.suite = new Intent(unloadAct, LoginActivity.class);
            unloadAct.startActivity(unloadAct.suite);
            unloadAct.finish();
            return null;
        }
        // Lecture de la réponse
        ReponseIOBREP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseIOBREP)ois.readObject();
            if(rep.getCode()==ReponseIOBREP.OK)
            {
                System.out.println("OK");
            }
        }
        catch (ClassNotFoundException  e)
        {
            System.out.println("Erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", unloadAct);
            unloadAct.suite = new Intent(unloadAct, LoginActivity.class);
            unloadAct.startActivity(unloadAct.suite);
            unloadAct.finish();
        }
        return null;
    }
}
