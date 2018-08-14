package com.hzgc.collect.expand.util;

public class FtpLogo {
    public static String getLogo() {
        String version = CollectProperties.getFtpVersion();
        return " _____  _             ____                                 \n" +
                "|  ___|| |_  _ __    / ___|   ___  _ __ __   __  ___  _ __ \n" +
                "| |_   | __|| '_ \\   \\___ \\  / _ \\| '__|\\ \\ / / / _ \\| '__|\n" +
                "|  _|  | |_ | |_) |   ___) ||  __/| |    \\ V / |  __/| |   \n" +
                "|_|     \\__|| .__/   |____/  \\___||_|     \\_/   \\___||_|       version " + version+"\n" +
                "            |_|";
    }
}
