package com.udacity.sandwichclub.utils;

import android.content.Context;
import android.util.Log;

import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(Context context, String json) {
        try {
            JSONObject sandwichObject = new JSONObject(json);

            JSONObject sandwichNameObject = sandwichObject.getJSONObject("name");
            String name = sandwichNameObject.getString("mainName");

            JSONArray alsoKnownAsArr = sandwichNameObject.optJSONArray("alsoKnownAs");
            List<String> alsoKnownAs = new ArrayList<>();
            if (alsoKnownAsArr != null) {
                for (int i = 0; i < alsoKnownAsArr.length(); i++) {
                    alsoKnownAs.add(alsoKnownAsArr.optString(i));
                }
            }
            if (alsoKnownAs.isEmpty()) {
                alsoKnownAs.add(context.getResources().getString(R.string.na_error_message));
            }

            String placeOfOrigin = sandwichObject.optString("placeOfOrigin");
            if (placeOfOrigin == null || placeOfOrigin.equals("")) {
                placeOfOrigin = context.getResources().getString(R.string.na_error_message);
            }

            String description = sandwichObject.optString("description", context.getResources().getString(R.string.na_error_message));

            String image = sandwichObject.optString("image");

            JSONArray ingredientsArr = sandwichObject.optJSONArray("ingredients");
            List<String> ingredients = new ArrayList<>();
            if (ingredientsArr != null) {
                for (int i = 0; i < ingredientsArr.length(); i++) {
                    ingredients.add(ingredientsArr.optString(i));
                }
            }
            if (ingredients.isEmpty()) {
                ingredients.add(context.getResources().getString(R.string.na_error_message));
            }

            return new Sandwich(name, alsoKnownAs, placeOfOrigin, description, image, ingredients);
        } catch (Exception e){
            return null;
        }
    }
}
