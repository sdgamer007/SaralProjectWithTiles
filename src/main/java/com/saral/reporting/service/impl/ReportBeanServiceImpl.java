package com.saral.reporting.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saral.reporting.model.ReportBean;
import com.saral.reporting.repo.ReportBeanRepository;
import com.saral.reporting.service.ReportBeanService;

@Service
public class ReportBeanServiceImpl implements ReportBeanService {

	@Autowired
	ReportBeanRepository reportBeanRepository;

	@Override
	public ReportBean save(ReportBean reportBean) {
		// TODO Auto-generated method stub

		return reportBeanRepository.save(reportBean);
	}

}
