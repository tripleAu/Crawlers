package com.crawler.bdzp.entity;

import javax.persistence.*;

@Entity
@Table(name = "baiduzhaopin_company_detail")
public class BaiduzhaopinCompanyDetail extends AbstractCompany {

  private long id;
  private String name;

  private String urlId;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrlId() {
    return urlId;
  }

  public void setUrlId(String urlId) {
    this.urlId = urlId;
  }

  @Override
  public String toString() {
    return "BaiduzhaopinCompanyDetail{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", urlId='" + urlId + '\'' +
            '}' + super.toString();
  }
}
