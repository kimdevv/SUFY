package com.kimdev.SubwayNotify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimdev.SubwayNotify.hidden.HiddenData;
import com.kimdev.SubwayNotify.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    @GetMapping({"", "/", "/login"})
    public String login(Model model) {
        model.addAttribute("restApiKey", HiddenData.kakaoRestApi);
        model.addAttribute("callBack", HiddenData.kakaoCallBack);

        return "login/login"; // login.jsp 호출
    }

    @GetMapping("/login/kakaoCallback")
    public String kakaoCallback(Model model,
                                @RequestParam(required = false) String code,
                                @RequestParam(required = false) String error) throws JsonProcessingException {

        if (error != null) { // 에러가 발생했을 경우
            model.addAttribute("restApiKey", HiddenData.kakaoRestApi);
            model.addAttribute("callBack", HiddenData.kakaoCallBack);

            return "login/login"; // 로그인 창으로 돌려 보낸다.

        } else { // 로그인 인가 완료됐을 경우 (에러가 뜨지 않았을 경우)
            loginService.kakaoCallback(model, code);
            return "login/tokenSave";
        }
    }
}
