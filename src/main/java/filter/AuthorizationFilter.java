package filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JwtUtil;

import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "AuthorizationFilter",servletNames = {"locationController"})
public class AuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        try{
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpSession session = request.getSession();

            String token = session.getAttribute("token").toString();
            Jwt<?, ?> jwt = JwtUtil.parseToken(token);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payload = objectMapper.readValue(JwtUtil.parsePayloadToJson(jwt.getPayload().toString()), Map.class);

            request.setAttribute("userId", payload.get("id").toString());
            chain.doFilter(request, servletResponse);
        }catch (Exception e){
            HttpServletResponse http = (HttpServletResponse) servletResponse;
            http.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            http.sendRedirect("/weather/user/login");
        }

    }
}
