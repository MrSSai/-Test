package cn.artviewer.gallery.config;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ehcache配置
 * 
 * @author turkey
 *
 */
@Configuration
public class EhCacheConfig {

	@Bean("ehCacheManager")
	public EhCacheManager cacheManager() {
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");

		return cacheManager;
	}
}
