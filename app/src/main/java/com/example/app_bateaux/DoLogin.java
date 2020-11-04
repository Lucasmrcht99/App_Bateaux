package com.example.app_bateaux;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Parcelable;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import req_rep_multithread.*;
import req_rep_IOBREP.*;

public class DoLogin extends AsyncTask<Void, Void, Void> {

    private String login;
    private String pwd;
    private LoginActivity logAct;
    private boolean Connect;
    private Socket cliSock;

    public DoLogin(String log, String pswd, LoginActivity la)
    {
        login = log;
        pwd = pswd;
        logAct = la;
        Connect = false;
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
        RequeteIOBREP req = new RequeteIOBREP(RequeteIOBREP.LOGIN, "");
        String ipAddress = "192.168.0.52";
        int port = 50012;
        if(Connect == false)
        {
            try
            {
                cliSock = new Socket(ipAddress, port);
                Connect = true;
            }
            catch (UnknownHostException e)
            {
                AfficheToast.Affiche("Erreur ! Host non trouvé [" + e + "]", logAct);
            }
            catch (IOException e)
            {
                System.out.println("Erreur de connexion " + ipAddress + " " + String.valueOf(port));
                AfficheToast.Affiche("Erreur de connexion " + ipAddress + " " + String.valueOf(port), logAct);
                Connect = false;
                return null;
            }
        }
        if(cliSock.isClosed())
        {
            AfficheToast.Affiche("Erreur de connexion", logAct);
            Connect = false;
            return null;
        }
        try
        {
            req.setChargeUtile(login + "&" + pwd);
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req);
            oos.flush();
        }
        catch (IOException e)
        {
            AfficheToast.Affiche("Connexion au serveur perdue\nPossiblement plus de connexions disponible", logAct);
            Connect = false;
            return null;
        }
        // Lecture de la réponse
        ReponseIOBREP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseIOBREP)ois.readObject();

            if(rep.getCode() == ReponseIOBREP.OK)
            {
                logAct.suite = new Intent(logAct, ChoixLangue.class);
                logAct.suite.putExtra("User", rep.getChargeUtile());
                SocketHandler.setSock(cliSock);
                logAct.startActivity(logAct.suite);
            }
            else
            {
                AfficheToast.Affiche("Erreur de connexion", logAct);
                RequeteIOBREP req2 = new RequeteIOBREP(RequeteIOBREP.CLOSE, "");
                oos = new ObjectOutputStream(cliSock.getOutputStream());
                oos.writeObject(req2);
                oos.flush();
                Connect = false;
            }
        }
        catch (ClassNotFoundException  e)
        {
            AfficheToast.Affiche("Erreur sur la classe = " + e.getMessage(), logAct);
        }
        catch (IOException e)
        {
            AfficheToast.Affiche("Connexion au serveur perdue", logAct);
            Connect = false;
        }
        return null;
    }
}
