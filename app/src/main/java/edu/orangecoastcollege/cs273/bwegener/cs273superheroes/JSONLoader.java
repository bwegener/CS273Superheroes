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
     *
     *
     */
    public static List<Superhero> loadJSONFromAsset(Context context) throws IOException{
        List<Superhero> allSuperheroesList = new ArrayList<>();

        String json = null;

        InputStream is = context.getAssets().open("Superheroes.json");

        int size = is.available();

        byte[] buffer = new byte[size];

        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");

        try {
            JSONObject jsonRootObject = new JSONObject(json);
            JSONArray allSuperheroesJSON = jsonRootObject.getJSONArray("Superheroes");

            int length = allSuperheroesJSON.length();
            for(int i = 0; i < length; ++i)
            {
                JSONObject superheroObject = allSuperheroesJSON.getJSONObject(i);

                String userName = superheroObject.getString("Username");
                String name = superheroObject.getString("Name");
                String power = superheroObject.getString("Superpower");
                String thing = superheroObject.getString("OneThing");

                Superhero superhero = new Superhero(userName, name, power, thing);

                allSuperheroesList.add(superhero);
            }
        }
        catch(JSONException e)
        {
            Log.e("Supeheroes", e.getMessage());
        }

        return allSuperheroesList;


    }

}
