package com.example.igormoraes.appfarmacia.controller;


import com.example.igormoraes.appfarmacia.model.Estabelecimento;

public interface IControllerMapa {
    void atualizarMapa(Estabelecimento estabelecimento);
    void excluirMapa(String id);
}
