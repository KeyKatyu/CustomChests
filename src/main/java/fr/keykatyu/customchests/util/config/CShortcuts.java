package fr.keykatyu.customchests.util.config;

import fr.keykatyu.customchests.Main;

public class CShortcuts {
    public static String PREFIX;

    public static void syncConfig() {
        Main.getInstance().saveDefaultConfig();
        PREFIX = Config.getString("messages.prefix");
    }
}
