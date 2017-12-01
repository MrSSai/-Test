package cn.artviewer.gallery.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.artviewer.gallery.dao.MailDao;
import cn.artviewer.gallery.model.Mail;
import cn.artviewer.gallery.service.MailService;
import cn.artviewer.gallery.service.SendMailSevice;
import cn.artviewer.gallery.utils.UserUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "adminLogger")
@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private SendMailSevice sendMailSevice;
	@Autowired
	private MailDao mailDao;

	@Override
	@Transactional
	public void save(Mail mail, List<String> toUser) {
		mail.setUserId(UserUtil.getCurrentUser().getId());
		mailDao.save(mail);

		toUser.forEach(u -> {
			int status = 1;
			try {
				sendMailSevice.sendMail(u, mail.getSubject(), mail.getContent());
			} catch (Exception e) {
				log.error("发送邮件失败", e);
				status = 0;
			}

			mailDao.saveToUser(mail.getId(), u, status);
		});

	}

}
