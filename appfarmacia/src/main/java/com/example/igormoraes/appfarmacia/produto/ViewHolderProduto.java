package com.example.igormoraes.appfarmacia.produto;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.igormoraes.appfarmacia.R;

public class ViewHolderProduto extends RecyclerView.ViewHolder {

    public TextView txtNomeProduto;
    public TextView txtPrecoProduto;

    public ViewHolderProduto(View itemView) {
        super(itemView);
        txtNomeProduto =  itemView.findViewById(R.id.txtNomeProduto);
        txtPrecoProduto = itemView.findViewById(R.id.txtPrecoProduto);
    }
}
