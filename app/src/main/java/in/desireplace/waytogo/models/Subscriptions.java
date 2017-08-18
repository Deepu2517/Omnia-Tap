package in.desireplace.waytogo.models;

public class Subscriptions {

    private Boolean OneMonthSubscription;
    private Boolean ThreeMonthSubscription;
    private Boolean SixMonthSubscription;

    public Subscriptions(Boolean oneMonthSubscription, Boolean threeMonthSubscription, Boolean sixMonthSubscription) {
        OneMonthSubscription = oneMonthSubscription;
        ThreeMonthSubscription = threeMonthSubscription;
        SixMonthSubscription = sixMonthSubscription;
    }

    public Boolean getOneMonthSubscription() {
        return OneMonthSubscription;
    }

    public void setOneMonthSubscription(Boolean oneMonthSubscription) {
        OneMonthSubscription = oneMonthSubscription;
    }

    public Boolean getThreeMonthSubscription() {
        return ThreeMonthSubscription;
    }

    public void setThreeMonthSubscription(Boolean threeMonthSubscription) {
        ThreeMonthSubscription = threeMonthSubscription;
    }

    public Boolean getSixMonthSubscription() {
        return SixMonthSubscription;
    }

    public void setSixMonthSubscription(Boolean sixMonthSubscription) {
        SixMonthSubscription = sixMonthSubscription;
    }
}
