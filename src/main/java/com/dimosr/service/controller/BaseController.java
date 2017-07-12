package com.dimosr.service.controller;

import com.dimosr.dependency.DependencyConfiguration;
import com.dimosr.dependency.RemoteService;
import com.dimosr.service.core.ServiceCall;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BaseController {

    @Autowired
    RemoteService remoteService;

    @Autowired
    ServiceCall<Integer, Integer> remoteServiceCall;

    private static final String VIEW_INDEX = "index";
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(ModelMap model) {
        return VIEW_INDEX;
    }

    @RequestMapping(value = "/{input}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String calculateResult(@PathVariable int input, ModelMap model) {
        logger.info("Received request for input: {}", input);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            final int result = remoteServiceCall.call(input);
            resultMap.put("result", result);
        } catch(Exception e) {
            String message = String.format("%s: %s", e.getClass().toString(), e.getMessage());
            resultMap.put("result", message);
        }

        return getJson(resultMap);
    }

    @RequestMapping(value = "/configure", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void configureDependency(@RequestBody DependencyConfiguration configuration) {
        remoteService.updateConfiguration(configuration);
        logger.info("Updated configuration: {}", configuration);
    }

    private String getJson(Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

}
