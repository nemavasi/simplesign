package ru.hlynov.oit.simplesign.tools;

//import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import static ru.hlynov.oit.simplesign.customfields.SimpleSignature.DB_SEP;

public class JsonConvertor {

    private static final Logger log = LoggerFactory.getLogger(JsonConvertor.class);


    // возвращает json из параметров задачи
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


//    admin
//    ###
//    03.04.2019 16:56:33
//    ###
//    {
//        "username":"admin",
//        "summary":"cee93ecb3ea0ebca318fcc89e32f5029ed06a6c3",
//        "description":"da39a3ee5e6b4b0d3255bfef95601890afd80709",
//        "attachments":   [
//                                {
//                                    "name":"DOC-2487 (3).docx", "hash":"6c83aec137027e3c39a82bdc6da49729ae1703fa","id":"10000"
//                                },
//                                {
//                                    "name":"№0.0 от 25.12.2018 - проверка вложения.docx","hash":"11b773f559175021ddf610c18f4c4eda866b935f","id":"10002"},
//                                {
//                                    "name":"123.TXT","hash":"b689f1011e030ebf5a177637e4c225c171d30208","id":"10001"
//                                }
//                        ]
//    }

    //  json строку из параметров задачи
    public static String getAttachmentNames(String signValue) {

        JsonElement jsonElement = new JsonParser().parse(signValue);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("attachments");

        Gson gson = new Gson();

        return gson.toJson(jsonArray);
    }

    //  возвращает значение контрольной суммы объекта из подписи
    //  objName - имя объекта
    //  attachId - идентификатор вложения
    //  signValue - цифровая подпись
    public static String getStoredSignatureByName(String objName, String attachId, String signValue) {
        JsonElement jsonElement = new JsonParser().parse(signValue);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (objName.equals("summary")) {
            return jsonObject.get("summary").getAsString();
        }

        if (objName.equals("description")) {
            return jsonObject.get("description").getAsString();
        }

        if (objName.equals("attachment")) {

            JsonArray jsonArray = jsonObject.getAsJsonArray("attachments");
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject oneAttach = jsonArray.get(i).getAsJsonObject();
                if(attachId.equals(oneAttach.get("id").getAsString())) {
                    return oneAttach.get("hash").getAsString();
                }
            }
        }
        log.warn("for object " + objName + " signature not found");
        return "";
    }


}
