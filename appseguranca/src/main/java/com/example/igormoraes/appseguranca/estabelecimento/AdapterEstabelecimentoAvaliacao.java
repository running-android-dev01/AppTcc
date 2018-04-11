package com.example.igormoraes.appseguranca.estabelecimento;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.igormoraes.appseguranca.R;
import com.example.igormoraes.appseguranca.model.EstabelecimentoAvaliacao;
import com.example.igormoraes.appseguranca.utils.DateUtils;

import java.util.List;


public class AdapterEstabelecimentoAvaliacao  extends RecyclerView.Adapter<ViewHolderEstabelecimentoAvaliacao> {
    private List<EstabelecimentoAvaliacao> mEstabelecimentoAvaliacao;
    private final Context context;


    public AdapterEstabelecimentoAvaliacao(Context context){
        this.context = context;
    }

    public void atualizarLista(List<EstabelecimentoAvaliacao> estabelecimentoAvaliacaos){
        mEstabelecimentoAvaliacao = estabelecimentoAvaliacaos;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderEstabelecimentoAvaliacao onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderEstabelecimentoAvaliacao(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_info_estabelecimento_avaliacao, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderEstabelecimentoAvaliacao holder, int position) {
        final EstabelecimentoAvaliacao estabelecimentoAvaliacao = mEstabelecimentoAvaliacao.get(position);


        holder.txtNomeEstabelecimentoAvaliacao.setText(estabelecimentoAvaliacao.nome);
        holder.txtDataEstabelecimentoAvaliacao.setText(DateUtils.ConvertToString(estabelecimentoAvaliacao.data));
        holder.txtNotaEstabelecimentoAvaliacao.setText(Long.toString(estabelecimentoAvaliacao.avaliacao));
        holder.txtDescricaoEstabelecimentoAvaliacao.setText(estabelecimentoAvaliacao.descricao);
    }

    @Override
    public int getItemCount() {
        return mEstabelecimentoAvaliacao != null ? mEstabelecimentoAvaliacao.size() : 0;
    }
}
