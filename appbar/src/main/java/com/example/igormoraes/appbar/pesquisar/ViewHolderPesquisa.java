package com.example.igormoraes.appbar.pesquisar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.igormoraes.appbar.R;

public class ViewHolderPesquisa extends RecyclerView.ViewHolder {
    public TextView txtNome;
    public TextView txtProdutoEstabelecimento;
    public TextView txtEndereco;
    public TextView txtDistancia;


    public ViewHolderPesquisa(View itemView) {
        super(itemView);

        txtNome =  itemView.findViewById(R.id.txtNome);
        txtProdutoEstabelecimento =  itemView.findViewById(R.id.txtProdutoEstabelecimento);
        txtEndereco =  itemView.findViewById(R.id.txtEndereco);
        txtDistancia =  itemView.findViewById(R.id.txtDistancia);
    }
}
