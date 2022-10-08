package com.ex.springdemo;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import org.elasticsearch.action.index.IndexRequest;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;

import com.ex.springdemo.wikidump.Dump;
import com.ex.springdemo.wikidump.Parser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ElasticSearchQuery {

    private final String indexName = "wiki";
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private Parser parser;

    public String createOrUpdateDocument(Dump object) throws IOException {

        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(object.getField2())
                .document(object)
        );
        if (response.result().name().equals("Created")) {
            return new StringBuilder("Document has been successfully created.").toString();
        } else if (response.result().name().equals("Updated")) {
            return new StringBuilder("Document has been successfully updated.").toString();
        }
        return new StringBuilder("Error while performing the operation.").toString();
    }

    public void bulkInsert(){

        List<Dump> wikidumpList = parser.fileParser();
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (Dump obj : wikidumpList) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(obj.getField2())
                            .document(obj)
                    )
            );
        }
        BulkResponse result = null;
        try {
            result = elasticsearchClient.bulk(br.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

// Log errors, if any
        if (result.errors()) {
            System.out.println("Bulk had errors");
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    System.out.println(item.error().reason());
                }
            }
        }
    }

    public List<Dump> searchAllDocuments() {

        SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName));
        SearchResponse<Dump> searchResponse = null;
        try {
            searchResponse = elasticsearchClient.search(searchRequest, Dump.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Hit<Dump>> hits = searchResponse.hits().hits();
        List<Dump> searchedList = new ArrayList<>();
        for (Hit<Dump> object : hits) {

            System.out.print((object.source()));
            searchedList.add(object.source());

        }
        return searchedList;
    }

    public String deleteDocumentById(String documentId) {
        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(documentId));

        DeleteResponse deleteResponse = null;
        try {
            deleteResponse = elasticsearchClient.delete(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return new StringBuilder("document with id " + deleteResponse.id() + " has been deleted.").toString();
        }
        System.out.println("document not found");
        return new StringBuilder("document with id " + deleteResponse.id() + " does not exist.").toString();

    }

    public void searchByKeywordTitle() {
        String field = "field2";
        String textToBeSearched = "1835";
        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.id(indexName);
        SearchRequest request = SearchRequest.of(s -> s.index(indexName).query(q -> q
                .match(t -> t
                        .field(field)
                        .query(textToBeSearched))));
        SearchResponse response = null;
        try {
            response = elasticsearchClient.search(request, Dump.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            System.out.println("There are " + total.value() + " results");
        } else {
            System.out.println("There are more than " + total.value() + " results");
        }

        List<Hit<Dump>> hits = response.hits().hits();
        for (Hit<Dump> hit : hits) {
            Dump obj = hit.source();
            System.out.println("Found found  " + obj + ", score " + hit.score());
        }
    }
}
