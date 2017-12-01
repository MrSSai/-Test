package cn.artviewer.gallery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger文档
 * 
 * @author turkey
 *
 *         2017年7月21日
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean("userDocket")
	public Docket userDocket() {
		return docket("用户", "/users.*");
	}

	@Bean("roleDocket")
	public Docket roleDocket() {
		return docket("角色", "/roles.*");
	}

	@Bean("permissionDocket")
	public Docket permissionDocket() {
		return docket("权限", "/permissions.*");
	}

	@Bean("fileDocket")
	public Docket fileDocket() {
		return docket("文件", "/files.*");
	}

	@Bean("onlineDocket")
	public Docket onlineDocket() {
		return docket("在线用户", "/online/users.*");
	}

	@Bean("loginDocket")
	public Docket loginDocket() {
		return docket("登陆", "/sys/login.*");
	}

	@Bean("articlesDocket")
	public Docket articlesDocket() {
		return docket("文章", "/articles.*");
	}

	@Bean("mailsDocket")
	public Docket mailsDocket() {
		return docket("邮件", "/mails.*");
	}

	@Bean("generateDocket")
	public Docket generateDocket() {
		return docket("代码生成", "/generate.*");
	}

	@Bean("jobsDocket")
	public Docket jobsDocket() {
		return docket("定时任务", "/jobs.*");
	}

	@Bean("excelDocket")
	public Docket excelDocket() {
		return docket("excel下载", "/excels.*");
	}

	private Docket docket(String title, String path) {
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.groupName(title).apiInfo(new ApiInfoBuilder().title(title)
						.contact(new Contact("turkey", "", "xiaoweijiagou@163.com")).version("1.0").build())
				.select().paths(PathSelectors.regex(path)).build();

		return docket;
	}
}
