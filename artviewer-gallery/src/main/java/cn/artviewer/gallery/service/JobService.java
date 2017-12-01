package cn.artviewer.gallery.service;

import com.github.pagehelper.Page;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;

import cn.artviewer.gallery.model.JobModel;

public interface JobService {

	void saveJob(JobModel jobModel);

	void doJob(JobDataMap jobDataMap);

	void deleteJob(Long id) throws SchedulerException;

	Page<Job> findByPage(int pageNo, int pageSize);
}
