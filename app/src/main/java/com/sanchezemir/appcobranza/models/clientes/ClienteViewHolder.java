package com.sanchezemir.appcobranza.models.clientes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchezemir.appcobranza.R;

import org.jetbrains.annotations.NotNull;

public class ClienteViewHolder extends RecyclerView.ViewHolder {

    TextView txtNombreCliente;


    public ClienteViewHolder(@NonNull @NotNull View v) {
        super(v);

        txtNombreCliente = v.findViewById(R.id.txtNombreCliente);

    }
}
