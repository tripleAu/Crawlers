package com.crawler.bdzp.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "appoint_company_detail")
public class AppointCompanyDetail extends AbstractCompany {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;
  private Date addtime;

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

  public Date getAddtime() {
    return addtime;
  }

  public void setAddtime(Date addtime) {
    this.addtime = addtime;
  }

  @Override
  public String toString() {
    return "AppointCompanyDetail{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", addtime=" + addtime +
            '}' + super.toString();
  }
}
