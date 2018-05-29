package com.example.sir_c.shared;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    EditText edtPass, edtEmail;
    Button btnLogin;
    Switch swtRec;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("login", MODE_PRIVATE);

        enlazarXml();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMail, strPass;
                strMail = edtEmail.getText().toString();
                strPass = edtPass.getText().toString();
                if(login(strMail, strPass)){
                    savePreferences(strMail, strPass);
                    goToMain();
                }
            }
        });

    }

    private void enlazarXml(){
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        swtRec = (Switch) findViewById(R.id.swtRec);
        setValuesPref();
    }

    private boolean isValidEmail(@NotNull String string){
        return (!TextUtils.isEmpty(string))
                && android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches();
    }

    private boolean isValidPassword (@NotNull String  string){
        return string.length()>=8;
    }

    private boolean login(@NotNull String strEmail,
                          @NotNull String strPass){
        boolean flag = false;
        if (!isValidEmail(strEmail)){
            Toast.makeText(this, "El correo no es valido y/o Vacio", Toast.LENGTH_SHORT).show();
        }else if (!isValidPassword(strPass)){
            Toast.makeText(this, "La contraseña esta vacia o menor de 8 caracteres", Toast.LENGTH_SHORT).show();
        } else {
            flag = true;
        }
        return flag;
    }

    private  void goToMain(){
        Intent intent = new Intent(MainActivity.this,
                DataActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void savePreferences (@NotNull String strEmail,
                                  @NotNull String strPass){
       if(swtRec.isChecked()){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("email", strEmail);
            editor.putString("password", strPass);
            editor.apply();
       }
    }

    @Contract(" -> !null")
    private String getMailPref(){
        return preferences.getString("email", "");
    }

    @Contract(" -> !null")
    private String getPassPref(){
        return preferences.getString("password", "");
    }

    @Contract("_ -> !null")
    private String getPreference(@NotNull String string){
        return preferences.getString(string, "");
    }

    private void setValuesPref(){
        String strUserMail = getMailPref();
        String strPass = getPassPref();

        if (!TextUtils.isEmpty(strUserMail)
                && !TextUtils.isEmpty(strPass)){
            edtPass.setText(strPass);
            edtEmail.setText(strUserMail);
        }
    }
}
