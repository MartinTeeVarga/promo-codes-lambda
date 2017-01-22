package com.czequered.promocodes.lambda;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Martin on 22/01/2017.
 */
public class CodeRepositoryTest {
//    AmazonDynamoDB dynamoDB;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void handleRequest() throws Exception {
        CodeRepository cr = new CodeRepository();
        Code codeRequest = new Code();
        codeRequest.setGame("test");
        codeRequest.setCode("PUB1");
        Code code = cr.find(codeRequest);
        assertEquals("test", code.getGame());
        assertEquals("PUB1", code.getCode());
        assertEquals(true, code.getPub());
    }
//
//    @Test
//    public void blahTest() throws Exception {
//
//        final String[] localArgs = {"-inMemory"};
//        DynamoDBProxyServer server = null;
//        try {
//            server = ServerRunner.createServerFromCommandLineArgs(localArgs);
//            server.start();
//            AmazonDynamoDBClient dynamodb = new AmazonDynamoDBClient();
//            dynamodb.setEndpoint("http://localhost:8000");
//
//            // use the DynamoDB API over HTTP
//            listTables(dynamodb.listTables(), "DynamoDB Local over HTTP");
//        } finally {
//            // Stop the DynamoDB Local endpoint
//            if (server != null) {
//                server.stop();
//            }
//        }
//    }
//
//    public static void listTables(ListTablesResult result, String method) {
//        System.out.println("found " + Integer.toString(result.getTableNames().size()) + " tables with " + method);
//        for (String table : result.getTableNames()) {
//            System.out.println(table);
//        }
//    }

}