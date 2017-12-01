package cn.artviewer.gallery.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.artviewer.gallery.annotation.LogAnnotation;
import cn.artviewer.gallery.dao.NoticeDao;
import cn.artviewer.gallery.dto.NoticeReadVO;
import cn.artviewer.gallery.dto.NoticeVO;
import cn.artviewer.gallery.model.Notice;
import cn.artviewer.gallery.model.Notice.Status;
import cn.artviewer.gallery.model.User;
import cn.artviewer.gallery.page.table.PageTableHandler;
import cn.artviewer.gallery.page.table.PageTableHandler.CountHandler;
import cn.artviewer.gallery.page.table.PageTableHandler.ListHandler;
import cn.artviewer.gallery.page.table.PageTableRequest;
import cn.artviewer.gallery.page.table.PageTableResponse;
import cn.artviewer.gallery.utils.UserUtil;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/notices")
public class NoticeController {

	@Autowired
	private NoticeDao noticeDao;

	@LogAnnotation
	@PostMapping
	@ApiOperation(value = "保存公告")
	@RequiresPermissions("notice:add")
	public Notice saveNotice(@RequestBody Notice notice) {
		noticeDao.save(notice);

		return notice;
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取公告")
	@RequiresPermissions("notice:query")
	public Notice get(@PathVariable Long id) {
		return noticeDao.getById(id);
	}

	@GetMapping(params = "id")
	public NoticeVO readNotice(Long id) {
		NoticeVO vo = new NoticeVO();

		Notice notice = noticeDao.getById(id);
		if (notice == null || notice.getStatus() == Status.DRAFT) {
			return vo;
		}
		vo.setNotice(notice);

		noticeDao.saveReadRecord(notice.getId(), UserUtil.getCurrentUser().getId());

		List<User> users = noticeDao.listReadUsers(id);
		vo.setUsers(users);

		return vo;
	}

	@LogAnnotation
	@PutMapping
	@ApiOperation(value = "修改公告")
	@RequiresPermissions("notice:add")
	public Notice updateNotice(@RequestBody Notice notice) {
		Notice no = noticeDao.getById(notice.getId());
		if (no.getStatus() == Status.PUBLISH) {
			throw new IllegalArgumentException("发布状态的不能修改");
		}
		noticeDao.update(notice);

		return notice;
	}

	@GetMapping
	@ApiOperation(value = "公告管理列表")
	@RequiresPermissions("notice:query")
	public PageTableResponse<Notice> listNotice(PageTableRequest request) {
		return PageTableHandler.<Notice> builder().countHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return noticeDao.count(request.getParams());
			}
		}).listHandler(new ListHandler<Notice>() {

			@Override
			public List<Notice> list(PageTableRequest request) {
				return noticeDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).build().handle(request);
	}

	@LogAnnotation
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除公告")
	@RequiresPermissions(value = { "notice:del" })
	public void delete(@PathVariable Long id) {
		noticeDao.delete(id);
	}

	@ApiOperation(value = "未读公告数")
	@GetMapping("/count-unread")
	public Integer countUnread() {
		User user = UserUtil.getCurrentUser();
		return noticeDao.countUnread(user.getId());
	}


	//  zss -- 未使用分页
	@GetMapping("/all")
	@ApiOperation(value = "所有公告")
//	public ModelAndView notice(HttpServletRequest request) {
//		ModelAndView mv = new ModelAndView("console/notice/noticeList");
//		List<Notice> notice = noticeDao.list(Maps.newHashMap(),0,10);
//		mv.addObject("noti", notice);
//		return mv;
//	}
	public ModelAndView notice(HttpServletRequest request, HttpServletResponse response) {
		List<Notice> notice = noticeDao.list(Maps.newHashMap(),0,10);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/console/notice/noticeList");
		mv.addObject("notices", notice);
		return mv;
	}

//	// zss -- 使用分页
//	@GetMapping("/all")
//	@ApiOperation(value = "所有公告")
//	public ModelAndView notice(HttpServletRequest request) {
//		ModelAndView mv = new ModelAndView("console/notice/noticeList");
//		String pageNum = request.getParameter("pageNum");
//		if (pageNum == null){
//			pageNum = "1";
//		}
//		Integer pageNo = Integer.parseInt(pageNum);
//		Page<Notice> notices =
//		List<Notice> notice = noticeDao.list(Maps.newHashMap(),0,10);
//		mv.addObject("noti", notice);
//		return mv;
//	}




	@GetMapping("/published")
	@ApiOperation(value = "公告列表")
	public PageTableResponse<NoticeReadVO> listNoticeReadVO(PageTableRequest request) {
		request.getParams().put("userId", UserUtil.getCurrentUser().getId());

		return PageTableHandler.<NoticeReadVO> builder().countHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return noticeDao.countNotice(request.getParams());
			}
		}).listHandler(new ListHandler<NoticeReadVO>() {

			@Override
			public List<NoticeReadVO> list(PageTableRequest request) {
				return noticeDao.listNotice(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).build().handle(request);
	}
}
