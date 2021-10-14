package mainpackage.dto;

public class EndPoints {
    public static final String PATH_TO_PROPERTIES ="src/main/resources/HWAplication.properties";
    public static final String URL_UPLOAD ="https://api.imgur.com/3/upload";
    public static final String URL_DELETEHASH = "https://api.imgur.com/3/image/{imageDeleteHash}";
    public static final String URL_ACCAUNT = "https://api.imgur.com/3/account/{username}";
    public static final String PATH_TO_IMAGE = "src/main/resources/image.jpeg";
    public static final String URL_UPLOADIMAGEID = "https://api.imgur.com/3/image/{uploadImageId}";
    public static final String URL_FAVORITED = "https://api.imgur.com/3/image/{uploadedImageId}/favorite";
}
