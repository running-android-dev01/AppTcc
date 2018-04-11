package com.example.igormoraes.appbar.produto;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.igormoraes.appbar.R;
import com.example.igormoraes.appbar.model.Produto;

import java.util.List;

public class AdapterProduto  extends RecyclerView.Adapter<ViewHolderProduto> {
    private List<Produto> mProduto;
    private final Context context;


    public AdapterProduto(Context context){
        this.context = context;
    }

    public void atualizarLista(List<Produto> produtos){
        mProduto = produtos;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderProduto onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderProduto(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_info_estabelecimento_produto, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderProduto holder, int position) {
        final Produto produto = mProduto.get(position);
        holder.txtNomeProduto.setText(produto.nome);
        holder.txtPrecoProduto.setText(String.format("R$ %.2f", produto.preco));

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, InfoProdutoActivity.class);
            i.putExtra(InfoProdutoActivity.PARAM_ID, produto.id);
            i.putExtra(InfoProdutoActivity.PARAM_ID_ESTABELECIMENTO, produto.id_estabelecimento);

            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return mProduto != null ? mProduto.size() : 0;
    }
}

