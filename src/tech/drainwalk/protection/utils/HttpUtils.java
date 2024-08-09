package tech.drainwalk.protection.utils;

import by.radioegor146.nativeobfuscator.Native;
import com.google.gson.JsonObject;
import me.dreamix.NativeGetter;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.io.IOException;

@Native
public class HttpUtils {

    private static final String query = "http://89.223.127.20:8080/api/";

    public static boolean isHashExists(String hash) {
        final JsonObject json = new JsonObject();
        json.addProperty("hash", hash.substring(0, hash.length() - 32));
        if (!NativeGetter.get0().equals(hash.substring(hash.length() - 32))) {
            return false;
        }

        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            final HttpPost request = new HttpPost(query + "checkhash");
            final StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            final HttpResponse response = httpClient.execute(request);
            final String body = new String(response.getEntity().getContent().readAllBytes());
            if (body.equals("found")) {
                try {
                    httpClient.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            return false;
        }
    }

    public static String getHash(String user_id) {
        final JsonObject json = new JsonObject();
        json.addProperty("hash", CryptUtils.encryptString(user_id));

        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            final HttpPost request = new HttpPost(query + "loginById");
            final StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            final HttpResponse response = httpClient.execute(request);
            final String body = new String(response.getEntity().getContent().readAllBytes());
            try {
                httpClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
           return body;

        } catch (Exception ex) {
            return "ebal";
        }
    }

    public static void ban() {
        final JsonObject json = new JsonObject();
        json.addProperty("hash", CryptUtils.encryptString(NativeGetter.get0()));

        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            final HttpPost request = new HttpPost(query + "ban");
            final StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            httpClient.execute(request);
            try {
                httpClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
