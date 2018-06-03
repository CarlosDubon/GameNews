package com.bonusteam.gamenews.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SecurityToken implements Parcelable{
    @SerializedName("token")
    private String tokenSecurity;

    protected SecurityToken(Parcel in) {
        tokenSecurity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tokenSecurity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SecurityToken> CREATOR = new Creator<SecurityToken>() {
        @Override
        public SecurityToken createFromParcel(Parcel in) {
            return new SecurityToken(in);
        }

        @Override
        public SecurityToken[] newArray(int size) {
            return new SecurityToken[size];
        }
    };

    public SecurityToken(String tokenSecurity) {
        this.tokenSecurity = tokenSecurity;
    }

    public String getTokenSecurity() {
        return tokenSecurity;
    }

    public void setTokenSecurity(String tokenSecurity) {
        this.tokenSecurity = tokenSecurity;
    }
}
