package cn.artviewer.gallery.service;

import java.util.List;

import cn.artviewer.gallery.model.Mail;

public interface MailService {

	void save(Mail mail, List<String> toUser);
}
