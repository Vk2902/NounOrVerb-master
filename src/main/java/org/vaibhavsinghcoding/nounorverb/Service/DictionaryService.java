package org.vaibhavsinghcoding.nounorverb.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class DictionaryService {

    private final RestTemplate restTemplate = new RestTemplate();
	
    @Value("${dictionary.api.url}")
    private String apiUrl;

    public ResponseEntity<List<String>> getMeaning(String word, String partOfSpeech) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl + word, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                JSONArray jsonResponse = new JSONArray(response.getBody());
                List<String> result = new ArrayList<>();

                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject entry = jsonResponse.getJSONObject(i);
                    JSONArray meanings = entry.getJSONArray("meanings");

                    for (int j = 0; j < meanings.length(); j++) {
                        JSONObject meaning = meanings.getJSONObject(j);
                        if (meaning.getString("partOfSpeech").equalsIgnoreCase(partOfSpeech)) {
                            JSONArray definitions = meaning.getJSONArray("definitions");
                            for (int k = 0; k < definitions.length(); k++) {
                                result.add(definitions.getJSONObject(k).getString("definition"));
                            }
                        }
                    }
                }

                if (result.isEmpty()) {
                    return ResponseEntity.status(404).body(Collections.singletonList("No meanings found for the specified part of speech."));
                }

                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(Collections.singletonList("Error retrieving meaning."));
            }
        } catch (RestClientException e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body(Collections.singletonList(e.getMessage()));
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body(Collections.singletonList(e.getMessage()));
        }
    }
}