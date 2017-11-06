package com.example.igor.apptcc.controller;


import com.example.igor.apptcc.modelDb.Estabelecimento;

public interface IEstabelecimentoController {
    void atualizarEstabelecimento(Estabelecimento estabelecimento);
    void excluirEstabelecimento(String id);
}
