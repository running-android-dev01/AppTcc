package com.example.igormoraes.appbar.usuario;


import android.content.Context;

import com.example.igormoraes.appbar.R;
import com.google.firebase.auth.FirebaseAuthException;

public class UsuarioUtil {
    public static String getErroFirebaseAuth(Context ctx, FirebaseAuthException firebaseAuthException){
        String erroCode = firebaseAuthException.getErrorCode();
        String erro = "";
        if (erroCode.equals("ERROR_INVALID_CUSTOM_TOKEN")){
            erro = ctx.getString(R.string.error_invalid_custom_token);
        }else if (erroCode.equals("ERROR_CUSTOM_TOKEN_MISMATCH")){
            erro = ctx.getString(R.string.error_custom_token_mismatch);
        }else if (erroCode.equals("ERROR_INVALID_CREDENTIAL")){
            erro = ctx.getString(R.string.error_invalid_credential);
        }else if (erroCode.equals("ERROR_INVALID_EMAIL")){
            erro = ctx.getString(R.string.error_invalid_email);
        }else if (erroCode.equals("ERROR_WRONG_PASSWORD")){
            erro = ctx.getString(R.string.error_wrong_password);
        }else if (erroCode.equals("ERROR_USER_MISMATCH")){
            erro = ctx.getString(R.string.error_user_mismatch);
        }else if (erroCode.equals("ERROR_REQUIRES_RECENT_LOGIN")){
            erro = ctx.getString(R.string.error_requires_recent_login);
        }else if (erroCode.equals("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL")){
            erro = ctx.getString(R.string.error_account_exists_with_different_credential);
        }else if (erroCode.equals("ERROR_EMAIL_ALREADY_IN_USE")){
            erro = ctx.getString(R.string.error_email_already_in_use);
        }else if (erroCode.equals("ERROR_CREDENTIAL_ALREADY_IN_USE")){
            erro = ctx.getString(R.string.error_credential_already_in_use);
        }else if (erroCode.equals("ERROR_USER_DISABLED")){
            erro = ctx.getString(R.string.error_user_disabled);
        }else if (erroCode.equals("ERROR_USER_TOKEN_EXPIRED")){
            erro = ctx.getString(R.string.error_user_token_expired);
        }else if (erroCode.equals("ERROR_USER_NOT_FOUND")){
            erro = ctx.getString(R.string.error_user_not_found);
        }else if (erroCode.equals("ERROR_INVALID_USER_TOKEN")){
            erro = ctx.getString(R.string.error_invalid_user_token);
        }else if (erroCode.equals("ERROR_OPERATION_NOT_ALLOWED")){
            erro = ctx.getString(R.string.error_operation_not_allowed);
        }else if (erroCode.equals("ERROR_WEAK_PASSWORD") ){
            erro = ctx.getString(R.string.error_weak_password);
        }
        return erro;
    }
}
