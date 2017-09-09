package in.desireplace.waytogo.models;

public class Notifications {

    private String Heading;
    private String Body;

    public Notifications() {
        //Required For Data Serializations.
    }

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String heading) {
        Heading = heading;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }
}
