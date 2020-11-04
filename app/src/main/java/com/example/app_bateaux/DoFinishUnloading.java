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

public class DoFinishUnloading extends AsyncTask<Void, Void, Integer> {

    private UnloadContainersActivity unloadAct;
    private ArrayList<Containers> listConainters;
    private String bateau;

    public DoFinishUnloading(UnloadContainersActivity context, ArrayList<Containers> list, String b)
    {
        unloadAct = context;
        listConainters = list;
        bateau = b;
    }

    @Override protected void onPreExecute() { super.onPreExecute(); }

    @Override protected void onProgressUpdate(Void... arg1)
    {
        super.onProgressUpdate();
    }

    @Override
    protected Integer doInBackground(Void... arg0) {
        ObjectInputStream ois=null;
        ObjectOutputStream oos=null;
        RequeteIOBREP req = new RequeteIOBREP(RequeteIOBREP.END_CONTAINER_IN, "");
        Socket cliSock = SocketHandler.getSock();
        try
        {
            String ligne="";
            int taille = listConainters.size();
            for(int i=0; i<taille; i++)
            {
                if(ligne.equals(""))
                {
                    ligne = listConainters.get(i).getId() + "|" + listConainters.get(i).getCoord()+ "|" + listConainters.get(i).getSociete()+ "|" + listConainters.get(i).getPoids()+ "|" + listConainters.get(i).getContenu()+ "|" + listConainters.get(i).getDangers()+ "|" + listConainters.get(i).getDestination() + "|" + bateau;
                }
                else
                {
                    ligne = ligne + "&" + listConainters.get(i).getId() + "|" + listConainters.get(i).getCoord()+ "|" + listConainters.get(i).getSociete()+ "|" + listConainters.get(i).getPoids()+ "|" + listConainters.get(i).getContenu()+ "|" + listConainters.get(i).getDangers()+ "|" + listConainters.get(i).getDestination() + "|" + bateau;
                }
            }
            req.setChargeUtile(ligne);
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
            unloadAct.setReponse(rep.getChargeUtile());
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
        return rep.getCode();
    }
}
