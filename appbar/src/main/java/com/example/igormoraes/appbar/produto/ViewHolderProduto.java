package com.example.igormoraes.appbar.produto;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.igormoraes.appbar.R;

public class ViewHolderProduto extends RecyclerView.ViewHolder {

    public TextView txtNomeProduto;
    public TextView txtPrecoProduto;

    public ViewHolderProduto(View itemView) {
        super(itemView);
        txtNomeProduto =  itemView.findViewById(R.id.txtNomeProduto);
        txtPrecoProduto = itemView.findViewById(R.id.txtPrecoProduto);
    }
}
