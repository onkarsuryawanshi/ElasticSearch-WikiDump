package com.ex.springdemo;

import com.ex.springdemo.wikidump.Dump;
import com.ex.springdemo.wikidump.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ElasticSearchController {
    @Autowired
    private ElasticSearchQuery elasticSearchQuery;
    @Autowired
    private Parser parser;

    @PostMapping("/createOrUpdateDocument")
    public String createOrUpdateDocument() throws IOException {
        List<Dump> wikidumpList = parser.fileParser();
        for (Dump object : wikidumpList) {
            String response = elasticSearchQuery.createOrUpdateDocument(object);
        }
        return "created";
    }

    //dumping the data using bulk insert
    @PostMapping("/bulkInsert")
    public String addMultipleDocuments() {
        elasticSearchQuery.bulkInsert();
        return "added data";
    }


    //get all the document presented on index
    @GetMapping("/getAllDocument")
    public ResponseEntity<Object> searchAllDocument() throws IOException {
        List<Dump> obj = elasticSearchQuery.searchAllDocuments();
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @DeleteMapping("/deleteDocument")
    public ResponseEntity<Object> deleteByStudentId(@RequestParam String documentId) throws IOException {
        String response = elasticSearchQuery.deleteDocumentById(documentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //searching in field2 -- keyword field
    @GetMapping("/searchQueryKeyword")
    public void searchQueryKeyword() throws IOException {
        elasticSearchQuery.searchByKeywordTitle();
    }
}
