package efrei.moutte.moutte_project.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import efrei.moutte.moutte_project.R;
import efrei.moutte.moutte_project.models.Annonce;
import efrei.moutte.moutte_project.models.Authentification;
import efrei.moutte.moutte_project.models.Constante;

public class AnnonceActivity extends AppCompatActivity {
    private List<String> location_types = new ArrayList<String>();
    String objectID = null;

    private JsonObjectRequest request ;
    private RequestQueue requestQueue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce);

        // hide the default actionbar
        // getSupportActionBar().hide();


        // Recieve data

        String ville  = getIntent().getExtras().getString("annonce_ville");
        String loyet = getIntent().getExtras().getString("annonce_loyet");
        String description = getIntent().getExtras().getString("annonce_description");
        String price = getIntent().getExtras().getString("annonce_price");
        String type = getIntent().getExtras().getString("annonce_type");
        String rendement = getIntent().getExtras().getString("annonce_rendement");
        String link = getIntent().getExtras().getString("annonce_link");
        String image_url = getIntent().getExtras().getString("annonce_img") ;
        objectID = getIntent().getExtras().getString("annonce_objectId");
        // String id = getIntent().getExtras().getString("annonce_id");

        // ini views

        // CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);
        // collapsingToolbarLayout.setTitleEnabled(true);

        TextView tv_ville = findViewById(R.id.annonce_ville);
        TextView tv_loyet = findViewById(R.id.annonce_loyet);
        TextView tv_description = findViewById(R.id.annonce_description);
        TextView tv_price= findViewById(R.id.annonce_price);
        TextView tv_type  = findViewById(R.id.annonce_type) ;
        TextView tv_rendement  = findViewById(R.id.annonce_redement);
        TextView tv_link = findViewById(R.id.annonce_link);
        ImageView img = findViewById(R.id.annonce_img);

        // setting values to each view

        tv_ville.setText(ville);
        tv_loyet.setText(loyet);
        tv_description.setText(description);
        tv_price.setText(price);
        tv_type.setText(type);
        tv_rendement.setText(rendement);
        tv_link.setText(link);
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);


        // set image using Glide
        Glide.with(this).load(image_url).apply(requestOptions).into(img);

        if(Authentification.getInstance() == null) {
            LinearLayout linear_layout = findViewById(R.id.content_connected);
            linear_layout.setVisibility(View.INVISIBLE);

            TextView tv_warning = findViewById(R.id.annonce_warning);
            tv_warning.setText("Si vous souhaitez, entrer des informations supplémentaires dans l'annonce du bien. Veuillez-vous connecter!");
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

    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkbox_location_nu:
                if (checked){
                    location_types.add("Location nu");
                }
                else
                    break;
            case R.id.checkbox_location_meuble:
                if (checked) {
                    location_types.add("Location meublée");
                }
                else
                    break;
            case R.id.checkbox_location_meuble_lcd:
                if (checked) {
                    location_types.add("Location meublée et courte durée");
                }
                else
                    break;
            case R.id.checkbox_location_cd:
                if (checked) {
                    location_types.add("Location courte durée");
                }
                else
                    break;

        }
    }
    public void onSubmit(View view) throws JSONException {

        EditText et_travaux = findViewById(R.id.editText_travaux);
        EditText et_montant = findViewById(R.id.editText_Montant);

        String travaux = et_travaux.getText().toString();
        String montant = et_montant.getText().toString();

        jsonrequest(travaux, montant,location_types);
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void jsonrequest(String travaux, String montant, List<String> location_types) throws JSONException {
        String user_id = Authentification.getInstance().getId();
        String email = Authentification.getInstance().getEmail();
        String password = Authentification.getInstance().getPassword();
        List<String> villes = Authentification.getInstance().getVilles();
        List<Annonce> annonces = Authentification.getInstance().getAnnonces();
        String JSON_URL = "https://9kmebpt3vb-dsn.algolia.net/1/indexes/user/" + user_id+ "?x-algolia-application-id=9KMEBPT3VB&x-algolia-api-key=" + Constante.getAlgolia_key_admin;

        Annonce annonce = new Annonce();
        annonce.setObjectID(objectID);
        annonce.setMontant(montant);
        annonce.setTravaux(travaux);
        annonce.setLocations(location_types);
        annonces.add(annonce);
        JSONObject params = new JSONObject();
        params.put("email", email);
        params.put("password", password);
        params.put("annonces", annonces);
        params.put("villes", villes);

        request = new JsonObjectRequest(Request.Method.PUT, JSON_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObject  = null ;
                Context context = getApplicationContext();
                CharSequence text = "Vous avez enregistré les données!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getApplicationContext();
                CharSequence text = "Une erreur est survenue, nous n'avons pas pu enregistré les informations!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

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

        requestQueue = Volley.newRequestQueue(AnnonceActivity.this);
        requestQueue.add(request) ;


    }
}
