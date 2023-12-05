package com.open.erp.openerp.filter;

import com.open.erp.openerp.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class TenantFilter extends OncePerRequestFilter {
    private static final String TENANT_ID_HEADER = "X-Tenant";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String tenant = request.getHeader(TENANT_ID_HEADER);

        if (StringUtils.hasText(tenant)) {
            log.info("setting context " + tenant);
            TenantContext.setTenantId(tenant);
            filterChain.doFilter(request, response);
            TenantContext.clear();
            return;
        }

        filterChain.doFilter(request, response);
    }
}
