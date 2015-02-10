package com.example.jorge.reproductoraudio;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class Reproducir extends Activity {

    private Cursor cancion;
    private int posicion;
    private final int GRABAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_reproducir);
        cancion =  getContentResolver().query( android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        posicion = getIntent().getExtras().getInt("posicion");
        cancion.moveToPosition(posicion);
        reproducir();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.accion_grabar) {
            Intent intentStop = new Intent(this, ServicioAudio.class);
            intentStop.setAction(ServicioAudio.STOP);
            intentStop.putExtra("cancion", cancion.getString(1));
            startService(intentStop);
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            startActivityForResult(intent, GRABAR);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void play(View v){
        Intent intent = new Intent(this, ServicioAudio.class);
        intent.setAction(ServicioAudio.PLAY);
        startService(intent);
    }

    public void stop(View v){
        Intent intent = new Intent(this, ServicioAudio.class);
        intent.setAction(ServicioAudio.STOP);
        startService(intent);
    }

    public void pause(View v){
        Intent intent = new Intent(this, ServicioAudio.class);
        intent.setAction(ServicioAudio.PAUSE);
        startService(intent);
    }

    public void reproducir(){
        Intent intentStop = new Intent(this, ServicioAudio.class);
        intentStop.setAction(ServicioAudio.STOP);
        intentStop.putExtra("cancion", cancion.getString(1));
        startService(intentStop);

        Intent intent = new Intent(this, ServicioAudio.class);
        intent.putExtra("cancion",cancion.getString(1));
        intent.setAction(ServicioAudio.ADD);
        startService(intent);

        Intent intentPlay = new Intent(this, ServicioAudio.class);
        intentPlay.putExtra("cancion",cancion.getString(1));
        intentPlay.setAction(ServicioAudio.PLAY);
        startService(intentPlay);

        String ruta = cancion.getString(cancion.getColumnIndex("_display_name"));
        TextView tv = (TextView) findViewById(R.id.tvArchivo);
        tv.setText(ruta);
    }

    public void cancionSiguiente(View v){
        Intent intentStop = new Intent(this, ServicioAudio.class);
        intentStop.setAction(ServicioAudio.STOP);
        intentStop.putExtra("cancion", cancion.getString(1));
        startService(intentStop);

        cancion.moveToNext();

        Intent intent = new Intent(this, ServicioAudio.class);
        intent.putExtra("cancion",cancion.getString(1));
        intent.setAction(ServicioAudio.ADD);
        startService(intent);

        Intent intentPlay = new Intent(this, ServicioAudio.class);
        intentPlay.putExtra("cancion",cancion.getString(1));
        intentPlay.setAction(ServicioAudio.PLAY);
        startService(intentPlay);

        Log.v("ruta",cancion.getString(1));
        String ruta = cancion.getString(cancion.getColumnIndex("_display_name"));
        TextView tv = (TextView) findViewById(R.id.tvArchivo);
        tv.setText(ruta);
    }

    public void cancionAnterior(View v){
        Intent intentStop = new Intent(this, ServicioAudio.class);
        intentStop.setAction(ServicioAudio.STOP);
        intentStop.putExtra("cancion",cancion.getString(1));
        startService(intentStop);

        cancion.moveToPrevious();

        Intent intent = new Intent(this, ServicioAudio.class);
        intent.putExtra("cancion",cancion.getString(1));
        intent.setAction(ServicioAudio.ADD);
        startService(intent);

        Intent intentPlay = new Intent(this, ServicioAudio.class);
        intentPlay.putExtra("cancion",cancion.getString(1));
        intentPlay.setAction(ServicioAudio.PLAY);
        startService(intentPlay);

        Log.v("ruta",cancion.getString(1));
        String ruta = cancion.getString(cancion.getColumnIndex("_display_name"));
        TextView tv = (TextView) findViewById(R.id.tvArchivo);
        tv.setText(ruta);
    }

    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GRABAR) {
                Uri uri = data.getData();
            }
        }
    }
}
