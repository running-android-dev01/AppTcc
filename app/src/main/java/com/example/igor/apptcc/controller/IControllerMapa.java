package com.example.igor.apptcc.controller;


import com.example.igor.apptcc.model.Estabelecimento;

public interface IControllerMapa {
    void atualizarMapa(Estabelecimento estabelecimento);
    void excluirMapa(String id);
}
