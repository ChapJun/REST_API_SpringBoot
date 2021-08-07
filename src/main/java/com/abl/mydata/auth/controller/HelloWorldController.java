package com.abl.mydata.auth.controller;

import com.abl.mydata.auth.domain.HelloWorldBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.logging.Logger;

// @Controller + @ResponseBody
// View를 갖지 않는 REST Data (JSON/XML)을 반환
@RestController
public class HelloWorldController {

    private static final Logger logger = Logger.getLogger(HelloWorldController.class.getName());

    private final MessageSource messageSource;

    public HelloWorldController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // GET
    // URI : /hello-world -> end-point
    // @RequestMapping(method=RequestMethod.GET, path="/hello-world")
    @GetMapping(path = "/hello-world")
    public String helloWorld() {

        logger.info("Hello World");
        return "Hello World";
    }

    @GetMapping(path = "/hello-world-internationalized")
    public String helloWorldInternationalized(
            @RequestHeader(name="Accept-Language", required = false) Locale locale) {

        String msg = messageSource.getMessage("greeting.message", null, locale);
        logger.info(msg);
        return msg;

    }

    // alt + enter (미 생성 클래스 생성) -> more action
    @GetMapping(path = "/hello-world-bean")
    public HelloWorldBean helloWorldBean() {

        HelloWorldBean hwb = new HelloWorldBean("Hello World Bean");
        logger.info(hwb.toString());

        // JSON 포맷으로 반환
        return hwb;
    }

    // hello-world-bean/path-variable/kenneth
    @GetMapping(path = "/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBeanPathVariable(@PathVariable String name) {

        HelloWorldBean hwb = new HelloWorldBean(String.format("Hello World Bean : %s", name));
        logger.info(hwb.toString());

        // JSON 포맷으로 반환
        return hwb;
    }


}
