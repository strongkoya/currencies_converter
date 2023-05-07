package com.example.examentp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity {

    private EditText editText;
    private RecyclerView recyclerView;
    private ElementAdapter elementAdapter;
    private MaterialSpinner spinner1;
    private String exchangeRate ="EUR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        spinner1 = (MaterialSpinner) findViewById(R.id.spn);
        spinner1.setItems(new String[]{"currencies", "efefe"});
        getCurrencies();

        //  spinner.setItems("Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow");
        spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //  Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                exchangeRate = item;
                allCurrencies();

            }
        });

        Intent intent = getIntent();
        String amount = intent.getStringExtra("amount");
        editText = findViewById(R.id.etFirstCurrency);
        editText.setText(amount);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Mettre à jour les éléments de RecyclerView en fonction de la recherche
                //recyclerViewAdapter.getFilter().filter(s.toString());
                allCurrencies();
                //  Toast.makeText(getContext(),s.toString(),Toast.LENGTH_SHORT).show();
                //  Log.d("EditText :     ",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        allCurrencies();


        elementAdapter = new ElementAdapter(new ArrayList<>());
    }
    private void allCurrencies() {
        String amount = "1";
        if(!editText.getText().toString().equals("")) {
            amount = editText.getText().toString();
        }
        else{editText.setText(amount);}


            String url = "https://m1mpdam-exam.azurewebsites.net/CurrencyExchange/ExchangeRates/"  + exchangeRate + "/" + amount + "/";
        //https://m1mpdam-exam.azurewebsites.net/CurrencyExchange/ExchangeRates/EUR/3/
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
                              // double value = jsonNode.get("rates").get(exchangeRate).asDouble();


                                // Imprimer la valeur extraite

                                List<Element> elementList = new ArrayList<>();
                                JsonNode ratesNode = jsonNode.get("rates");
                                if (ratesNode != null) {
                                    Iterator<Map.Entry<String, JsonNode>> fields = ratesNode.fields();
                                    while (fields.hasNext()) {
                                        Map.Entry<String, JsonNode> entry = fields.next();
                                        String currency = entry.getKey();
                                      //  Toast.makeText(getApplicationContext(), "currency = "+currency, Toast.LENGTH_LONG).show();
                                        double value = entry.getValue().asDouble();
                                        String somme = String.valueOf(value);
                                        Element element = new Element(currency, somme);
                                        elementList.add(element);
                                    }
                                }
                                Toast.makeText(getApplicationContext(), "elementList = "+elementList.size(), Toast.LENGTH_LONG).show();

                                elementAdapter.setElementList(elementList);


                                elementAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(elementAdapter);
                                elementAdapter.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(Element element) {
                                          //Toast.makeText(getApplicationContext(), "youe have clicked !!!!!" + element.getCurrency(), Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), ThirdActivty.class);
                                        intent.putExtra("amount", editText.getText().toString());
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                          /*Intent intent = new Intent(getApplicationContext(), ElementDetail.class);
                                        intent.putExtra("url", element.getUrl());
                                        intent.putExtra("title", element.getTitle());
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                                    }
                                });

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
                                Toast.makeText(getApplicationContext(), "fgfEssayez une autre ville !!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(SecondActivity.this);
            requestQueue.add(stringRequest);

    }



    // Vérifie si le terminal est connecté à Internet
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
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
                            Toast.makeText(getApplicationContext(), currencies[2]+ "2222", Toast.LENGTH_LONG).show();

                            spinner1.setItems(currencies);
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

        RequestQueue requestQueue = Volley.newRequestQueue(SecondActivity.this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}