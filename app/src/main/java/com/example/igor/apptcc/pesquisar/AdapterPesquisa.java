package com.example.igor.apptcc.pesquisar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.igor.apptcc.R;
import com.example.igor.apptcc.model.PesquisaModel;
import com.example.igor.apptcc.model.Produto;
import com.example.igor.apptcc.produto.InfoProdutoActivity;
import com.example.igor.apptcc.produto.ViewHolderProduto;
import com.example.igor.apptcc.utils.AndroidUtils;

import java.util.List;

public class AdapterPesquisa  extends RecyclerView.Adapter<ViewHolderPesquisa> {
    private List<PesquisaModel> mPesquisaModel;
    private final Context context;


    public AdapterPesquisa(Context context){
        this.context = context;
    }

    public void atualizarLista(List<PesquisaModel> pesquisaModel){
        mPesquisaModel = pesquisaModel;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderPesquisa onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderPesquisa(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pesquisar, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderPesquisa holder, int position) {
        final PesquisaModel pesquisaModel = mPesquisaModel.get(position);

        holder.txtNome.setText(pesquisaModel.produto_nome);
        holder.txtProdutoEstabelecimento.setText(pesquisaModel.estabelecimento_nome);
        holder.txtEndereco.setText(pesquisaModel.estabelecimento_endereco);
        holder.txtDistancia.setText(AndroidUtils.formatDistanciaFormat(pesquisaModel.distancia));

    }

    @Override
    public int getItemCount() {
        return mPesquisaModel != null ? mPesquisaModel.size() : 0;
    }
}
