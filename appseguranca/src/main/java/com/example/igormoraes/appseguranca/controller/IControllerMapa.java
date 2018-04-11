package com.example.igormoraes.appseguranca.controller;


import com.example.igormoraes.appseguranca.model.Estabelecimento;

public interface IControllerMapa {
    void atualizarMapa(Estabelecimento estabelecimento);
    void excluirMapa(String id);
}
