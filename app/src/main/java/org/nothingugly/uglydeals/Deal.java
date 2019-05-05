package org.nothingugly.uglydeals;

import java.sql.Date;

public class Deal {


    public Deal(){}

    //Constructor
    public Deal(String id, String name, String partnerID, String termsOfUse, String dealPhoto, String description, Date validFrom, Date validTill,Boolean mainAd) {
        this.name = name;
        this.partnerID = partnerID;
        this.termsOfUse = termsOfUse;
        this.dealImage = dealPhoto;
        this.id = id;
        //this.validFrom = validFrom;
        //this.validTill = validTill;
        this.description = description;
    }

//Declarng the variables

    public String id, name, partnerID, termsOfUse, dealImage, description;
    public Boolean mainAd;
   // public Date validFrom;
    //public Date validTill;

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


    public Boolean getMainAd() {
        return mainAd;
    }

    public void setMainAd(Boolean mainAd) {
        this.mainAd = mainAd;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


 /*  public String getValidFrom() {
        return  validFrom.toString();
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTill() {
        return validTill.toString();

    public void setValidTill(Date validTill) {
    this.validTill = validTill;
    }
    }*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
