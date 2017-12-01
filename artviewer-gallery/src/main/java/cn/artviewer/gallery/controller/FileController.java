package cn.artviewer.gallery.controller;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.artviewer.gallery.annotation.LogAnnotation;
import cn.artviewer.gallery.dao.FileInfoDao;
import cn.artviewer.gallery.dto.LayuiFile;
import cn.artviewer.gallery.dto.LayuiFile.LayuiFileData;
import cn.artviewer.gallery.model.FileInfo;
import cn.artviewer.gallery.page.table.PageTableRequest;
import cn.artviewer.gallery.page.table.PageTableHandler;
import cn.artviewer.gallery.page.table.PageTableResponse;
import cn.artviewer.gallery.page.table.PageTableHandler.CountHandler;
import cn.artviewer.gallery.page.table.PageTableHandler.ListHandler;
import cn.artviewer.gallery.service.FileService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/files")
public class FileController {

	@Autowired
	private FileService fileService;
	@Autowired
	private FileInfoDao fileInfoDao;

	@LogAnnotation
	@PostMapping
	@ApiOperation(value = "文件上传")
	public FileInfo uploadFile(MultipartFile file) throws IOException {
		return fileService.save(file);
	}

	/**
	 * layui富文本文件自定义上传
	 * 
	 * @param file
	 * @param domain
	 * @return
	 * @throws IOException
	 */
	@LogAnnotation
	@PostMapping("/layui")
	@ApiOperation(value = "layui富文本文件自定义上传")
	public LayuiFile uploadLayuiFile(MultipartFile file, String domain) throws IOException {
		FileInfo fileInfo = fileService.save(file);

		LayuiFile layuiFile = new LayuiFile();
		layuiFile.setCode(0);
		LayuiFileData data = new LayuiFileData();
		layuiFile.setData(data);
		data.setSrc(domain + "/files" + fileInfo.getUrl());
		data.setTitle(file.getOriginalFilename());

		return layuiFile;
	}

	@GetMapping
	@ApiOperation(value = "文件查询")
	@RequiresPermissions("sys:file:query")
	public PageTableResponse<FileInfo> listFiles(PageTableRequest request) {
		return PageTableHandler.<FileInfo> builder().countHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return fileInfoDao.count(request.getParams());
			}
		}).listHandler(new ListHandler<FileInfo>() {

			@Override
			public List<FileInfo> list(PageTableRequest request) {
				List<FileInfo> list = fileInfoDao.list(request.getParams(), request.getOffset(), request.getLimit());
				return list;
			}
		}).build().handle(request);
	}

	@LogAnnotation
	@DeleteMapping("/{id}")
	@ApiOperation(value = "文件删除")
	@RequiresPermissions("sys:file:del")
	public void delete(@PathVariable String id) {
		fileService.delete(id);
	}

}
