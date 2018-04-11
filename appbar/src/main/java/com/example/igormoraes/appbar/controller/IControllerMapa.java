package com.example.igormoraes.appbar.controller;


import com.example.igormoraes.appbar.model.Estabelecimento;

public interface IControllerMapa {
    void atualizarMapa(Estabelecimento estabelecimento);
    void excluirMapa(String id);
}
