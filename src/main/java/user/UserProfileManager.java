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
    private static boolean changeOccurred = false;

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
            }, 1000*16, 1000*16);
        }


        if (userProfiles.containsKey(userProfile.getName()))
        {
            if (!userProfiles.get(userProfile.getName()).equals(userProfile))
            {
                userProfiles.replace(userProfile.getName(), userProfile);
                changeOccurred = true;
            }
            userProfileTime.replace(userProfile.getName(), Instant.now().toEpochMilli()); //Keep this, as even though it's identical, it means the user is still around
        }
        else
        {
            changeOccurred = true;
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
        changeOccurred = userProfiles.remove(profile.getName(), profile);
        userProfileTime.remove(profile.getName());
        if (userProfiles.size() == 0)
        {
            profileCleanerTimer.cancel();
        }
    }

    /**
     * Returns if a change has occurred since the last call of this method.<br>
     *     A change constitutes as any modification to the profiles, be it an addition, removal, or modification.
     * @return whether a change has occurred since last call
     */
    public static boolean hasChangeOccurred()
    {
        boolean toReturn = changeOccurred; //We need to reset changeOccurred to false, as the value has been observed.
        changeOccurred = false;
        return toReturn;
    }
}
