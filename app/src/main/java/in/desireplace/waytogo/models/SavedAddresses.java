package in.desireplace.waytogo.models;


import com.google.firebase.database.Exclude;

public class SavedAddresses {

    private String mFullName;
    private String mMobileNumber;
    private String mPinCode;
    private String mHouseNumber;
    private String mLocality;
    private String mCity;
    private String mState;

    private String mKey;

    public SavedAddresses() {
        //Required For Data Serializations.
    }

    public SavedAddresses(String fullName, String mobileNumber, String pinCode, String houseNumber, String locality, String city, String state) {
        mFullName = fullName;
        mMobileNumber = mobileNumber;
        mPinCode = pinCode;
        mHouseNumber = houseNumber;
        mLocality = locality;
        mCity = city;
        mState = state;
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

    public void setValues(SavedAddresses updatedAddress) {
        this.mFullName = updatedAddress.mFullName;
        this.mMobileNumber = updatedAddress.mMobileNumber;
        this.mPinCode = updatedAddress.mPinCode;
        this.mHouseNumber = updatedAddress.mHouseNumber;
        this.mLocality = updatedAddress.mLocality;
        this.mCity = updatedAddress.mCity;
        this.mState = updatedAddress.mState;
    }
}
