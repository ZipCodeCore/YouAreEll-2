package utils;

import models.Id;

import java.util.ArrayList;

public class HelperMethods {
    public static String getId(ArrayList<Id> ids, String github){
        String userid = "";

        for(Id id : ids){
            if(id.getGithub().equals(github)){
                userid = id.getUserid();
                break;
            }
        }
        return userid;
    }
}
