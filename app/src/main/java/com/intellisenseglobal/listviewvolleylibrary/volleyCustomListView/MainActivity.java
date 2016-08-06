package com.intellisenseglobal.listviewvolleylibrary.volleyCustomListView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.intellisenseglobal.listviewvolleylibrary.AppController;
import com.intellisenseglobal.listviewvolleylibrary.R;
import com.intellisenseglobal.listviewvolleylibrary.adapter.worldsBillionairesAdapter;
import com.intellisenseglobal.listviewvolleylibrary.model.WorldsBillionaires;

import android.app.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
//import android.R;

public class MainActivity extends Activity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Billionaires json url
    private static final String url = "https://raw.githubusercontent.com/mobilesiri/Android-Custom-Listview-Using-Volley/master/richman.json";

    private ProgressDialog pDialog;
    private List<WorldsBillionaires> worldsBillionairesList = new ArrayList<WorldsBillionaires>();
    private ListView listView;
    private worldsBillionairesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
        adapter = new worldsBillionairesAdapter(this, worldsBillionairesList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading…");
        pDialog.show();


        // Creating volley request obj
        JsonArrayRequest billionaireReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                WorldsBillionaires worldsBillionaires = new WorldsBillionaires();
                                worldsBillionaires.setBillionairesname(obj.getString("name"));
                                worldsBillionaires.setBillionairesImgUrl(obj.getString("image"));
                                worldsBillionaires.setWorth(obj.getString("worth"));
                                worldsBillionaires.setYear(obj.getInt("InYear"));
                                worldsBillionaires.setSource(obj.getString("source"));

// adding Billionaire to worldsBillionaires array
                                worldsBillionairesList.add(worldsBillionaires);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // notifying list adapter about data changes
// so that it shows updated data in ListView
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });
//        / Adding request to request queue
        AppController.getInstance().addToRequestQueue(billionaireReq);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }
}

