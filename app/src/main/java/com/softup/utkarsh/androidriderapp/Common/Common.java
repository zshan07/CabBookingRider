package com.softup.utkarsh.androidriderapp.Common;

import com.google.android.gms.common.api.GoogleApiClient;
import com.softup.utkarsh.androidriderapp.Remote.FCMClient;
import com.softup.utkarsh.androidriderapp.Remote.GoogleMapAPI;
import com.softup.utkarsh.androidriderapp.Remote.IFCMService;
import com.softup.utkarsh.androidriderapp.Remote.IGoogleAPI;

public class Common
{
    public static boolean isDriverFound=false;
    public static String driverId= "";
    public static final String driver_tb1 = "Drivers";
    public static final String user_driver_tb1 = "DriversInformation";
    public static final String user_rider_tb1 = "RidersInformation";
    public static final String pickup_request_tb1 = "PickupRequest";
    public static final String token_tb1 = "Tokens";
    public static final String rate_detail_tb1 = "RateDetails";

    public static final String fcmURL = "https://fcm.googleapis.com/";
    public static final String googleAPIUrl = "https://maps.googleapis.com";

    private static  double base_fare = 2.00;
    private static  double time_rate = 0.00;
    private static  double distance_rate = 1.00;

    public static double getPrice(double km,int min)
    {

        return (base_fare+(time_rate*min)+(distance_rate*km));
    }



    public static IFCMService getFCMService()
    {
        return FCMClient.getClient(fcmURL).create(IFCMService.class);
    }

    public static IGoogleAPI getGoogleService()
    {
        return GoogleMapAPI.getClient(googleAPIUrl).create(IGoogleAPI.class);
    }
}
