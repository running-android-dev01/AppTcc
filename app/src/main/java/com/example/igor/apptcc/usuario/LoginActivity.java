package com.example.igor.apptcc.usuario;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.igor.apptcc.MainActivity;
import com.example.igor.apptcc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private static String TAG = LoginActivity.class.getName();
    private FirebaseAuth mAuth;

    private EditText edtEmail;
    private EditText edtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        Button btnAcessar = findViewById(R.id.btnAcessar);
        TextView txtCadastrar = findViewById(R.id.txtCadastrar);

        btnAcessar.setOnClickListener(clickAcessar);
        txtCadastrar.setOnClickListener(clickCadastrar);
    }

    private View.OnClickListener clickAcessar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String email = edtEmail.getText().toString();
            final String senha = edtSenha.getText().toString();

            Boolean erro = false;
            edtEmail.setError(null);
            edtSenha.setError(null);

            if (TextUtils.isEmpty(email)){
                erro = true;
                edtEmail.setError("Informe o email!");
            }
            if (TextUtils.isEmpty(senha)){
                erro = true;
                edtSenha.setError("Informe a senha!");
            }

            if (erro){
                return;
            }

            mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        String erro = UsuarioUtil.getErroFirebaseAuth(LoginActivity.this, ((FirebaseAuthException)task.getException()));
                        Toast.makeText(LoginActivity.this, erro, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };

    private View.OnClickListener clickCadastrar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(LoginActivity.this, NovoLoginActivity.class));
        }
    };
}
