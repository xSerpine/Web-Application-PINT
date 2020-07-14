package com.example.cliente_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cliente_app.viagensrealizadas.realizada;
import com.example.cliente_app.viagensrealizadas.realizadaadapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ViagensRealizadas extends AppCompatActivity {

    private RecyclerView mrecyclerView;
    private realizadaadapter madapterviagem;

    protected ArrayList<realizada> mviagensrealizadas;
    String origem, destino, data, hora, custo, npessoas;
    int id;
    String email;
    TextView vazio;

    //--------------MENU-------------//

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_marcarviagem:
                startActivity(new Intent(this, MarcarViagem.class));
                return true;
            case R.id.nav_marcadas:
                startActivity(new Intent(this, ViagensMarcadas.class));
                return true;
            case R.id.nav_realizadas:
                startActivity(new Intent(this, ViagensRealizadas.class));
                return true;
            case R.id.nav_notificacoes:
                startActivity(new Intent(this, Notificacoes.class));
                return true;
            case R.id.nav_perfil:
                startActivity(new Intent(this, Perfil.class));
                return true;
            case R.id.nav_termos:
                startActivity(new Intent(this, Termos.class));
                return true;
            case R.id.nav_logout:
                startActivity(new Intent(this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //--------------!MENU-------------//



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viagensrealizadas);

        mrecyclerView = (RecyclerView) findViewById(R.id.notirecycl);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("Pref", 0);
        final SharedPreferences.Editor editor = pref.edit();
        email = pref.getString("email", null);

        vazio = (TextView) findViewById(R.id.txtvazio);
        mviagensrealizadas = new ArrayList<>();

        get();
        mviagensrealizadas.clear();
    }

    private void get() {

        String url = "https://api-pint.herokuapp.com/mobile/viagens/realizadas/" + email;

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            mviagensrealizadas = new ArrayList<realizada>();
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject viagem = jsonArray.getJSONObject(i);

                                id = Integer.valueOf(viagem.getString("id_viagem"));
                                origem = viagem.getString("origem");
                                destino = viagem.getString("destino");
                                data = viagem.getString("data_ida").substring(0, 10);
                                hora = viagem.getString("hora_ida").substring(0,4);
                                custo = viagem.getString("custo").substring(1,5);
                                npessoas = viagem.getString("num_pessoas");

                                mviagensrealizadas.add(new realizada(id, origem, destino, data, hora, custo, npessoas));
                            }

                            madapterviagem = new realizadaadapter(ViagensRealizadas.this, mviagensrealizadas);
                            mrecyclerView.setAdapter(madapterviagem);
                            if (mviagensrealizadas.isEmpty()) {
                                mrecyclerView.setVisibility(View.INVISIBLE);
                            }
                            vazio.setVisibility(View.GONE);
                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });


        queue.add(postRequest);
    }
}

