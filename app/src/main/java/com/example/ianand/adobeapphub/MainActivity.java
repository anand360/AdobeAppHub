package com.example.ianand.adobeapphub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adobeapphub.listview.library.JSONParser;


public class MainActivity extends ActionBarActivity {
    ListView list;
    TextView name;
    TextView inapp;
    TextView pCount;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<>();
    //URL to get JSON Array
    private static String url = "http://adobe.0x10.info/api/products?type=json";
    //JSON Node Names
    private static final String TAG_OS = "android";
    private static final String TAG_NAME = "name";
    private static final String TAG_INAPP = "inapp-purchase";
    private static final String TAG_TYPE = "type";
    private static final String TAG_URL = "url";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_RATING = "rating";
    private static final String TAG_LASTUPDATED = "last updated";
    private static final String TAG_DESC = "description";
    JSONArray android = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oslist = new ArrayList<>();
        pCount = (TextView)findViewById(R.id.pCount);
        new JSONParse().execute();
    }

    private class JSONParse extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            name = (TextView)findViewById(R.id.name);
            inapp = (TextView)findViewById(R.id.inapppurchase);
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONArray json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                android = json;
                for(int i = 0; i < android.length(); i++){
                    JSONObject c = android.getJSONObject(i);
                    // Storing  JSON item in a Variable
                    String name = c.getString(TAG_NAME);
                    String inAppPurchase = c.getString(TAG_INAPP);
                    String type = c.getString(TAG_TYPE);
                    String appUrl = c.getString(TAG_URL);
                    String appImage = c.getString(TAG_IMAGE);
                    String appRating = c.getString(TAG_RATING);
                    String appLastUpdated = c.getString(TAG_LASTUPDATED);
                    String appDescription = c.getString(TAG_DESC);

                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_NAME, name);
                    map.put(TAG_INAPP, "In-App: "+ inAppPurchase);
                    map.put(TAG_TYPE, type);
                    map.put(TAG_URL, appUrl);
                    map.put(TAG_IMAGE, appImage);
                    map.put(TAG_RATING, appRating);
                    map.put(TAG_LASTUPDATED, appLastUpdated);
                    map.put(TAG_DESC, appDescription);
                    if(!name.isEmpty()) {
                        oslist.add(map);
                    }

                    list=(ListView)findViewById(R.id.list);

                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, oslist,
                            R.layout.list_v,
                            new String[] { TAG_NAME, TAG_INAPP }, new int[] {
                            R.id.name, R.id.inapppurchase});
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                           //Toast.makeText(MainActivity.this, "You Clicked at "+oslist.get(+position).get("name"), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), AppDetailActivity.class);

                            i.putExtra("AppDetail", oslist.get(+position));
                            startActivity(i);
                        }
                    });
                }

                pCount.append(Integer.toString(android.length() - 1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
