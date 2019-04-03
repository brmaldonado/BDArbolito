//Se ha creado una app que nos permite crear y guardar registros de arboles: Altura, fecha de siembra, ultima fecha de revision,
//nombre del encargado. Así tambien en la pantalla principal muestra la lista de los árboles que hemos creado
//Permite manipular los datos guardados y borrar algunos registros
package net.unadeca.testbasedatos.activities;
//importar librerías y clases

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.unadeca.testbasedatos.R;
import net.unadeca.testbasedatos.database.models.Arbolito;
import net.unadeca.testbasedatos.database.models.Arbolito_Table;

import java.util.List;

//constructor de la clase, extiende de la clase AppCompactActivity
//Esta clase nos permitirá implementar las funciones de barra de accion de la biblioteca de soporte
public class MainActivity extends AppCompatActivity {

    //declaracion de variable que conectará con el diseño con la vista
    private CoordinatorLayout view;
    //Declaracion de la variable que mostrará la lista de arboles guardados
    private ListView lista;

    @Override
    //metodo OnCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //conexion
        lista = findViewById(R.id.lista);
        view = findViewById(R.id.content);
        setAdapter();


        //Creando objetos de arbolito
        //ejemplo 1 y 2
        //implementacion de objetos
        /** Arbolito pino = new Arbolito();
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

         **/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //implementacion del dialog
                mostrarDialog();
            }
        });


        //Se ha llamado el método borrarArbolito y se ha configurado para borrar todos
        //los arbolitos que su altura se encuentre entre 1 y 10
       /** borrarArbolito();
        //Muestra el mensaje que confirma que la accion se ha realizado
        Snackbar.make(view, "Hemos borrado el listado de arbolitos", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    **/
        }

    @Override
    //Método onCreateOptionsMenu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    //Metodo OnOptionsSelected
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //metodo para traer y guardar la informacion en nuestra lista
    private String[] getArbolitos() {
        //crea la lista
        List<Arbolito> listado = SQLite.select().from(Arbolito.class).queryList();
        //Crea un array para gardar la lista
        String[] array = new String[listado.size()];
        //Guarda la informacion en el array
        for (int c = 0; c < listado.size(); c++) {
            array[c] = listado.get(c).toString();
        }
        return array;
    }

    //Establecer adaptador
    private void setAdapter() {
        lista.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getArbolitos()));
    }

    //Método que nos permite interactividad con el usuario
    //Le permite al usuario poder agregar registros nuevos de arbolitos
    //Muestra cudros de dialogocon informacion


    //Método para eliminar los registros creados
    //Se ha configurado para borrar lis arbolitos con altura de 1 a 10 
    private void borrarArbolito() {
        SQLite.delete().from(Arbolito.class).where(Arbolito_Table.altura.between(1).and(10)).execute();
        setAdapter();
        Snackbar.make(view, "Hemos borrado el listado de arbolitos", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
}
