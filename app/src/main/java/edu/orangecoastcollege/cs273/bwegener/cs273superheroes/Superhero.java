package edu.orangecoastcollege.cs273.bwegener.cs273superheroes;

/**
 * Created by Brandy on 10/9/17.
 */

public class Superhero {

    private String mUserName;
    private String mName;
    private String mPower;
    private String mThing;
    private String mFileName;

    public Superhero(String userName, String name, String power, String thing)
    {
        mUserName = userName;
        mName = name;
        mPower = power;
        mThing = thing;
        mFileName = userName + ".png";
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

    public String getFileName() { return mFileName; }

    @Override
    public String toString() {
        return "Superhero{" +
                "mUserName='" + mUserName + '\'' +
                ", mName='" + mName + '\'' +
                ", mPower='" + mPower + '\'' +
                ", mThing='" + mThing + '\'' +
                ", mFileName='" + mFileName + '\'' +
                '}';
    }
}
