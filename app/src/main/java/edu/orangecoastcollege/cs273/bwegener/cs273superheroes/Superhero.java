package edu.orangecoastcollege.cs273.bwegener.cs273superheroes;

/**
 * The <code>Superhero</code> contains a superhero with their user name,
 * real name, power, and thing special to them
 *
 * @author Brian Wegener
 * @version 1.0
 *
 * Created by bwegener on 10/9/17.
 */

public class Superhero {

    private String mUserName;
    private String mName;
    private String mPower;
    private String mThing;
    private String mFileName;

    /**
     * This creates a superhero with a user name, real name, power, and one thing special to them
     * it also creates a file that grabs the .png
     *
     * @param userName the user name of the superhero (student)
     * @param name the real name of the superhero
     * @param power the power of the superhero
     * @param thing the one thing special about the superhero
     */
    public Superhero(String userName, String name, String power, String thing)
    {
        mUserName = userName;
        mName = name;
        mPower = power;
        mThing = thing;
        mFileName = userName + ".png";
    }

    /**
     * This gets the superhero's user name
     * @return the superhero's user name
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * This gets the superhero's real name
     * @return the superhero's real name
     */
    public String getName() {
        return mName;
    }

    /**
     * This gets the superhero's power
     * @return the superhero's power
     */
    public String getPower() {
        return mPower;
    }

    /**
     * This gets the superhero's one special thing
     * @return the superhero's one special thing
     */
    public String getThing() {
        return mThing;
    }

    /**
     * This gets the file name associated with the superhero
     * @return
     */
    public String getFileName() { return mFileName; }

    /**
     * This prints a toString with a user name, real name,
     * power, one special thing, and file name
     * @return
     */
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
