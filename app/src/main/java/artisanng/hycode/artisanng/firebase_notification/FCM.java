package artisanng.hycode.artisanng.firebase_notification;

public class FCM {
    private String to;
    private Data data;

    public FCM(String to, Data data) {
        this.to = to;
        this.data = data;
    }
    public FCM() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FCM{" +
                "to='" + to + '\'' +
                ", data=" + data +
                '}';
    }
}
