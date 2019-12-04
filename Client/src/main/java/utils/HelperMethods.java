package utils;

import models.Id;

import java.util.ArrayList;
import java.util.List;

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

    public static String buildMessage(List<String> list){
        StringBuilder sb = new StringBuilder();
        Boolean inMsg = false;
        for(String item : list){
            if(item.charAt(0) == '\''){
                sb.append(item);
                inMsg = true;
            }else if(inMsg){
                sb.append(" " + item);
            }else if (item.charAt(item.length()-1) == '\''){
                inMsg = false;
                sb.append(item);
                break;
            }
        }
        String output = sb.toString();
        return output.substring(1, output.length()-1);
    }
}
