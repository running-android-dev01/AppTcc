package com.example.igormoraes.appfarmacia.pesquisar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.igormoraes.appfarmacia.R;
import com.example.igormoraes.appfarmacia.estabelecimento.InfoEstabelecimentoActivity;
import com.example.igormoraes.appfarmacia.model.PesquisaModel;
import com.example.igormoraes.appfarmacia.utils.AndroidUtils;

import java.util.List;

class AdapterPesquisa  extends RecyclerView.Adapter<ViewHolderPesquisa> {
    private List<PesquisaModel> mPesquisaModel;
    private Context context;


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

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, InfoEstabelecimentoActivity.class);
            i.putExtra(InfoEstabelecimentoActivity.PARAM_ID, pesquisaModel.estabelecimento_key);

            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return mPesquisaModel != null ? mPesquisaModel.size() : 0;
    }
}
