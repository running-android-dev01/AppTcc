package com.example.igor.apptcc.avaliacaoEstabelecimento;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.igor.apptcc.R;
import com.example.igor.apptcc.modelDb.AvaliacaoEstabelecimento;

import java.util.List;

public class AvaliacacaoEstabelecimentoAdapter extends RecyclerView.Adapter{
    private List<AvaliacaoEstabelecimento> lAvaliacaoEstabelecimento;
    private Context context;

    public AvaliacacaoEstabelecimentoAdapter(Context context, List<AvaliacaoEstabelecimento> lAvaliacaoEstabelecimento) {
        this.lAvaliacaoEstabelecimento = lAvaliacaoEstabelecimento;
        this.context = context;
    }

    public class AvaliacacaoEstabelecimentoViewHolder extends RecyclerView.ViewHolder {

        final TextView txtAvaliacaoNome;
        final TextView txtAvaliacaoNota;
        final TextView txtAvaliacaoData;
        final TextView txtAvaliacaoAvaliacao;

        public AvaliacacaoEstabelecimentoViewHolder(View view) {
            super(view);
            txtAvaliacaoNome = view.findViewById(R.id.txtAvaliacaoNome);
            txtAvaliacaoNota = view.findViewById(R.id.txtAvaliacaoNota);
            txtAvaliacaoData = view.findViewById(R.id.txtAvaliacaoData);
            txtAvaliacaoAvaliacao = view.findViewById(R.id.txtAvaliacaoAvaliacao);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_estabelecimento_avaliacao, parent, false);

        AvaliacacaoEstabelecimentoViewHolder holder = new AvaliacacaoEstabelecimentoViewHolder(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        AvaliacacaoEstabelecimentoViewHolder holder = (AvaliacacaoEstabelecimentoViewHolder) viewHolder;

        AvaliacaoEstabelecimento avaliacaoEstabelecimento  = lAvaliacaoEstabelecimento.get(position) ;

        holder.txtAvaliacaoNome.setText(avaliacaoEstabelecimento.nome);
        holder.txtAvaliacaoNota.setText(Integer.toString(avaliacaoEstabelecimento.nota));
        holder.txtAvaliacaoData.setText(avaliacaoEstabelecimento.data);
        holder.txtAvaliacaoAvaliacao.setText(avaliacaoEstabelecimento.avaliacao);
    }

    @Override
    public int getItemCount() {
        return lAvaliacaoEstabelecimento.size();
    }

}
