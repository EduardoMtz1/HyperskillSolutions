package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import platform.model.Code;
import platform.service.CodeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HtmlController {
    private CodeService codeService;

    @Autowired
    public HtmlController(CodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping(path = "/code/{id}", produces = "text/html")
    public String getHtmlCode(@PathVariable String id, Model model) {
        Code code = codeService.getCodeFromStorage(id);

        if(code.isViewLimit()) {
            codeService.updateViewById(code);
        }

        if(code.isTimeLimit()) {
            long currentSecond = System.currentTimeMillis();
            codeService.updateTimeById(code, currentSecond);
        }


        model.addAttribute("code", code);
        return "Code";
    }

    @GetMapping(path = "/code/new", produces = "text/html")
    public String getNewCodeView() {
        return "Newcode";
    }

    @GetMapping(path = "/code/latest", produces = "text/html")
    public String getLatestCodesView(Model model) {
        List<Code> latestCodes = codeService.getLastCode();
        model.addAttribute("latestCodes", latestCodes);
        return "Latestcode";
    }

}
