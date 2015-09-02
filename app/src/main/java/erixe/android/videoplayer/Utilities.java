package erixe.android.videoplayer;

import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by emad on 9/1/2015.
 */
public class Utilities {

    public static String encodeStringWithUTF8(String vorodi) throws UnsupportedEncodingException {
        byte[] tmp = vorodi.getBytes(Charset.forName("UTF-8"));
        String tmp2 = new String(tmp, Charset.forName("UTF-8"));
        String result = new String(tmp2.getBytes(), "ISO-8859-1");
        return result;
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "", result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;
    }

    public static InputStream getWebServiseInputStream(String username, String password, String url) throws JSONException, IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();

        final String basicAuth = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
        connection.setRequestProperty ("Authorization", basicAuth);
        connection.setInstanceFollowRedirects(true);

        InputStream inputStream = connection.getInputStream();

        return inputStream;
    }

}
