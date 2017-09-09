package in.desireplace.waytogo.models;

import com.google.firebase.database.Exclude;

public class YourOrders {

    private String mOrderNumber;
    private String mServiceType;
    private String mFullName;
    private String mMobileNumber;
    private String mEmail;
    private String mHouseNumber;
    private String mLocality;
    private String mLandmark;
    private String mOrderDate;
    private String mOrderTime;

    private String mShirts;
    private String mTrousers;
    private String mOthers;

    private String mCans;

    private String mKey;

    public YourOrders() {
        //Required For Data Serializations.
    }

    public YourOrders(String orderNumber, String serviceType, String fullName, String mobileNumber, String email, String houseNumber, String locality, String landmark, String shirts, String trousers, String others, String orderDate, String orderTime) {
        mOrderNumber = orderNumber;
        mServiceType = serviceType;
        mFullName = fullName;
        mMobileNumber = mobileNumber;
        mEmail = email;
        mHouseNumber = houseNumber;
        mLocality = locality;
        mLandmark = landmark;
        mShirts = shirts;
        mTrousers = trousers;
        mOthers = others;
        mOrderDate = orderDate;
        mOrderTime = orderTime;
    }

    public YourOrders(String orderNumber, String serviceType, String fullName, String mobileNumber, String email, String houseNumber, String locality, String landmark, String cans, String orderDate, String orderTime) {
        mOrderNumber = orderNumber;
        mServiceType = serviceType;
        mFullName = fullName;
        mMobileNumber = mobileNumber;
        mEmail = email;
        mHouseNumber = houseNumber;
        mLocality = locality;
        mLandmark = landmark;
        mCans = cans;
        mOrderDate = orderDate;
        mOrderTime = orderTime;
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

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        this.mFullName = fullName;
    }

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mMobileNumber = mobileNumber;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
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

    public String getShirts() {
        return mShirts;
    }

    public void setShirts(String shirts) {
        this.mShirts = shirts;
    }

    public String getTrousers() {
        return mTrousers;
    }

    public void setTrousers(String trousers) {
        this.mTrousers = trousers;
    }

    public String getOthers() {
        return mOthers;
    }

    public void setOthers(String others) {
        this.mOthers = others;
    }

    public String getCans() {
        return mCans;
    }

    public void setCans(String cans) {
        this.mCans = cans;
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

    @Exclude
    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public void setValues(YourOrders updatedOrder) {
        this.mServiceType = updatedOrder.mServiceType;
        this.mFullName = updatedOrder.mFullName;
        this.mMobileNumber = updatedOrder.mMobileNumber;
        this.mHouseNumber = updatedOrder.mHouseNumber;
        this.mLocality = updatedOrder.mLocality;
        this.mLandmark = updatedOrder.mLandmark;
    }
}
