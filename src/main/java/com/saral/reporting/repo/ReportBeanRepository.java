package com.saral.reporting.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saral.reporting.model.ReportBean;

public interface ReportBeanRepository extends JpaRepository<ReportBean, Long> {

}
