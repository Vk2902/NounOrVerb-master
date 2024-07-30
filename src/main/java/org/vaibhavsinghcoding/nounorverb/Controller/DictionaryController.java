package org.vaibhavsinghcoding.nounorverb.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.vaibhavsinghcoding.nounorverb.Service.DictionaryService;

@RestController
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/api/meaning")
    public ResponseEntity<?> getMeaning(
            @RequestParam String word,
            @RequestParam String partOfSpeech) {
        return dictionaryService.getMeaning(word, partOfSpeech);
    }
}
