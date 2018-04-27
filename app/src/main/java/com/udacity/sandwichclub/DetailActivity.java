package com.udacity.sandwichclub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            Log.e("DETAILS", "no intent");
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            Log.e("DETAILS", "no position");
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(this, json);
        if (sandwich == null) {
            // Sandwich data unavailable
            Log.e("DETAILS", "no sandwich");
            closeOnError();
            return;
        }

        populateUI(sandwich);
        String image = sandwich.getImage();
        if (image != "") {
            // https://stackoverflow.com/questions/25744344/android-picasso-load-image-failed-how-to-show-error-message
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    TextView imageErrorTv = findViewById(R.id.image_error_tv);
                    imageErrorTv.setText(R.string.image_na_error_message);
                }
            });
            builder.build().load(image).into(ingredientsIv);
        }

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        TextView alsoKnownAsTv = findViewById(R.id.also_known_tv);
        alsoKnownAsTv.setText(sandwich.getAlsoKnownAs().toString().replace("[", "").replace("]", ""));
        TextView originTv = findViewById(R.id.origin_tv);
        originTv.setText(sandwich.getPlaceOfOrigin());
        TextView descriptionTv = findViewById(R.id.description_tv);
        descriptionTv.setText(sandwich.getDescription());
        TextView ingredientsTv = findViewById(R.id.ingredients_tv);
        ingredientsTv.setText(sandwich.getIngredients().toString().replace("[", "").replace("]", ""));
    }
}
