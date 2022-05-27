package com.mrq.paljobs.models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Proposal {

    private String id;
    private String companyImage;
    private String companyName;
    private String companyId;
    private String title;
    private String content;
    private ArrayList<String> skills;
    private String requirement;
    private String time;
    private boolean saved;
    private boolean submit;

    public Proposal() {
    }

    @NotNull
    @Override
    public String toString() {
        return "Proposal{" +
                "id='" + id + '\'' +
                ", companyImage='" + companyImage + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyId='" + companyId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", skills=" + skills +
                ", requirement='" + requirement + '\'' +
                ", time='" + time + '\'' +
                ", saved='" + saved + '\'' +
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

    public boolean getSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean getSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }
}
