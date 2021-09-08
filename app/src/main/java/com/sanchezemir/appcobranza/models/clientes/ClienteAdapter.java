package com.sanchezemir.appcobranza.models.clientes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchezemir.appcobranza.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteViewHolder> {

    Context context;
    List<Cliente> clientes;

    public ClienteAdapter(Context context, List<Cliente> clientes) {
        this.context = context;
        this.clientes = clientes;
    }

    @NonNull
    @NotNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_clientes, parent, false);
        return new ClienteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ClienteViewHolder holder, int position) {
        //Cliente cliente = clientes.get(position);

        //String sName = context.getString(R.string.nombre_cliente, cliente.getName());
        holder.txtNombreCliente.setText(clientes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }
}
