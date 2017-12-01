package cn.artviewer.gallery.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.artviewer.gallery.dao.SysLogsDao;
import cn.artviewer.gallery.model.SysLogs;
import cn.artviewer.gallery.page.table.PageTableRequest;
import cn.artviewer.gallery.page.table.PageTableHandler;
import cn.artviewer.gallery.page.table.PageTableResponse;
import cn.artviewer.gallery.page.table.PageTableHandler.CountHandler;
import cn.artviewer.gallery.page.table.PageTableHandler.ListHandler;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/logs")
public class SysLogsController {

	@Autowired
	private SysLogsDao sysLogsDao;

	@GetMapping
	@RequiresPermissions(value = "sys:log:query")
	@ApiOperation(value = "日志列表")
	public PageTableResponse<SysLogs> list(PageTableRequest request) {
		return PageTableHandler.<SysLogs> builder().countHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return sysLogsDao.count(request.getParams());
			}
		}).listHandler(new ListHandler<SysLogs>() {

			@Override
			public List<SysLogs> list(PageTableRequest request) {
				return sysLogsDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).build().handle(request);
	}

}
