package com.example.app_bateaux;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class DoAddSociete extends AsyncTask<Void, Void, Integer> {

    private AddSocieteActivity addSocAct;
    private String Societe;
    private String Nom;
    private String Mail;
    private String Tel;
    private String Adresse;

    public DoAddSociete(AddSocieteActivity context, String soc, String nom, String mail, String tel, String adr)
    {
        addSocAct = context;
        Societe = soc;
        Nom = nom;
        Mail = mail;
        Tel = tel;
        Adresse = adr;
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
        RequeteIOBREP req = new RequeteIOBREP(RequeteIOBREP.ADD_SOC, Societe + "&" + Nom + "&" + Mail + "&" + Tel + "&" + Adresse);
        Socket cliSock = SocketHandler.getSock();
        try {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            oos.flush();
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", addSocAct);
            addSocAct.suite = new Intent(addSocAct, LoginActivity.class);
            addSocAct.startActivity(addSocAct.suite);
            addSocAct.finish();
        }
        // Lecture de la réponse
        ReponseIOBREP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseIOBREP)ois.readObject();
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
            AfficheToast.Affiche( "Connexion au serveur perdue", addSocAct);
            addSocAct.suite = new Intent(addSocAct, LoginActivity.class);
            addSocAct.startActivity(addSocAct.suite);
            addSocAct.finish();
        }
        catch (ClassNotFoundException  e)
        {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
        }

        return rep.getCode();
    }
}
