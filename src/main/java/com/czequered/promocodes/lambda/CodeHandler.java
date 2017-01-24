package com.czequered.promocodes.lambda;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CodeHandler implements RequestHandler<Code, Code> {

    private DynamoDBMapper mapper;

    public CodeHandler() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));
        this.mapper = new DynamoDBMapper(client);
    }

    public CodeHandler(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Code handleRequest(Code codeRequest, Context context) {
        CodeRepository cr = new CodeRepository(mapper);
        return cr.find(codeRequest);
    }
}
