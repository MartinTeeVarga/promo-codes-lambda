package com.czequered.promocodes.lambda;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Created by Martin on 22/01/2017.
 */
public class CodeRepository {
    private static final String BAD_REQUEST = "400";
    private static final String NOT_FOUND = "404";
    private static final String GONE = "410";


    public Code find(Code codeRequest) {
        if (codeRequest == null || codeRequest.getGame() == null || codeRequest.getCode() == null) {
            throw new RuntimeException(BAD_REQUEST);
        }

        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2)); // setting from ~/.aws/config is ignored
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        Code loaded = mapper.load(codeRequest);

        if (loaded == null) {
            throw new RuntimeException(NOT_FOUND);
        }

        Instant now = Instant.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;

        TemporalAccessor taFrom = timeFormatter.parse(loaded.getFrom());
        Instant from = Instant.from(taFrom);
        if (now.compareTo(from) < 0) {
            throw new RuntimeException(NOT_FOUND);
        }

        TemporalAccessor taTo = timeFormatter.parse(loaded.getTo());
        Instant to = Instant.from(taTo);
        if (now.compareTo(to) > 0) {
            throw new RuntimeException(GONE);
        }

        return loaded;
    }
}
