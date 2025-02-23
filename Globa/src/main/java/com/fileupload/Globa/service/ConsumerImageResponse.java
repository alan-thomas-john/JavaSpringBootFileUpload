package com.fileupload.Globa.service;

public class ConsumerImageResponse {
    //private Long consumerId;
    private String consumerName;
    private Long imgId;
    private String name;
    private String type;
    private String imgFile;
    private byte[] imageData;

    public ConsumerImageResponse( String consumerName, Long imgId, String name, String type, String imgFile, byte[] imageData) {
        //this.consumerId = consumerId;
        this.consumerName = consumerName;
        this.imgId = imgId;
        this.name = name;
        this.type = type;
        this.imgFile = imgFile;
        this.imageData = imageData;
    }

    // Getters
   // public Long getConsumerId() { return consumerId; }
    public String getConsumerName() { return consumerName; }
    public Long getImgId() { return imgId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getImgFile() { return imgFile; }
    public byte[] getImageData() { return imageData; }
}
