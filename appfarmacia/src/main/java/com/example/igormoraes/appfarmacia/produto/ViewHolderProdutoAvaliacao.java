package com.example.igormoraes.appfarmacia.produto;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.igormoraes.appfarmacia.R;

public class ViewHolderProdutoAvaliacao extends RecyclerView.ViewHolder {

    public TextView txtNomeProdutoAvaliacao;
    public TextView txtDataProdutoAvaliacao;
    public TextView txtNotaProdutoAvaliacao;
    public TextView txtDescricaoProdutoAvaliacao;

    public ViewHolderProdutoAvaliacao(View itemView) {
        super(itemView);

        txtNomeProdutoAvaliacao =  itemView.findViewById(R.id.txtNomeProdutoAvaliacao);
        txtDataProdutoAvaliacao =  itemView.findViewById(R.id.txtDataProdutoAvaliacao);
        txtNotaProdutoAvaliacao =  itemView.findViewById(R.id.txtNotaProdutoAvaliacao);
        txtDescricaoProdutoAvaliacao =  itemView.findViewById(R.id.txtDescricaoProdutoAvaliacao);
    }
}
