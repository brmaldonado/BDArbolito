package net.unadeca.testbasedatos.activities;
//importar librerías y clases
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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

public class MainActivity extends AppCompatActivity {

    //declaracion de variable que conectará con el diseño con la vista
    private CoordinatorLayout view;
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
        view = findViewById(R.id.content);
        setAdapter();

        /**implementacion de objetos
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
         **/

        borrarArbolito();
        Snackbar.make(view, "Hemos borrado el listado de arbolitos", Snackbar.LENGTH_LONG)
                .setAction("Action",null).show();
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
       LayoutInflater layoutInflater = LayoutInflater.from(this);
       View formulario = layoutInflater.inflate(R.layout.formulario, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(formulario);
        final TextInputLayout altura = formulario.findViewById(R.id.txtAltura);
        final TextInputLayout siembra = formulario.findViewById(R.id.txtFechaSiembra);
        final TextInputLayout revision = formulario.findViewById(R.id.txtCheck);
        final TextInputLayout encargado = formulario.findViewById(R.id.txtEncargado);

        builder.setMessage("Rellena los campos requeridos")
                .setTitle("Agregar nuevo arbolito!!!")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{

                            validate(altura, siembra,revision,encargado);
                            guardarBD(altura, siembra, revision, encargado);
                        }catch (Exception e){
                            Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Action",null).show();
                        }
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


    private void validate(TextInputLayout a, TextInputLayout s, TextInputLayout c, TextInputLayout e ) throws Exception {
        if (a.getEditText().getText().toString().isEmpty()) {
            throw new Exception("Necesita escribir la altura del arbolito");
        }
        if (s.getEditText().getText().toString().isEmpty()) {
            throw new Exception("Necesita escribir la fecha de siembra del arbolito");
        }
        if (c.getEditText().getText().toString().isEmpty()) {
            throw new Exception("Necesita escribir la fecha de revisión del arbolito");
        }
        if (e.getEditText().getText().toString().isEmpty()) {
            throw new Exception("Necesita escribir el nombre del encargado");
        }
    }
        private void guardarBD(TextInputLayout a, TextInputLayout s, TextInputLayout c, TextInputLayout e){
        Arbolito arbolito=new Arbolito();
        arbolito.altura = Integer.parseInt(a.getEditText().getText().toString());
        arbolito.fecha_plantado=s.getEditText().getText().toString();
        arbolito.fecha_ultima_revision=c.getEditText().getText().toString();
        arbolito.plantado_por=e.getEditText().getText().toString();
        arbolito.save();

        setAdapter();

            Snackbar.make(view, "Se ha guardado el arbolito", Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();


    }

    private void borrarArbolito(){
        Delete.table(Arbolito.class);
        SQLite.delete().from(Arbolito.class).where(Arbolito_Table.altura.between(1).and(10)).execute();
        setAdapter();
    }
}
