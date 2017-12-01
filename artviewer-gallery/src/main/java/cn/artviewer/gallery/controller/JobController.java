package cn.artviewer.gallery.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.artviewer.gallery.model.Role;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.SchedulerException;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.artviewer.gallery.annotation.LogAnnotation;
import cn.artviewer.gallery.dao.JobDao;
import cn.artviewer.gallery.model.JobModel;
import cn.artviewer.gallery.page.table.PageTableHandler;
import cn.artviewer.gallery.page.table.PageTableHandler.CountHandler;
import cn.artviewer.gallery.page.table.PageTableHandler.ListHandler;
import cn.artviewer.gallery.page.table.PageTableRequest;
import cn.artviewer.gallery.page.table.PageTableResponse;
import cn.artviewer.gallery.service.JobService;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/jobs")
public class JobController {

	@Autowired
	private JobService jobService;
	@Autowired
	private JobDao jobDao;

	@LogAnnotation
	@ApiOperation("添加定时任务")
	@PostMapping
	@RequiresPermissions("job:add")
	public void add(@RequestBody JobModel jobModel) {
		JobModel model = jobDao.getByName(jobModel.getJobName());
		if (model != null) {
			throw new IllegalArgumentException(jobModel.getJobName() + "已存在");
		}

		jobModel.setIsSysJob(false);
		jobService.saveJob(jobModel);
	}

	// zss ---未使用分页
//	@LogAnnotation
//	@ApiOperation("获取所有工作")
//	@GetMapping("/list")
//	public ModelAndView lists(HttpServletRequest request) {
//		ModelAndView mv = new ModelAndView("console/job/jobList");
//		List jobs = jobDao.list(Maps.newHashMap(), 0, 10);
//		mv.addObject("job", jobs);
//		return mv;
//	}

	//  zss ---使用分页
	@GetMapping("/list")
	@ApiOperation("获取工作列表")
	public ModelAndView jobList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("console/job/jobList");
		String pageNum = request.getParameter("pageNum");
		if (pageNum == null){
			pageNum = "1";
		}
		Integer pageNo = Integer.parseInt(pageNum);
		Page<Job> jobs = jobService.findByPage(pageNo,2);
		PageInfo<Job> pageInfo = new PageInfo<>(jobs);
		mv.addObject("page",pageInfo);
		return mv;
	}


	@LogAnnotation
	@ApiOperation("修改定时任务")
	@PutMapping
	@RequiresPermissions("job:add")
	public void update(@RequestBody JobModel jobModel) {
		jobModel.setStatus(1);
		jobService.saveJob(jobModel);
	}

	@LogAnnotation
	@ApiOperation("删除定时任务")
	@DeleteMapping("/{id}")
	@RequiresPermissions("job:del")
	public void delete(@PathVariable Long id) throws SchedulerException {
		jobService.deleteJob(id);
	}

	@ApiOperation("根据id获取定时任务")
	@GetMapping("/{id}")
	@RequiresPermissions("job:query")
	public JobModel getById(@PathVariable Long id) {
		return jobDao.getById(id);
	}

	@GetMapping
	@ApiOperation(value = "定时任务列表")
	@RequiresPermissions("job:query")
	public PageTableResponse<JobModel> list(PageTableRequest request) {
		return PageTableHandler.<JobModel> builder().countHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return jobDao.count(request.getParams());
			}
		}).listHandler(new ListHandler<JobModel>() {

			@Override
			public List<JobModel> list(PageTableRequest request) {
				List<JobModel> list = jobDao.list(request.getParams(), request.getOffset(), request.getLimit());
				return list;
			}
		}).build().handle(request);
	}

	@ApiOperation(value = "校验cron表达式")
	@GetMapping(params = "cron")
	public boolean checkCron(String cron) {
		return CronExpression.isValidExpression(cron);
	}

	@Autowired
	private ApplicationContext applicationContext;

	@ApiOperation(value = "springBean名字")
	@GetMapping("/beans")
	public List<String> listAllBeanName() {
		String[] strings = applicationContext.getBeanDefinitionNames();
		List<String> list = new ArrayList<>();
		for (String str : strings) {
			if (str.contains(".")) {
				continue;
			}

			Class<?> clazz = getClass(str);
			if (clazz.isAssignableFrom(Controller.class) || clazz.isAnnotationPresent(RestController.class)) {
				continue;
			}

			list.add(str);
		}
		list.sort((l1, l2) -> l1.compareTo(l2));

		return list;
	}

	@ApiOperation(value = "springBean的无参方法")
	@GetMapping("/beans/{name}")
	public Set<String> listMethodName(@PathVariable String name) {
		Class<?> clazz = getClass(name);
		Method[] methods = clazz.getDeclaredMethods();

		Set<String> names = new HashSet<>();
		Arrays.asList(methods).parallelStream().forEach(m -> {
			int b = m.getModifiers();// public 1 static 8 final 16
			if (b == 1 || b == 9 || b == 17 || b == 25) {
				Class<?>[] classes = m.getParameterTypes();
				if (classes.length == 0) {
					names.add(m.getName());
				}
			}
		});

		return names;
	}

	private Class<?> getClass(String name) {
		Object object = applicationContext.getBean(name);
		Class<?> clazz = object.getClass();
		if (AopUtils.isAopProxy(object)) {
			clazz = clazz.getSuperclass();
		}

		return clazz;
	}

	@GetMapping("addJob")
	public ModelAndView add(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/console/job/addJob");
		return mv;
	}

}
