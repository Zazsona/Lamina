package user;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class UserProfileManager //TODO: Replace usage of names with UUID.
{
    private static CurrentUserProfile currentUserProfile;
    private static HashMap<String, UserProfile> userProfiles = new HashMap<>();
    private static HashMap<String, Long> userProfileTime = new HashMap<>();
    private static Timer profileCleanerTimer;

    /**
     * Gets the profile for this user
     * @return the profile for the user at this system
     */
    public static CurrentUserProfile getCurrentUserProfile()
    {
        if (currentUserProfile == null)
        {
            try
            {
                currentUserProfile = new CurrentUserProfile();
                currentUserProfile.restore();
            }
            catch (IOException e)
            {
                System.err.println("The user profile has become corrupted or is missing. Resetting stored data.");
                currentUserProfile.deleteFile();
                currentUserProfile.save();
                //If the delete fails, there's nothing we can do. The OS is preventing us from accessing data. As such, we'll return a profile with default values.
            }
        }
        return currentUserProfile;
    }

    /**
     * Gets User Profiles
     * @return gets the user profile map. Index is the user's name.
     */
    public static HashMap<String, UserProfile> getUserProfiles()
    {
        return userProfiles;
    }

    /**
     * Gets user profile from name
     * @param name the name of the user to get
     * @return the user's profile
     */
    public static UserProfile getUserProfile(String name)
    {
        return userProfiles.get(name);
    }

    /**
     * Adds the user profile to the map. If the map is empty, this starts the profile removal timer.
     * @param userProfile the profile to add
     */
    public static void addUserProfile(UserProfile userProfile)
    {
        if (userProfiles.size() == 0)
        {
            profileCleanerTimer = new Timer(true);
            profileCleanerTimer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    ArrayList<UserProfile> profilesToRemove = new ArrayList<>();
                    Iterator profileTimeIterator = userProfileTime.entrySet().iterator();
                    while (profileTimeIterator.hasNext())
                    {
                        HashMap.Entry<String, Long> entry = (HashMap.Entry<String, Long>) profileTimeIterator.next();
                        if (Instant.now().toEpochMilli() - entry.getValue() >= 1000*30)
                        {
                            profilesToRemove.add(userProfiles.get(entry.getKey())); //We cannot remove these directly, as it will result in concurrent modification with the iterator.
                        }
                    }
                    for (UserProfile profile : profilesToRemove)
                    {
                        removeUserProfile(profile);
                    }
                }
            }, 1000*32, 1000*32);
        }


        if (userProfiles.containsKey(userProfile.getName()))
        {
            userProfiles.replace(userProfile.getName(), userProfile);
            userProfileTime.replace(userProfile.getName(), Instant.now().toEpochMilli());
        }
        else
        {
            userProfiles.put(userProfile.getName(), userProfile);
            userProfileTime.put(userProfile.getName(), Instant.now().toEpochMilli());
        }
    }

    /**
     * Removes the user profile
     * @param profile the profile to remove
     */
    public static void removeUserProfile(UserProfile profile)
    {
        userProfiles.remove(profile.getName());
        userProfileTime.remove(profile.getName());
        if (userProfiles.size() == 0)
        {
            profileCleanerTimer.cancel();
        }
    }
}
