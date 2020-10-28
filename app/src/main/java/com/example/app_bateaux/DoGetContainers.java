package com.example.app_bateaux;

import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import req_rep_IOBREP.ReponseIOBREP;
import req_rep_IOBREP.RequeteIOBREP;

public class DoGetContainers extends AsyncTask<Void, Void, Void> {

    private String destination;
    private String mode;


    @Override protected void onPreExecute() { super.onPreExecute(); }

    @Override protected void onProgressUpdate(Void... arg1)
    {
        super.onProgressUpdate();
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        return null ;
    }
}
