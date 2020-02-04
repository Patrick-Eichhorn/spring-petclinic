package org.springframework.samples.petclinic.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class EumInterceptorConfiguration implements WebMvcConfigurer {

	@Value("${eum.server:}")
	String eumUrl;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
					ModelAndView modelAndView) {

				if (!StringUtils.isEmpty(eumUrl) && modelAndView != null) {
					String viewName = modelAndView.getViewName();
					if (viewName != null && !viewName.startsWith("redirect:")) {
						modelAndView.addObject("eumserver", eumUrl);
					}
				}
			}
		});
	}

}
