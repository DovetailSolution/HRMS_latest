package com.tcs.hr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class OpenAIUtil {

    // Replace this with your actual Together.ai API key
    private static final String API_KEY = "d3874821929425538e2133e2b05fe4ed0c9754b1b80406af0a59f3cfa41ac3e0";

    public static String generateEmail(String prompt) throws IOException {
        URL url = new URL("https://api.together.xyz/v1/chat/completions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY); 
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject requestBody = new JSONObject()
            .put("model", "deepseek-ai/DeepSeek-V3")
            .put("messages", new JSONArray()
                .put(new JSONObject()
                    .put("role", "user")
                    .put("content", prompt)));

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.toString().getBytes());
            os.flush();
        }

        StringBuilder responseStr = new StringBuilder();
        int responseCode = conn.getResponseCode();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                responseStr.append(line);
            }
        }

        if (responseCode != 200) {
            throw new IOException("Together.ai API error: " + responseStr);
        }

        // ✅ Extract content from the response
        JSONObject json = new JSONObject(responseStr.toString());
        return json.getJSONArray("choices")
                   .getJSONObject(0)
                   .getJSONObject("message")
                   .getString("content");
    }
}
