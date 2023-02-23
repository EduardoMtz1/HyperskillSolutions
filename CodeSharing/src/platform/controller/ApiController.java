package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import platform.model.Code;
import platform.service.CodeService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class ApiController {

    private static final String DATE_FORMATER = "yyyy-MM-dd HH:mm:ss";

    private CodeService service;

    public ApiController() {
    }

    @Autowired
    public ApiController(CodeService service) {
        this.service = service;
    }

    @GetMapping(path = "/api/code/{id}", produces = "application/json;charset=UTF-8")
    public Code getApiCode(@PathVariable("id") String id) {
        Code responseCode = service.getCodeFromStorage(id);

        if (responseCode.isViewLimit()) {
            service.updateViewById(responseCode);

        }

        if (responseCode.isTimeLimit()) {
            long currentSecond = System.currentTimeMillis();
            service.updateTimeById(responseCode, currentSecond);

        }


        return service.getCodeFromStorage(id);
    }

    @GetMapping(path = "/api/code/latest", produces = "application/json;charset=UTF-8")
    public Object[] getApiLatestCode() {
        return service.getLastCode().toArray();
    }


    @PostMapping(path = "/api/code/new", produces = "application/json;charset=UTF-8")
    public String setApiCode(@RequestBody Code newCode) {
        Code responseCode = new Code();
        responseCode.setCode(newCode.getCode());
        responseCode.setTime(newCode.getTime());
        responseCode.setDate(getActualDate());
        responseCode.setStartSeconds(System.currentTimeMillis());
        responseCode.setViews(newCode.getViews());
        responseCode.setViewLimit(newCode.getViews() > 0);
        responseCode.setTimeLimit(newCode.getTime() > 0);
        service.addCodeToStorage(responseCode);
        String response = "{ \"id\" : \"" + responseCode.getId() + "\" }";
        return response;
    }

    public String getActualDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATER);
        return now.format(formatter);
    }
}
