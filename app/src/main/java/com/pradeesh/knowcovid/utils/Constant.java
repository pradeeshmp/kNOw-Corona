package com.pradeesh.knowcovid.utils;

public class Constant {

    public static final String SUCCESS = "Success";
    public static final String FAILURE = "Failure";
    public static final String ERROR = "Error";

    public static final String COVID_BASE_URL = "https://corona.lmao.ninja/v2/";

    public static final String RSS_BASE_URL = "https://newsapi.org/v2/";
    public static final String RSSAPIKEY = "affb243fff994645a1df4477dc00592a"; // create own api key in https://newsapi.org/
    public static final String RSSTOPIC = "medical-news-today";


    //Intent - RSS detailed view message keys
    public static final String RSS_URLKEY = "rssURL";
    public static final String RSS_HEADLINEKEY = "rssHeadline";
    public static final boolean RSS_VEHMODE = false;

    //Map View URL
    public static final String MAPURL = "https://www.trackcorona.live/map"; // https://www.bing.com/covid

    //Muticolor text
    public static final String SPLASH_APPNAME = "k<font color=#FF0000>NO</font>w Corona";

    //Vehicle Parameters
    public static final int VEH_SPEED_LIMIT = 10;
    public static final String VEH_IGNITION_STATUS_ON = "IGNITION_STATE_ON";
    public static final String VEH_IGNITION_STATUS_OFF = "IGNITION_STATE_OFF";


}
