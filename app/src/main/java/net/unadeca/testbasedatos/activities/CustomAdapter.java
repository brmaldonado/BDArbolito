package net.unadeca.testbasedatos.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.unadeca.testbasedatos.R;
import net.unadeca.testbasedatos.database.models.Arbolito;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Arbolito> implements View.OnClickListener {

    private List<Arbolito> dataSet;
    Context mContext;
    CoordinatorLayout view;

    // View lookup cache
    private static class ViewHolder {
        TextView txtArbolito;
        ImageButton delete;
        ImageView update;
    }

    public CustomAdapter(List<Arbolito> data, Context context) {
        super(context, R.layout.item, data);
        this.dataSet = data;
        this.mContext = context;
        this.view = 1;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Arbolito dataModel = (Arbolito) object;

        Toast.makeText(getContext(), "text", Toast.LENGTH_LONG).show();

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Arbolito dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item, parent, false);
            viewHolder.txtArbolito = convertView.findViewById(R.id.text);
            viewHolder.delete = convertView.findViewById(R.id.delete);
            viewHolder.update = convertView.findViewById(R.id.imagen);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtArbolito.setText(dataModel.toString());
        viewHolder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialog(dataModel);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

    public void mostrarDialog(Arbolito arbol) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        //Se crea la variable local
        View formulario = layoutInflater.inflate(R.layout.formulario, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //Muestra el formulario para agregar un nuevo registro de arbolitos
        builder.setView(formulario);
        //Declaracion y conexion de variables
        final TextInputLayout altura = formulario.findViewById(R.id.txtAltura);
        altura.getEditText().setText(arbol.altura);

        final TextInputLayout siembra = formulario.findViewById(R.id.txtFechaSiembra);
        altura.getEditText().setText(arbol.fecha_plantado);

        final TextInputLayout revision = formulario.findViewById(R.id.txtCheck);
        altura.getEditText().setText(arbol.fecha_ultima_revision);

        final TextInputLayout encargado = formulario.findViewById(R.id.txtEncargado);
        altura.getEditText().setText(arbol.plantado_por);

        //Instrucciones para el usuario
        builder.setMessage("Rellena los campos requeridos")
                //Titulo del cuadro de dialogo
                .setTitle("Agregar nuevo arbolito!!!")
                .setCancelable(false)
                //Al hacer clic en el boton aceptar el registro es guardado
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Try catch nos permite capturar y manipular los errores que se puedan dar
                        try {
                            //llama el metodo validate
                            validate(altura, siembra, revision, encargado);
                            //llama el metodo guardarBD
                            guardarBD(altura, siembra, revision, encargado, arbol);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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

    viewHolder.delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dataModel.delete();
            dataSet.remove(dataModel);
            notifyDataSetChanged();
            Toast.makeText(getContext(), "Se eliminó el arbolito", Toast.LENGTH_LONG).show();
        }
    });
    // Return the completed view to render on screen
        return convertView;
    //Método que nos permite validad la informacion ingresada por el usuario
    //valida que los campos no esten vacíos
    //Recibe como parametros los campos altura, fecha de siembra, revision y encargado
    //Captura el error de un campo vacío y muestra el mensaje al usuario para indicarle
    //que informacion hace falta ingresar
    private void validate(TextInputLayout a, TextInputLayout s, TextInputLayout c, TextInputLayout e) throws Exception {
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
    //Método que permite guardar la informacion y crear el registro de un nuevo arbolito
    //Hace la conexion con la base de datos para poder almacenarlos alli
    private void guardarBD(TextInputLayout a, TextInputLayout s, TextInputLayout c, TextInputLayout e, Arbolito arbolito) {
        arbolito.altura = Integer.parseInt(a.getEditText().getText().toString());
        arbolito.fecha_plantado = s.getEditText().getText().toString();
        arbolito.fecha_ultima_revision = c.getEditText().getText().toString();
        arbolito.plantado_por = e.getEditText().getText().toString();
        arbolito.save();

        //Muestra el mensaje que confirma que los datos se han guardado
        Snackbar.make(view, "Se ha guardado el arbolito", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();


        notifyDataSetChanged();


    }
}
