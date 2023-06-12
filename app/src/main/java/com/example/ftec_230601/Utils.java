package com.example.ftec_230601;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public PessoaObj getInformacao(String end){
        String json;
        PessoaObj retorno;
        json = NetworkUtils.getJSONFromAPI(end);
        Log.i("Resultado", json);
        retorno = parseJson(json);

        return retorno;
    }

    private PessoaObj parseJson(String json){
        try {
            PessoaObj pessoa = new PessoaObj();

            JSONObject jsonObj = new JSONObject(json);
            JSONArray array = jsonObj.getJSONArray("results");
            JSONObject obj = array.getJSONObject(0);

            //Atribui os objetos que estão nas camadas mais altas
            pessoa.setEmail(obj.getString("email"));
            pessoa.setTelefone(obj.getString("phone"));

            //Dados de login
            JSONObject login = obj.getJSONObject("login");
            pessoa.setUsername(login.getString("username"));
            pessoa.setSenha(login.getString("password"));

            //Data de nascimento
            JSONObject dob = obj.getJSONObject("dob");
            pessoa.setNascimento(dob.getString("date").substring(0,10));


            //Nome da pessoa é um objeto, instancia um novo JSONObject
            JSONObject nome = obj.getJSONObject("name");
            pessoa.setNome(nome.getString("first"));
            pessoa.setSobrenome(nome.getString("last"));

            //Endereco tambem é um Objeto
            JSONObject endereco = obj.getJSONObject("location");
            JSONObject street = endereco.getJSONObject("street");
            pessoa.setEndereco(
                    street.getString("name") + ", "
                            + street.getString("number"));
            pessoa.setEstado(endereco.getString("state"));
            pessoa.setCidade(endereco.getString("city"));
            pessoa.setPais(endereco.getString("country"));

            // Coordenadas
            JSONObject coordenadas = endereco.getJSONObject("coordinates");
            pessoa.setLatitude(coordenadas.getDouble("latitude"));
            pessoa.setLongitude(coordenadas.getDouble("longitude"));

            //Imagem eh um objeto
            JSONObject foto = obj.getJSONObject("picture");
            pessoa.setFoto(baixarImagem(foto.getString("large")));



            return pessoa;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap baixarImagem(String url) {
        try{
            URL endereco;
            InputStream inputStream;
            Bitmap imagem; endereco = new URL(url);
            inputStream = endereco.openStream();
            imagem = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return imagem;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}