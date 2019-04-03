package ru.hlynov.oit.simplesign.tools;

//import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonConvertor {

    public static String getJsonFromParams(String username, String summaryhash, String descriptionhash,  Map<String, Map<String,String>> attacmentshash) {

//        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//        Date date = new Date();

        Gson gson = new Gson();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("summary", summaryhash);
        jsonObject.addProperty("description", descriptionhash);
//        jsonObject.addProperty("date", dateFormat.format(date));

        JsonArray jsonArrayAttachHash = new JsonArray();

        String filename = "";
        String filehash = "";


        for (Map.Entry entryFile : attacmentshash.entrySet()) {
            JsonObject attachHash = new JsonObject();

            Map<String,String> fileProps = (Map<String,String>)entryFile.getValue();

            filename = fileProps.get("name");
            filehash = fileProps.get("hash");

            attachHash.addProperty("name", filename);
            attachHash.addProperty("hash", filehash);
            attachHash.addProperty("id", (String)entryFile.getKey());

            jsonArrayAttachHash.add(attachHash);


        }

        jsonObject.add("attachments", jsonArrayAttachHash);


        return gson.toJson(jsonObject);
    }

}
