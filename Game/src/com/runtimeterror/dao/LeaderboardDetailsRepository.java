/**
 *
 */
package com.runtimeterror.dao;

import java.util.*;
import java.util.stream.Collectors;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.runtimeterror.model.Leaderboard;

public class LeaderboardDetailsRepository {

    public LeaderboardDetailsRepository() {
        System.setProperty("aws.accessKeyId", "AKIA3RMLYMV3P4ZV7423");
        System.setProperty("aws.secretKey", "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");
    }

    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_WEST_1).build();
    DynamoDB dynamoDb = new DynamoDB(client);

    public List<Leaderboard> getTopLeaderboard(int size) {
        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride
                        .withTableNameReplacement("runtimeterror_leaderboard")).build();
        DynamoDBMapper mapper = new DynamoDBMapper(client, mapperConfig);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Leaderboard> scanResult = new ArrayList<>(mapper.scan(Leaderboard.class, scanExpression));
        scanResult.sort(Comparator.comparingLong(Leaderboard::getGameTime).reversed());
        List<Leaderboard> result = scanResult.stream()
                .filter(user -> user.getDifficulty().equals("hard"))
                .collect(Collectors.toList());
        if(result.size() > size) {
            result = result.stream().limit(size).collect(Collectors.toList());
        }
        return result;
    }

    public Leaderboard getLeaderboard(String key) {
        Table table = dynamoDb.getTable("runtimeterror_leaderboard");
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("escapeId", key);
        try {
            System.out.println("Attempting to read item");
            Item outcome = table.getItem(spec);
            if (Objects.nonNull(outcome)) {
                Leaderboard leaderboard = new Leaderboard();
                leaderboard.setUserName(outcome.get("userName").toString());
                leaderboard.setEscapeId(outcome.get("escapeId").toString());
                leaderboard.setDifficulty(outcome.get("difficulty").toString());
                leaderboard.setGameTime(Integer.parseInt(outcome.get("gameTime").toString()));
                leaderboard.setRuntime(Integer.parseInt(outcome.get("runtime").toString()));
                return leaderboard;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addLeaderboard(Leaderboard leaderboard) {
        boolean result = false;
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        try {
            mapper.save(leaderboard);
            result = true;
        } catch (DynamoDBMappingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
