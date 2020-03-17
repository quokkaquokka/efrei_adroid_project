package efrei.moutte.moutte_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import efrei.moutte.moutte_project.R;
import efrei.moutte.moutte_project.adapters.RecyclerViewAdapter;
import efrei.moutte.moutte_project.models.Annonce;

public class MainActivity extends AppCompatActivity {

    private final String JSON_URL = "https://9kmebpt3vb-dsn.algolia.net/1/indexes/annonce?x-algolia-application-id=9KMEBPT3VB&x-algolia-api-key=6b3e5d8cef4bc3f48fd4b566b270dd0b" ;
    private JsonObjectRequest request ;
    private RequestQueue requestQueue ;
    private List<Annonce> lstAnnonce ;
    private RecyclerView recyclerView ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstAnnonce = new ArrayList<>() ;
        recyclerView = findViewById(R.id.recyclerViewAnnonce);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            jsonrequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_villes) {
            Intent villesIntent = new Intent(this, VilleActivity.class);
            startActivity(villesIntent);
            return true;
        }
        if (id == R.id.action_login) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            return true;
        }
        if (id == R.id.action_annonces) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void jsonrequest() throws JSONException {

        request = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObject  = null ;
                try {
                        JSONArray arr = response.getJSONArray("hits");
                        for(int i = 0; i < arr.length(); i++) {
                            jsonObject = arr.getJSONObject(i);
                            Annonce annonce = new Annonce();

                            annonce.setObjectID(jsonObject.getString("objectID"));
                            annonce.setId_annonce(jsonObject.getString("id_annonce"));
                            annonce.setVille(jsonObject.getString("ville"));
                            annonce.setSource(jsonObject.getString("source"));
                            annonce.setType(jsonObject.getString("type"));
                            annonce.setLoyer(jsonObject.getInt("loyer"));
                            annonce.setPrix(jsonObject.getInt("prix"));
                            annonce.setCp(jsonObject.getString("cp"));
                            annonce.setSurface(jsonObject.getInt("surface"));
                            annonce.setPrixm2(jsonObject.getInt("prixm2"));
                            annonce.setRendement((float) jsonObject.getDouble("rendement"));
                            annonce.setInvestissement(jsonObject.getInt("investissement"));
                            annonce.setNbpieces(jsonObject.getInt("nbpieces"));
                            annonce.setNeuf(jsonObject.getBoolean("neuf"));
                            annonce.setPermalien(jsonObject.getString("permalien"));
                            annonce.setDescription(jsonObject.getString("description"));
                            annonce.setImg_url(jsonObject.getString("img"));

                            lstAnnonce.add(annonce);
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setUpRecyclerView(lstAnnonce);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Referer", "https://www.rendementlocatif.com/investissement/annonces");
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }

    private void setUpRecyclerView(List<Annonce> lstAnnonce) {
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, lstAnnonce) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

    }
}
