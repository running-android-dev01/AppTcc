package com.example.igor.apptcc.produto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.igor.apptcc.R;
import com.example.igor.apptcc.model.ProdutoAvaliacao;
import com.example.igor.apptcc.utils.DateUtils;

import java.util.List;

/**
 * Created by igormoraes on 17/03/18.
 */

class AdapterProdutoAvaliacao  extends RecyclerView.Adapter<ViewHolderProdutoAvaliacao> {
    private List<ProdutoAvaliacao> mProdutoAvaliacao;
    private final Context context;


    public AdapterProdutoAvaliacao(Context context){
        this.context = context;
    }

    public void atualizarLista(List<ProdutoAvaliacao> produtoAvaliacaos){
        mProdutoAvaliacao = produtoAvaliacaos;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderProdutoAvaliacao onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderProdutoAvaliacao(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_info_produto_avaliacao, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderProdutoAvaliacao holder, int position) {
        final ProdutoAvaliacao produtoAvaliacao = mProdutoAvaliacao.get(position);


        holder.txtNomeProdutoAvaliacao.setText(produtoAvaliacao.nome);
        holder.txtDataProdutoAvaliacao.setText(DateUtils.ConvertToString(produtoAvaliacao.data, context));
        holder.txtNotaProdutoAvaliacao.setText(String.format("%d", produtoAvaliacao.avaliacao));
        holder.txtDescricaoProdutoAvaliacao.setText(produtoAvaliacao.descricao);
    }

    @Override
    public int getItemCount() {
        return mProdutoAvaliacao != null ? mProdutoAvaliacao.size() : 0;
    }
}
