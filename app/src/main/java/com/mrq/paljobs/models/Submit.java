package com.mrq.paljobs.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Submit implements Serializable {

    private String id;

    private String companyImage;
    private String companyName;
    private String companyId;

    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerCv;
    private ArrayList<String> customerSkills;

    private String proposalId;
    private String proposal;

    private String title;
    private String content;
    private ArrayList<String> skills;
    private String requirement;

    private String time;

    private boolean saved;

    public Submit() {
    }

    @NotNull
    @Override
    public String toString() {
        return "Submit{" +
                "id='" + id + '\'' +
                ", companyImage='" + companyImage + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyId='" + companyId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", customerCv='" + customerCv + '\'' +
                ", customerSkills='" + customerSkills + '\'' +
                ", proposalId='" + proposalId + '\'' +
                ", proposal='" + proposal + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", skills=" + skills +
                ", requirement='" + requirement + '\'' +
                ", time='" + time + '\'' +
                ", saved=" + saved +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyImage() {
        return companyImage;
    }

    public void setCompanyImage(String companyImage) {
        this.companyImage = companyImage;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerCv() {
        return customerCv;
    }

    public void setCustomerCv(String customerCv) {
        this.customerCv = customerCv;
    }

    public ArrayList<String> getCustomerSkills() {
        return customerSkills;
    }

    public void setCustomerSkills(ArrayList<String> customerSkills) {
        this.customerSkills = customerSkills;
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
