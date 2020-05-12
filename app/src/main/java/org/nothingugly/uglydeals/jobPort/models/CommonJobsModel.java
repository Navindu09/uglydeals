package org.nothingugly.uglydeals.jobPort.models;

import java.util.Date;

public class CommonJobsModel {
    String companyId;
    private String description;
    private String educationExperience;
    private Date endDate;
    private String experienceRequirements;
    private boolean paid;
    private boolean remote;
    private int reward;
    private String skillsExperience;
    private Date startDate;
    private String title;
    private String location;
    private String type;
    private String id;
    private boolean saved;
    private String level;
    private String dealPhoto;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDealPhoto() {
        return dealPhoto;
    }

    public void setDealPhoto(String dealPhoto) {
        this.dealPhoto = dealPhoto;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public boolean getSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEducationRequirement() {
        return educationExperience;
    }

    public void setEducationRequirement(String educationRequirement) {
        this.educationExperience = educationRequirement;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getExperience() {
        return experienceRequirements;
    }

    public void setExperience(String experience) {
        this.experienceRequirements = experience;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public String getSkills() {
        return skillsExperience;
    }

    public void setSkills(String skills) {
        this.skillsExperience = skills;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
