package com.example.igormoraes.appbar.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.igormoraes.appbar.MainActivity;
import com.example.igormoraes.appbar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class NovoLoginActivity extends AppCompatActivity {
    private static String TAG = NovoLoginActivity.class.getName();
    private FirebaseAuth mAuth;

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtDigitarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtDigitarSenha = findViewById(R.id.edtDigitarSenha);
        Button btnCriarConta = findViewById(R.id.btnCriarConta);
        TextView txtEntrar = findViewById(R.id.txtEntrar);

        btnCriarConta.setOnClickListener(clickCriarConta);
        txtEntrar.setOnClickListener(clickEntrar);
    }

    private View.OnClickListener clickCriarConta = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String nome = edtNome.getText().toString();
            final String email = edtEmail.getText().toString();
            final String senha = edtSenha.getText().toString();
            final String digitarSenha = edtDigitarSenha.getText().toString();

            Boolean erro = false;

            edtNome.setError(null);
            edtEmail.setError(null);
            edtSenha.setError(null);
            edtDigitarSenha.setError(null);

            if (TextUtils.isEmpty(nome)){
                erro = true;
                edtNome.setError("Informe o nome!");
            }
            if (TextUtils.isEmpty(email)){
                erro = true;
                edtEmail.setError("Informe o email!");
            }
            if (TextUtils.isEmpty(senha)){
                erro = true;
                edtSenha.setError("Informe a senha!");
            }
            if (TextUtils.isEmpty(digitarSenha)){
                erro = true;
                edtDigitarSenha.setError("Digitar novamente a senha!");
            }
            if (!senha.equals(digitarSenha)){
                erro = true;
                edtDigitarSenha.setError("Senha diferente da digitada novamente!");
            }

            if (erro){
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(NovoLoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nome)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                Log.d(TAG, "Sucesso");
                                            }
                                        }
                                    });
                        }
                    } else {
                        String erro = UsuarioUtil.getErroFirebaseAuth(NovoLoginActivity.this, ((FirebaseAuthException)task.getException()));
                        Toast.makeText(NovoLoginActivity.this, erro, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };

    private View.OnClickListener clickEntrar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser!=null){
                mAuth.signOut();
            }
            finish();
        }
    };
}
