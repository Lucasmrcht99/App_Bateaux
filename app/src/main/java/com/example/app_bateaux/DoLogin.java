package com.example.app_bateaux;

import android.content.Intent;
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
        String ipAddress = "192.168.0.27";
        int port = 50006;
        if(Connect == false)
        {
            try
            {
                cliSock = new Socket(ipAddress, port);
                Connect = true;
            }
            catch (UnknownHostException e)
            {
                logAct.AfficheToast("Erreur ! Host non trouvé [" + e + "]");
            }
            catch (IOException e)
            {
                System.out.println("Erreur de connexion " + ipAddress + " " + String.valueOf(port));
                logAct.AfficheToast("Erreur de connexion " + ipAddress + " " + String.valueOf(port));
                Connect = false;
                return null;
            }
        }
        if(cliSock.isClosed())
        {
            logAct.AfficheToast("Erreur de connexion");
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
            logAct.AfficheToast("Connexion au serveur perdue\nPossiblement plus de connexions disponible");
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
                logAct.suite = new Intent(logAct, MenuActivity.class);
                logAct.suite.putExtra("User", rep.getChargeUtile());
                SocketHandler.setSock(cliSock);
                logAct.startActivity(logAct.suite);
            }
            else
            {
                logAct.AfficheToast("Erreur de connexion");
                RequeteIOBREP req2 = new RequeteIOBREP(RequeteIOBREP.CLOSE, "");
                oos = new ObjectOutputStream(cliSock.getOutputStream());
                oos.writeObject(req2);
                oos.flush();
                Connect = false;
            }
        }
        catch (ClassNotFoundException  e)
        {
            logAct.AfficheToast("Erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e)
        {
            logAct.AfficheToast("Connexion au serveur perdue");
            Connect = false;
        }
        return null;
    }
}
