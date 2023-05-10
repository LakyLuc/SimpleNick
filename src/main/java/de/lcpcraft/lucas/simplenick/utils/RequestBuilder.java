package de.lcpcraft.lucas.simplenick.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.lcpcraft.lucas.simplenick.utils.modrinth.ProjectVersion;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.net.URL;
import java.util.Map;

public class RequestBuilder {
    private final String url;
    private HttpsURLConnection connection;

    public RequestBuilder(String url) {
        this.url = url;
    }

    public HttpsURLConnection execute() {
        try {
            URL myUrl = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) myUrl.openConnection();
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (SimpleNick)");
            con.setRequestMethod("GET");
            connection = con;
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String body() {
        try {
            DataInputStream input = new DataInputStream(connection.getInputStream());
            StringBuilder strBuilder = new StringBuilder();
            for (int c = input.read(); c != -1; c = input.read())
                strBuilder.append((char) c);
            input.close();
            return strBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> resultMap() {
        String body = body();
        if (body != null)
            return new Gson().fromJson(body, new TypeToken<Map<String, Object>>() {
            }.getType());
        return null;
    }

    public ProjectVersion[] projectVersions() {
        String body = body();
        if (body != null)
            return new Gson().fromJson(body, ProjectVersion[].class);
        return null;
    }
}
