package com.example.examentp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private String[] currencies1;
    private String[] currencies2;
    private MaterialSpinner spinner1;
    private MaterialSpinner spinner2;
    private Button btnConvert;
    private EditText editText;
    private String currencyExchange="EUR";
    private String exchangeRate="EUR";
    private EditText etSecondCurrency;
    private Button btnAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConvert = findViewById(R.id.btnConvert);
        btnAll = findViewById(R.id.btnAll);
        editText = findViewById(R.id.etFirstCurrency);
        etSecondCurrency = findViewById(R.id.etSecondCurrency);
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(editText.getText().toString())) {
                    // Afficher un message d'erreur pour informer l'utilisateur que l'EditText est requis
                    editText.setError("Ce champ est requis");
                } else {
                    // Exécuter l'action associée au bouton
                    // ...
                    Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                    intent.putExtra("amount", editText.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        });


         spinner1 = (MaterialSpinner) findViewById(R.id.spnFirstCountry);
        spinner2 = (MaterialSpinner) findViewById(R.id.spnSecondCountry);
        getCurrencies();

     //  spinner.setItems("Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow");
        spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
              //  Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                currencyExchange = item;

            }
        });

        spinner2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

              //  Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                 exchangeRate = item;
            }
        });

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    // Afficher un message d'erreur pour informer l'utilisateur que l'EditText est requis
                    editText.setError("Ce champ est requis");
                } else {
                    // Exécuter l'action associée au bouton
                    // ...
                     convert();
                }


            }
        });




    }

    private void getCurrencies() {
        String url = "https://m1mpdam-exam.azurewebsites.net/CurrencyExchange/Currencies/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 0) {
                            Gson gson = new Gson();
                            String[] currencies = gson.fromJson(response, String[].class);
                         //   Toast.makeText(getApplicationContext(), currencies[0], Toast.LENGTH_LONG).show();

                            spinner1.setItems(currencies);
                            spinner2.setItems(currencies);
                        } else {
                            if (!isNetworkAvailable(getApplicationContext())) {
                                Toast.makeText(getApplicationContext(), getString(R.string.erreur1), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.erreur2), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!isNetworkAvailable(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(), getString(R.string.erreur1), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.erreur2), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void convert() {
        String amount = "1";
        if(!editText.getText().toString().equals("")) {
            amount = editText.getText().toString();
        }
        else{editText.setText(amount);}
        if(Objects.equals(currencyExchange, exchangeRate)){
            etSecondCurrency.setText(amount);
        }
        else {

            String url = "https://m1mpdam-exam.azurewebsites.net/CurrencyExchange/ExchangeRate/" + currencyExchange + "/" + exchangeRate + "/" + amount + "/";
            // Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
            System.out.println(url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), response.length() + " gggg", Toast.LENGTH_LONG).show();

                            if (response.length() > 0) {
                                // Créer un objet ObjectMapper pour traiter la réponse JSON
                                ObjectMapper mapper = new ObjectMapper();

                                // Analyser la réponse JSON en un objet JsonNode
                                JsonNode jsonNode = null;
                                try {
                                    jsonNode = mapper.readTree(response);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }

                                // Extraire la valeur de "AUD"
                                double value = jsonNode.get("rates").get(exchangeRate).asDouble();

                                etSecondCurrency.setText(String.valueOf(value));
                                // Imprimer la valeur extraite

                            } else {
                                if (!isNetworkAvailable(getApplicationContext())) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.erreur1), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.erreur2), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (!isNetworkAvailable(getApplicationContext())) {
                                Toast.makeText(getApplicationContext(), getString(R.string.erreur1), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.erreur2), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
        }
    }


    // Vérifie si le terminal est connecté à Internet
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}