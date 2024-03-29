package com.mrq.paljobs.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Favorite implements Serializable {

    private String id;
    private String companyImage;
    private String companyName;
    private String companyId;

    private String customerId;
    private String proposalId;

    private String title;
    private String content;
    private ArrayList<String> skills;
    private String requirement;
    private String time;

    private boolean submit;

    public Favorite() {
    }

    @NotNull
    @Override
    public String toString() {
        return "Proposal{" +
                "id='" + id + '\'' +
                ", companyImage='" + companyImage + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyId='" + companyId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", proposalId='" + proposalId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", skills=" + skills +
                ", requirement='" + requirement + '\'' +
                ", time='" + time + '\'' +
                ", submit='" + submit + '\'' +
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId;
    }

    public boolean getSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }
}
