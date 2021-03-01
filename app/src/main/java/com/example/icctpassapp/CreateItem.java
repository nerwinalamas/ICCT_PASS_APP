package com.example.icctpassapp;



public class CreateItem {
    String className;
    String subjectCode;
    String section;

    public CreateItem(String className, String subjectCode, String section) {
        this.className = className;
        this.subjectCode = subjectCode;
        this.section = section;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

}
