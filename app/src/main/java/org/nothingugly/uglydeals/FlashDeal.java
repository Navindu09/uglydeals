package org.nothingugly.uglydeals;

import java.util.Date;

public class FlashDeal {
    private Boolean active;
    private String dealPhoto,description, id, name, termsOfUse, partnerName;
    private Date validFrom, validTill;

    public FlashDeal(){}

    public FlashDeal(Boolean active, String dealPhoto, String description, String id, String name, String termsOfUse, Date ValidFrom, Date ValidTill){
        this.active = active;
        this.dealPhoto = dealPhoto;
        this.description = description;
        this.id = id;
        this.name = name;
        this.termsOfUse = termsOfUse;
        this.validFrom = validFrom;
        this.validTill = validTill;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDealPhoto() {
        return dealPhoto;
    }

    public void setDealPhoto(String dealPhoto) {
        this.dealPhoto = dealPhoto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTermsOfUse() {
        return termsOfUse;
    }

    public void setTermsOfUse(String termsOfUse) {
        this.termsOfUse = termsOfUse;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTill() {
        return validTill;
    }

    public void setValidTill(Date validTill) {
        this.validTill = validTill;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }
}
