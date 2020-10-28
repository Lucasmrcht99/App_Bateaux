package com.example.app_bateaux;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class ContainerAdapter extends BaseAdapter {

    public Context mContext;
    public List<Containers> mListC;
    public LayoutInflater mInflater;

    @Override
    public int getCount() {
        return mListC.size();
    }

    @Override
    public Object getItem(int position) {
        return mListC.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public ContainerAdapter(Context context, List<Containers> aListC) {
        mContext = context;
        mListC = aListC;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ConstraintLayout layoutItem;
        //(1) : Réutilisation des layouts


        if (convertView == null) {
            layoutItem = (ConstraintLayout) mInflater.inflate(R.layout.listviewlayout, parent, false);
        } else {
            layoutItem = (ConstraintLayout) convertView;
        }

        //(2) : Récupération des TextView de notre layout
        TextView Id = (TextView)layoutItem.findViewById(R.id.Id);
        TextView Coord = (TextView)layoutItem.findViewById(R.id.Coord);
        TextView Date = (TextView)layoutItem.findViewById(R.id.Date);
        TextView Poids = (TextView)layoutItem.findViewById(R.id.Poids);

        //(3) : Renseignement des valeurs
        Id.setText(mListC.get(position).Id);
        Coord.setText(mListC.get(position).Coord);
        Date.setText(mListC.get(position).Date);
        Poids.setText(mListC.get(position).Poids);

        //On retourne l'item créé.



        return layoutItem;
    }
}
