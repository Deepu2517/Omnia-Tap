package in.desireplace.waytogo.models;

import com.google.firebase.database.Exclude;

public class YourOrders {

    private String mServiceType;
    private String mFullName;
    private String mMobileNumber;
    private String mPinCode;
    private String mHouseNumber;
    private String mLocality;
    private String mCity;
    private String mState;

    private String mKey;

    public YourOrders() {
        //Required For Data Serializations.
    }

    public YourOrders(String serviceType, String fullName, String mobileNumber, String pinCode, String houseNumber, String locality, String city, String state) {
        mServiceType = serviceType;
        mFullName = fullName;
        mMobileNumber = mobileNumber;
        mPinCode = pinCode;
        mHouseNumber = houseNumber;
        mLocality = locality;
        mCity = city;
        mState = state;
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

    public String getPinCode() {
        return mPinCode;
    }

    public void setPinCode(String pinCode) {
        this.mPinCode = pinCode;
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

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
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
        this.mPinCode = updatedOrder.mPinCode;
        this.mHouseNumber = updatedOrder.mHouseNumber;
        this.mLocality = updatedOrder.mLocality;
        this.mCity = updatedOrder.mCity;
        this.mState = updatedOrder.mState;
    }
}
