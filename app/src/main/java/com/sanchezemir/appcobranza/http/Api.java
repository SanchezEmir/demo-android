package com.sanchezemir.appcobranza.http;

import com.sanchezemir.appcobranza.models.clientes.Cliente;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("clientes")
    Call<List<Cliente>> getClientes();

}
