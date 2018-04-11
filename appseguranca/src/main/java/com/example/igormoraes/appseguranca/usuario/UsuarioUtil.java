package com.example.igormoraes.appseguranca.usuario;


import android.content.Context;

import com.example.igormoraes.appseguranca.R;
import com.google.firebase.auth.FirebaseAuthException;

class UsuarioUtil {
    public static String getErroFirebaseAuth(Context ctx, FirebaseAuthException firebaseAuthException){
        String erroCode = firebaseAuthException.getErrorCode();
        String erro = "";
        switch (erroCode) {
            case "ERROR_INVALID_CUSTOM_TOKEN":
                erro = ctx.getString(R.string.error_invalid_custom_token);
                break;
            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                erro = ctx.getString(R.string.error_custom_token_mismatch);
                break;
            case "ERROR_INVALID_CREDENTIAL":
                erro = ctx.getString(R.string.error_invalid_credential);
                break;
            case "ERROR_INVALID_EMAIL":
                erro = ctx.getString(R.string.error_invalid_email);
                break;
            case "ERROR_WRONG_PASSWORD":
                erro = ctx.getString(R.string.error_wrong_password);
                break;
            case "ERROR_USER_MISMATCH":
                erro = ctx.getString(R.string.error_user_mismatch);
                break;
            case "ERROR_REQUIRES_RECENT_LOGIN":
                erro = ctx.getString(R.string.error_requires_recent_login);
                break;
            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                erro = ctx.getString(R.string.error_account_exists_with_different_credential);
                break;
            case "ERROR_EMAIL_ALREADY_IN_USE":
                erro = ctx.getString(R.string.error_email_already_in_use);
                break;
            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                erro = ctx.getString(R.string.error_credential_already_in_use);
                break;
            case "ERROR_USER_DISABLED":
                erro = ctx.getString(R.string.error_user_disabled);
                break;
            case "ERROR_USER_TOKEN_EXPIRED":
                erro = ctx.getString(R.string.error_user_token_expired);
                break;
            case "ERROR_USER_NOT_FOUND":
                erro = ctx.getString(R.string.error_user_not_found);
                break;
            case "ERROR_INVALID_USER_TOKEN":
                erro = ctx.getString(R.string.error_invalid_user_token);
                break;
            case "ERROR_OPERATION_NOT_ALLOWED":
                erro = ctx.getString(R.string.error_operation_not_allowed);
                break;
            case "ERROR_WEAK_PASSWORD":
                erro = ctx.getString(R.string.error_weak_password);
                break;
        }
        return erro;
    }
}
