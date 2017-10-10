package edu.orangecoastcollege.cs273.bwegener.cs273superheroes;

/**
 * Created by Brandy on 10/9/17.
 */

public class Superhero {

    private String mUserName;
    private String mName;
    private String mPower;
    private String mThing;

    public Superhero(String userName, String name, String power, String thing)
    {
        mUserName = userName;
        mName = name;
        mPower = power;
        mThing = thing;
        name = name.replaceAll(" ", "_");
        power = power.replaceAll(" ", "_");
        thing = thing.replaceAll(" ", "_");
    }

    public String getUserName() {
        return mUserName;
    }

    public String getName() {
        return mName;
    }

    public String getPower() {
        return mPower;
    }


    public String getThing() {
        return mThing;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Superhero superhero = (Superhero) o;

        if (!mName.equals(superhero.mName)) return false;
        if (!mPower.equals(superhero.mPower)) return false;
        if (!mThing.equals(superhero.mThing)) return false;

        // IS THIS RIGHT?
        return true;
    }

    @Override
    public int hashCode() {
        int result = mUserName != null ? mUserName.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mPower != null ? mPower.hashCode() : 0);
        result = 31 * result + (mThing != null ? mThing.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Superhero{" +
                "UserName='" + mUserName + '\'' +
                ", Name='" + mName + '\'' +
                ", Power='" + mPower + '\'' +
                ", Thing='" + mThing + '\'' +
                '}';
    }
}
