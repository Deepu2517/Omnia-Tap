package in.desireplace.waytogo.models;

public class SupportTicket {

    private String mEmail;
    private String mAboutProblem;
    private String mUid;
    private String mName;
    private String mOrderNumber;
    private String mServiceType;
    private String mMobileNumber;
    private String mHouseNumber;
    private String mLocality;
    private String mLandmark;
    private String mOrderDate;
    private String mOrderTime;

    public SupportTicket() {
        //Required For Data Serializations.
    }

    public SupportTicket(String email, String aboutProblem, String uid, String name, String orderNumber, String serviceType, String mobileNumber, String houseNumber, String locality, String landmark, String orderDate, String orderTime) {
        mEmail = email;
        mAboutProblem = aboutProblem;
        mUid = uid;
        mName = name;
        mOrderNumber = orderNumber;
        mServiceType = serviceType;
        mMobileNumber = mobileNumber;
        mHouseNumber = houseNumber;
        mLocality = locality;
        mLandmark = landmark;
        mOrderDate = orderDate;
        mOrderTime = orderTime;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getAboutProblem() {
        return mAboutProblem;
    }

    public void setAboutProblem(String aboutProblem) {
        this.mAboutProblem = aboutProblem;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        this.mUid = uid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getOrderNumber() {
        return mOrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.mOrderNumber = orderNumber;
    }

    public String getServiceType() {
        return mServiceType;
    }

    public void setServiceType(String serviceType) {
        this.mServiceType = serviceType;
    }

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mMobileNumber = mobileNumber;
    }

    public String getHouseNumber() {
        return mHouseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.mHouseNumber = houseNumber;
    }

    public String getLocality() {
        return mLocality;
    }

    public void setLocality(String locality) {
        this.mLocality = locality;
    }

    public String getLandmark() {
        return mLandmark;
    }

    public void setLandmark(String landmark) {
        this.mLandmark = landmark;
    }

    public String getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(String orderDate) {
        this.mOrderDate = orderDate;
    }

    public String getOrderTime() {
        return mOrderTime;
    }

    public void setOrderTime(String orderTime) {
        this.mOrderTime = orderTime;
    }
}
