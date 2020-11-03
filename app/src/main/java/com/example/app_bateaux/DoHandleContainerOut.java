package com.example.app_bateaux;

import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class DoHandleContainerOut  extends AsyncTask<Void, Void, Integer> {
    private LoadContainersActivity loadAct;
    private String Id;
    private String Mode;
    private String Dest;
    private String Nb;

    public DoHandleContainerOut(LoadContainersActivity context, String id, String mode, String dest, int nb)
    {
        loadAct = context;
        Id = id;
        Mode = mode;
        Dest = dest;
        Nb = String.valueOf(nb);
    }

    @Override protected void onPreExecute() { super.onPreExecute(); }

    @Override protected void onProgressUpdate(Void... arg1)
    {
        super.onProgressUpdate();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        ObjectInputStream ois=null;
        ObjectOutputStream oos=null;
        RequeteIOBREP req = new RequeteIOBREP(RequeteIOBREP.HANDLE_CONTAINER_OUT, "");
        Socket cliSock = SocketHandler.getSock();
        try
        {
            req.setChargeUtile(Dest + "&" + Mode + "&" + Id + "&" + Nb);
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
        return rep.getCode();
    }
}
