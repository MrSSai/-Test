package cn.artviewer.gallery.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import cn.artviewer.gallery.model.FileInfo;

public interface FileService {

	FileInfo save(MultipartFile file) throws IOException;

	void delete(String id);

}