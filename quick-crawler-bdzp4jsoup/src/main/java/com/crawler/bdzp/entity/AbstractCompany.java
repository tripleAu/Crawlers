package com.crawler.bdzp.entity;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/11/30
 * Time: 11:38
 * Description:
 */
public class AbstractCompany {

    private String companyNature;
    private String companyScale;
    private String companyCategory;
    private String companyAddress;
    private String companyContact;

    public String getCompanyNature() {
        return companyNature;
    }

    public void setCompanyNature(String companyNature) {
        this.companyNature = companyNature;
    }

    public String getCompanyScale() {
        return companyScale;
    }

    public void setCompanyScale(String companyScale) {
        this.companyScale = companyScale;
    }

    public String getCompanyCategory() {
        return companyCategory;
    }

    public void setCompanyCategory(String companyCategory) {
        this.companyCategory = companyCategory;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    @Override
    public String toString() {
        return "AbstractCompany{" +
                "companyNature='" + companyNature + '\'' +
                ", companyScale='" + companyScale + '\'' +
                ", companyCategory='" + companyCategory + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                ", companyContact='" + companyContact + '\'' +
                '}';
    }
}
