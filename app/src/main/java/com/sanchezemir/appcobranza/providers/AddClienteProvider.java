package com.sanchezemir.appcobranza.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sanchezemir.appcobranza.models.clientes.Cliente;

public class AddClienteProvider {

    CollectionReference mCollection;

    public AddClienteProvider(){

        mCollection = FirebaseFirestore.getInstance().collection("AgregarCli");

    }

    public Task<Void> save(Cliente cliente){

        return null;

    }
}
