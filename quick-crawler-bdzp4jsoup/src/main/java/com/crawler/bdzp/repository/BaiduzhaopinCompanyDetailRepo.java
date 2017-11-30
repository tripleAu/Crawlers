package com.crawler.bdzp.repository;

import com.crawler.bdzp.entity.BaiduzhaopinCompanyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/11/20
 * Time: 12:05
 * Description:
 */
public interface BaiduzhaopinCompanyDetailRepo extends JpaRepository<BaiduzhaopinCompanyDetail,Integer> {

    @Query(value = "SELECT count(*) FROM BaiduzhaopinCompanyDetail WHERE url_id = :urlId")
    int findCountByUrlId(@Param("urlId") String urlId);

}
