package com.example.ftec_230601;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    TextView nome, sobrenome, email, endereco, cidade, estado, username, senha, nascimento, telefone, latitude, longitude;
    ImageView foto;
    ProgressDialog load;
    TextView status;
    Button irMapa, obterUsuario;
    PessoaObj pessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = (TextView) findViewById(R.id.tv_firstName);
        sobrenome = (TextView) findViewById(R.id.tv_lastName);
        email = (TextView) findViewById(R.id.tv_email);
        endereco = (TextView) findViewById(R.id.tv_address);
        cidade = (TextView) findViewById(R.id.tv_city);
        estado = (TextView) findViewById(R.id.tv_state);
        username = (TextView) findViewById(R.id.tv_username);
        senha = (TextView) findViewById(R.id.tv_password);
        nascimento = (TextView) findViewById(R.id.tv_birthday);
        telefone = (TextView) findViewById(R.id.tv_phone);
        foto = (ImageView) findViewById(R.id.iv_avatar);

        latitude = (TextView) findViewById(R.id.tv_latitude);
        longitude = (TextView) findViewById(R.id.tv_longitude);

        status = (TextView) findViewById(R.id.tv_progressDialog);

        irMapa = findViewById(R.id.btn_map);
        obterUsuario = findViewById(R.id.btn_newUser);

        obterUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewRandomUser();
            }
        });

        getNewRandomUser();

        irMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double lat, lon;
                String locationName;
                if(pessoa != null) {

                    lat = pessoa.getLatitude();
                    lon = pessoa.getLongitude();

                    // Bug, a lat e lon informados pelo app apontam aleatoriamente no mapa !!
                    // o trecho de codigo abaixo encontra as coordenadas pelo endereço descrito aproximado
                    locationName = pessoa.getEstado() + ", " + pessoa.getPais();

                    Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List addressList= gc.getFromLocationName( locationName, 1);
                        Log.i("Local", addressList.toString());
                        if(addressList != null && addressList.size() > 0){
                            Address address = (Address) addressList.get(0);
                            if(address.hasLatitude() && address.hasLongitude()){
                                lat = address.getLatitude();
                                lon = address.getLongitude();
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    pessoa.getFoto().compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    byte[] byteArray = bStream.toByteArray();

                    Intent intent = new Intent(MainActivity.this, UserMapActivity.class);
                    intent.putExtra("latitude", lat);
                    intent.putExtra("longitude", lon);
                    intent.putExtra("nome", pessoa.getPais());
                    intent.putExtra("sobrenome", pessoa.getEstado());
                    intent.putExtra("foto", byteArray);

                    startActivity(intent);
                }
            }
        });

    }

    private void getNewRandomUser(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                // onPreExecute Method
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        status.setText("iniciando requisições");
                    }
                });
                // doInBackground
                Utils util = new Utils();

                //PessoaObj pessoa =  util.getInformacao("https://randomuser.me/api/0.7");
                pessoa =  util.getInformacao("https://randomuser.me/api/1.4");

                // onPostExecute
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nome.setText(pessoa.getNome().substring(0,1).toUpperCase()+pessoa.getNome().substring(1));
                        sobrenome.setText(pessoa.getSobrenome().substring(0,1).toUpperCase()+pessoa.getSobrenome().substring(1));
                        email.setText(pessoa.getEmail());
                        endereco.setText(pessoa.getEndereco());
                        cidade.setText(pessoa.getCidade().substring(0,1).toUpperCase()+pessoa.getCidade().substring(1));
                        estado.setText(pessoa.getEstado());
                        username.setText(pessoa.getUsername());
                        senha.setText(pessoa.getSenha());
                        nascimento.setText(pessoa.getNascimento());
                        telefone.setText(pessoa.getTelefone());
                        foto.setImageBitmap(pessoa.getFoto());

                        latitude.setText(Double.toString(pessoa.getLatitude()));
                        longitude.setText(Double.toString(pessoa.getLongitude()));
                        status.setText("Feito");
                    }
                });
            }
        });
    }

}