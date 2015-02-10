package com.example.jorge.reproductoraudio;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


/**
 * Created by Jorge on 09/02/2015.
 */
public class Adaptador extends CursorAdapter{

    public Adaptador(Context context, Cursor c) {
        super(context, c,true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.lista_detalle, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1;
        tv1 = (TextView)view.findViewById(R.id.tvNombre);
        tv1.setText(cursor.getString(cursor.getColumnIndex("_display_name")));
    }
}
