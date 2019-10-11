package artisanng.hycode.artisanng.firebase_notification;

public class Data {
    private String mess;
    private String title;
    private String type;
    private  String chatWithImage;
    private  String chatWithId;
private String distKm,sex,email;
    public Data(String title,String mess,String type,String chatWithImage){
        this.title=title;
        this.mess=mess;
        this.type=type;
        this.chatWithImage=chatWithImage;
    }
    public Data(){    }


    public String getChatWithImage() {
        return chatWithImage;
    }

    public void setChatWithImage(String chatWithImage) {
        this.chatWithImage = chatWithImage;
    }
    public void setChatWithId(String chatWithId) {
        this.chatWithId = chatWithId;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Data{" +
                "mess='" + mess + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", chatWithImage='" + chatWithImage + '\'' +
                ", chatWithId='" + chatWithId + '\'' +
                '}';
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDistKm(String distKm) {
        this.distKm=distKm;
    }

    public void setSex(String sex) {
        this.sex=sex;
    }

    public void setEmail(String email) {
        this.email=email;
    }
}
