package com.equinox.lyra2;

import com.equinox.equinox_essentials.Essentials;

public class Lyra2 {

    //Gets Lyra version.
    public static String getLyra2Version() {
        return Config.version;
    }

    //A goofy little method that prints the ENTIRE list of employees at Equinox Electronic
    public static void printCredits() {
        Essentials.logger.createProgressBar("Loading list of people at Equinox...",100);

        int x = 0;
        while (x < 100) {
            x++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Essentials.logger.updateProgressBar(x,100);
        }
        System.out.println("Haha, just kidding. Equinox Electronic for now is just me, AnotherCastaway. Hope you like it. ✨\nGitHub: CastawayMakesThings");
    }

    //This method will return the README so that people don't have to go online to get it
    public static void getREADME (String filepath) {
        Essentials.logger.logWarning("NO README YET!");
        //TODO Add commentation to the new utility classes
    }

}
