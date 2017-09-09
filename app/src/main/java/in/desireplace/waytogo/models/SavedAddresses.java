package in.desireplace.waytogo.models;


import com.google.firebase.database.Exclude;

public class SavedAddresses {

    private String mFullName;
    private String mMobileNumber;
    private String mEmail;
    private String mHouseNumber;
    private String mLocality;
    private String mLandmark;

    private String mKey;

    public SavedAddresses() {
        //Required For Data Serializations.
    }

    public SavedAddresses(String fullName, String mobileNumber, String email, String houseNumber, String locality, String landmark) {
        mFullName = fullName;
        mMobileNumber = mobileNumber;
        mEmail = email;
        mHouseNumber = houseNumber;
        mLocality = locality;
        mLandmark = landmark;
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
        this.mEmail = updatedAddress.mEmail;
        this.mHouseNumber = updatedAddress.mHouseNumber;
        this.mLocality = updatedAddress.mLocality;
        this.mLandmark = updatedAddress.mLandmark;
    }
}
