package com.example.jorge.reproductoraudio;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class Principal extends Activity {
    private Adaptador ad;
    private Cursor canciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);

        canciones =  getContentResolver().query( android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);

        final ListView lv = (ListView) findViewById(R.id.listView);
        ad = new Adaptador(this, canciones);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
        ad.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Principal.this, Reproducir.class);
                intent.putExtra("posicion",i);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, ServicioAudio.class));
        super.onDestroy();
    }
}
