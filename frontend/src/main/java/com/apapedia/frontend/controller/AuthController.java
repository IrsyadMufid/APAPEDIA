package com.apapedia.frontend.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apapedia.frontend.dto.auth.request.AuthRequestDTO;
import com.apapedia.frontend.dto.user.request.CreateUserRequestDTO;
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
            var token = (String) request.getSession().getAttribute("accessToken");
            var activeUser = authService.getActiveUser(token);
            model.addAttribute("activeUser", activeUser);
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

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/login-sso")
    public ModelAndView loginSSO() {
        return new ModelAndView("redirect:"+ Setting.SERVER_LOGIN + Setting.CLIENT_LOGIN);
    }

    @GetMapping("/logout-sso")
    public ModelAndView logoutSSO(Principal principal) {
        return new ModelAndView("redirect:" + Setting.SERVER_LOGOUT + Setting.CLIENT_LOGOUT);
    }

    @GetMapping("/register")
    public String register(Model model){
        var registerDTO = new CreateUserRequestDTO();
        model.addAttribute("registerDTO", registerDTO);
        return "/user/register";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request){
        var loginDTO = new AuthRequestDTO();
        model.addAttribute("loginDTO", loginDTO);
        return "/user/login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute AuthRequestDTO authRequestDTO, HttpServletRequest request, RedirectAttributes redirectAttributes, BindingResult bindingResult) throws IOException, InterruptedException{
        if (bindingResult.hasFieldErrors()){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        }
        else {
            var token = authService.getToken(authRequestDTO.getUsername(), authRequestDTO.getPassword());

            String username = authRequestDTO.getUsername();
            String password = authRequestDTO.getPassword();
            
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, null);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            httpSession.setAttribute("accessToken", token);
            httpSession.setAttribute("loginSSO", false);
        }
        return "home";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession httpSession = request.getSession(true);
        httpSession.invalidate();
        return "redirect:/";
    }
}
