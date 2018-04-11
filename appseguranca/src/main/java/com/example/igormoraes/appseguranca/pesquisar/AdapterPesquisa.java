package com.example.igormoraes.appseguranca.pesquisar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.igormoraes.appseguranca.R;
import com.example.igormoraes.appseguranca.model.PesquisaModel;
import com.example.igormoraes.appseguranca.utils.AndroidUtils;

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
