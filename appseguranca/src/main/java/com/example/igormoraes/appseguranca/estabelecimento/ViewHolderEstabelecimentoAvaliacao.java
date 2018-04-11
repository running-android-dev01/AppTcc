package com.example.igormoraes.appseguranca.estabelecimento;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.igormoraes.appseguranca.R;

/**
 * Created by igormoraes on 17/03/18.
 */

public class ViewHolderEstabelecimentoAvaliacao extends RecyclerView.ViewHolder {

    public TextView txtNomeEstabelecimentoAvaliacao;
    public TextView txtDataEstabelecimentoAvaliacao;
    public TextView txtNotaEstabelecimentoAvaliacao;
    public TextView txtDescricaoEstabelecimentoAvaliacao;

    public ViewHolderEstabelecimentoAvaliacao(View itemView) {
        super(itemView);

        txtNomeEstabelecimentoAvaliacao =  itemView.findViewById(R.id.txtNomeEstabelecimentoAvaliacao);
        txtDataEstabelecimentoAvaliacao =  itemView.findViewById(R.id.txtDataEstabelecimentoAvaliacao);
        txtNotaEstabelecimentoAvaliacao =  itemView.findViewById(R.id.txtNotaEstabelecimentoAvaliacao);
        txtDescricaoEstabelecimentoAvaliacao =  itemView.findViewById(R.id.txtDescricaoEstabelecimentoAvaliacao);
    }
}
