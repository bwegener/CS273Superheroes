package edu.orangecoastcollege.cs273.bwegener.cs273superheroes;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandy on 10/9/17.
 */

public class JSONLoader {

    /**
     * Loads JSON data from a file in the assets directory.
     */
    public static List<Superhero> loadJSONFromAsset(Context context) throws IOException {

        List<Superhero> allSuperheroesList = new ArrayList<>();

        InputStream is = context.getAssets().open("cs273superheroes.json");

        int size = is.available();

        byte[] buffer = new byte[size];

        is.read(buffer);
        is.close();

        String json = new String(buffer, "UTF-8");

        try {
            JSONObject jsonRootObject = new JSONObject(json);
            JSONArray allSuperheroesJSON = jsonRootObject.getJSONArray("CS273Superheroes");

            int length = allSuperheroesJSON.length();

            for (int i = 0; i < length; i++) {

                JSONObject superHeroJSON = allSuperheroesJSON.getJSONObject(i);

                String userName = superHeroJSON.getString("Username");
                String name = superHeroJSON.getString("Name");
                String power = superHeroJSON.getString("Superpower");
                String thing = superHeroJSON.getString("OneThing");

                Superhero superhero = new Superhero(userName, name, power, thing);

                allSuperheroesList.add(superhero);
            }
        } catch (JSONException e) {
            Log.e("CS 273 Superheroes", e.getMessage());
        }

        return allSuperheroesList;


    }

}
