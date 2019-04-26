package org.nothingugly.uglydeals;

import java.sql.Time;
import java.sql.Timestamp;

public class Deal {


    public Deal(){}

    //Constructor
    public Deal(String name, String partnerID, String termsOfUse, String dealPhoto, Timestamp validFrom, Timestamp validTill) {
        this.name = name;
        this.partnerID = partnerID;
        this.termsOfUse = termsOfUse;
        this.dealImage = dealPhoto;
        //this.validFrom = validFrom;
      //  this.validTill = validTill;
    }

    //Declarng the variables

    public String name, partnerID, termsOfUse, dealImage;
    //public Timestamp validFrom;
    //public Timestamp validTill;

    //Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(String partnerID) {
        this.partnerID = partnerID;
    }

    public String getTermsOfUse() {
        return termsOfUse;
    }

    public void setTermsOfUse(String termsOfUse) {
        this.termsOfUse = termsOfUse;
    }

    public String getDealPhoto() {
        return dealImage;
    }

    public void setDealPhoto(String dealPhoto) {
        this.dealImage = dealPhoto;
    }

    /*public Timestamp getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Timestamp validFrom) {
        this.validFrom = validFrom;
    }

    public Timestamp getValidTill() {
        return validTill;
    }

    public void setValidTill(Timestamp validTill) {
        this.validTill = validTill;
    }*/

}
