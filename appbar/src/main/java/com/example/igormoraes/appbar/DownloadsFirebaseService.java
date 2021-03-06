package com.example.igormoraes.appbar;

import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.igormoraes.appbar.controller.ControllerEstabelecimento;
import com.example.igormoraes.appbar.controller.ControllerEstabelecimentoAvaliacao;
import com.example.igormoraes.appbar.controller.ControllerProduto;
import com.example.igormoraes.appbar.controller.ControllerProdutoAvaliacao;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DownloadsFirebaseService extends Service {
    private final String TAG =DownloadsFirebaseService.class.getName();
    public DownloadsFirebaseService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        downloadInfo();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(DownloadsFirebaseBreadCast.RESTART);
        sendBroadcast(broadcastIntent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void downloadInfo(){
        String CAMINHO = "data";
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference(CAMINHO);
        scoresRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizar(DownloadsFirebaseService.this, dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizar(DownloadsFirebaseService.this, dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                excluir(DownloadsFirebaseService.this, dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved: The " + dataSnapshot.getKey() + " dinosaur's score is " + dataSnapshot.getValue());
                atualizar(DownloadsFirebaseService.this, dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: The " + databaseError.toString());
            }
        });
    }


    private void atualizar(final Context context, DataSnapshot dataSnapshot){
        String id = dataSnapshot.getKey();
        switch (id) {
            case "estabelecimento":
                for (DataSnapshot getSnapshot : dataSnapshot.getChildren()) {
                    ControllerEstabelecimento.atualizarEstabelecimento(context, getSnapshot);
                }
                ControllerEstabelecimento.setNewEstab(context.getApplicationContext());
                break;
            case "produtoAvaliacoes":
                for (DataSnapshot getSnapshot : dataSnapshot.getChildren()) {
                    String id_estabelecimento = getSnapshot.getKey();
                    for (DataSnapshot getProduto : getSnapshot.getChildren()) {
                        String id_produto = getProduto.getKey();
                        for (DataSnapshot getAvaliacao : getProduto.getChildren()) {
                            ControllerProdutoAvaliacao.atualizarAvaliacao(context, getAvaliacao, id_estabelecimento, id_produto);
                        }
                    }
                }
                break;
            default:
                for (DataSnapshot getSnapshot : dataSnapshot.getChildren()) {
                    String id_estabelecimento = getSnapshot.getKey();
                    for (DataSnapshot getInfo : getSnapshot.getChildren()) {
                        String key = getInfo.getKey();
                        if (key.equals("avaliacoes")) {
                            for (DataSnapshot getAvaliacao : getInfo.getChildren()) {
                                ControllerEstabelecimentoAvaliacao.atualizarAvaliacao(context, getAvaliacao, id_estabelecimento);
                            }
                        } else if (key.equals("produtos")) {
                            for (DataSnapshot getProduto : getInfo.getChildren()) {
                                ControllerProduto.atualizarProduto(context, getProduto, id_estabelecimento);
                            }
                        }
                    }
                }
                break;
        }
    }

    private void excluir(final Context context, DataSnapshot dataSnapshot){
        String id = dataSnapshot.getKey();
        switch (id) {
            case "estabelecimento":
                for (DataSnapshot getSnapshot : dataSnapshot.getChildren()) {
                    ControllerEstabelecimento.excluirEstabelecimento(context, getSnapshot);
                }
                break;
            case "produtoAvaliacoes":
                for (DataSnapshot getSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot getProduto : getSnapshot.getChildren()) {
                        for (DataSnapshot getAvaliacao : getProduto.getChildren()) {
                            ControllerProdutoAvaliacao.excluirAvaliacao(context, getAvaliacao);
                        }
                    }
                }
                break;
            default:
                for (DataSnapshot getSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot getInfo : getSnapshot.getChildren()) {
                        String key = getInfo.getKey();
                        if (key.equals("avaliacoes")) {
                            for (DataSnapshot getAvaliacao : getInfo.getChildren()) {
                                ControllerEstabelecimentoAvaliacao.excluirAvaliacao(context, getAvaliacao);
                            }
                        } else if (key.equals("produtos")) {
                            for (DataSnapshot getProduto : getInfo.getChildren()) {
                                ControllerProduto.excluirProduto(context, getProduto);
                            }
                        }
                    }
                }
                break;
        }

    }
}
