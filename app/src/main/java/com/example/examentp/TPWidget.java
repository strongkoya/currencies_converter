package com.example.examentp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RemoteViews;
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

import java.util.Objects;

public class TPWidget extends AppWidgetProvider {

    private static String widgetText;
    private static String currencyExchange ="EUR";
    private static String exchangeRate ="USD";
    static String tauxDeChange = "1.23456";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, final ConversionCallback callback) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.t_p_widget);
        convert(context, new ConversionCallback() {
            @Override
            public void onConversionComplete(String tauxDeChange1) {
                widgetText = "1 EUR = " + tauxDeChange1 + " $";
                views.setTextViewText(R.id.appwidget_text, widgetText);
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        });
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, new ConversionCallback() {
                @Override
                public void onConversionComplete(String tauxDeChange) {
                    TPWidget.tauxDeChange = tauxDeChange;
                }
            });
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    private static void convert(Context context, final ConversionCallback callback) {
        String amount = "1";


            String url = "https://m1mpdam-exam.azurewebsites.net/CurrencyExchange/ExchangeRate/" + currencyExchange + "/" + exchangeRate + "/" + amount + "/";
            // Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
            System.out.println(url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                          //  Toast.makeText(getApplicationContext(), response.length() + " gggg", Toast.LENGTH_LONG).show();

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
                                String tauxDeChange= String.valueOf(value);
                                callback.onConversionComplete(tauxDeChange);

                                // Imprimer la valeur extraite

                            } else {

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            requestQueue.add(stringRequest);

    }


    // Vérifie si le terminal est connecté à Internet
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
