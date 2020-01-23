package com.formacion.springboot.app.zuul.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PostTiempoTranscurridoFilter extends ZuulFilter {
	private static Logger log = LoggerFactory.getLogger(PreTiempoTranscurridoFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		final RequestContext ctx = RequestContext.getCurrentContext();
		final HttpServletRequest request = ctx.getRequest();
		log.info("Entrando a post filter");

		final Long tiempoInicio = (Long) request.getAttribute("tiempoInicio");
		final Long tiempoFinal = System.currentTimeMillis();

		final Long tiempoTranscurrido = tiempoFinal - tiempoInicio;

		log.info(String.format("tiempo transcurrido en segundos %s", tiempoTranscurrido.doubleValue() / 1000.00));
		log.info(String.format("tiempo transcurrido en milisegundos %s", tiempoTranscurrido));

		return null;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
