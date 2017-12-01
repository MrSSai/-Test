package cn.artviewer.gallery.service;

import cn.artviewer.gallery.model.SysLogs;

/**
 * 日志service
 * 
 * @author turkey
 *
 *         2017年8月19日
 */
public interface SysLogService {

	void save(SysLogs sysLogs);

	void save(Long userId, String module, Boolean flag, String remark);

	void deleteLogs();
}
