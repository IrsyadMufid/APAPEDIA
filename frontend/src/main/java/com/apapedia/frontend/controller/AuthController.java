package com.apapedia.frontend.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.apapedia.frontend.security.xml.Attributes;
import com.apapedia.frontend.security.xml.ServiceResponse;
import com.apapedia.frontend.service.AuthService;
import com.apapedia.frontend.setting.Setting;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;
    
    @GetMapping("")
    public String home(Model model, HttpServletRequest request){
        if (request.getSession().getAttribute("accessToken") != null){
            model.addAttribute("activeUserId", request.getSession().getAttribute("activeUserId"));
        }
        
        return "home";
    }

    private WebClient webClient = WebClient.builder()
                    .codecs(configurer -> configurer.defaultCodecs()
                    .jaxb2Decoder(new Jaxb2XmlDecoder()))
                    .build();

    @GetMapping("/validate-ticket")
    public ModelAndView adminLoginSSO(@RequestParam(value = "ticket", required = false) String ticket, HttpServletRequest request) {
        ServiceResponse serviceResponse = this.webClient.get().uri(
                String.format(
                        Setting.SERVER_VALIDATE_TICKET,
                        ticket,
                        Setting.CLIENT_LOGIN
                )
        ).retrieve().bodyToMono(ServiceResponse.class).block();

        Attributes attributes = serviceResponse.getAuthenticationSuccess().getAttributes();
        String username = serviceResponse.getAuthenticationSuccess().getUser();

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "seller", null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        String name = attributes.getNama();
        var token = authService.getTokenSSO(username, name);

        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        httpSession.setAttribute("accessToken", token);
        httpSession.setAttribute("loginSSO", true);
        var activeUser = authService.getActiveUser(token);
        httpSession.setAttribute("activeUserId", activeUser.getId());

        return new ModelAndView("redirect:/user/profile/edit/" + activeUser.getId());
    }

    @GetMapping("/validate-ticket-login")
    public ModelAndView userLoginSSO(@RequestParam(value = "ticket", required = false) String ticket, HttpServletRequest request) {
        ServiceResponse serviceResponse = this.webClient.get().uri(
                String.format(
                        Setting.SERVER_VALIDATE_TICKET,
                        ticket,
                        Setting.CLIENT_LOGIN_USER
                )
        ).retrieve().bodyToMono(ServiceResponse.class).block();

        Attributes attributes = serviceResponse.getAuthenticationSuccess().getAttributes();
        String username = serviceResponse.getAuthenticationSuccess().getUser();
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "seller", null);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        
        String name = attributes.getNama();
        var token = authService.loginUserSSO(username, name);

        if (token != null) {
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            httpSession.setAttribute("accessToken", token);
            httpSession.setAttribute("loginSSO", true);
            var activeUser = authService.getActiveUser(token);
            httpSession.setAttribute("activeUserId", activeUser.getId());
            return new ModelAndView("redirect:/");
        } else {
            request.getSession().invalidate();
            return new ModelAndView("redirect:/");
        }
    }

    @GetMapping("/register")
    public ModelAndView loginSSO() {
        return new ModelAndView("redirect:"+ Setting.SERVER_LOGIN + Setting.CLIENT_LOGIN);
    }

    @GetMapping("/logout-sso")
    public ModelAndView logoutSSO(Principal principal) {
        return new ModelAndView("redirect:" + Setting.SERVER_LOGOUT + Setting.CLIENT_LOGOUT);
    }

    @GetMapping("/login")
    public ModelAndView loginUserSSO() {
        return new ModelAndView("redirect:"+ Setting.SERVER_LOGIN + Setting.CLIENT_LOGIN_USER);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }
}
