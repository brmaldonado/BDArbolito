package net.unadeca.testbasedatos.activities;
//importar librerías y clases
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.unadeca.testbasedatos.R;
import net.unadeca.testbasedatos.database.models.Arbolito;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //declaracion de variable que conectará con el diseño con la vista
    private  ListView lista;
    @Override
    //metodo OnCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //conexion
        lista = findViewById(R.id.lista);
        setAdapter();

        //implementacion de objetos
        Arbolito pino = new Arbolito();
        //datos del objeto creado
        pino.altura = 4;
        pino.fecha_plantado = "2019-01-01";
        pino.fecha_ultima_revision = "2019-02-01";
        pino.plantado_por = "Juan Martinez";
        //guardar la informacion
        pino.save();

        //objeto 2
        Arbolito cedro = new Arbolito();
        cedro.altura = 10;
        cedro.fecha_plantado = "2017-01-01";
        cedro.fecha_plantado = "2019-02-01";
        cedro.plantado_por = "Martin Perez";
        cedro.save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //metodo para traer y guardar la informacion en nuestra lista
    private String[] getArbolitos(){
        List<Arbolito> listado = SQLite.select().from(Arbolito.class).queryList();
        String[] array = new String[listado.size()];
        for(int c= 0; c< listado.size(); c++){
            array[c]= listado.get(c).toString();
        }
        return array;
    }

    //Establecer adaptador
    private void setAdapter(){
        lista.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getArbolitos()));
    }

    public void mostrarDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Este es un mensaje de prueba")
                .setTitle("Alerta!!!")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        AlertDialog dialogo = builder.create();
        dialogo.show();
    }


}
