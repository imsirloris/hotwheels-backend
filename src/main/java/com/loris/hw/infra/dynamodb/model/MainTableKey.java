package com.loris.hw.infra.dynamodb.model;

public record MainTableKey(String pk, String sk) {
    public static MainTableKey userMeta(String userId) {
        return new MainTableKey("USER#" + userId, "META#" + userId);
    }
    public static MainTableKey userCar(String userId, String carId) {
        return new MainTableKey("USER#" + userId, "CAR#" + carId);
    }
}
