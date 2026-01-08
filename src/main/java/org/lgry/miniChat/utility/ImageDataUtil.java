package org.lgry.miniChat.utility;

public class ImageDataUtil {
    public static String getPlayerFaceURL(String userName) {
        return "https://crafthead.net/helm/%USERNAME%".replace("%USERNAME%", userName);
    }
}
