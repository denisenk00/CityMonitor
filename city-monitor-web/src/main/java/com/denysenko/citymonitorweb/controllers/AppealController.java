package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.services.entity.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/appeals")
public class AppealController {
    @Autowired
    private AppealService appealService;

    @ModelAttribute("unreadAppealsCnt")
    public long getCountOfUnreadAppeals(){
        return appealService.countOfUnreadAppeals();
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('appeals:read', 'appeals:write')")
    public String appealsPage(Model model,
                              @RequestParam(name = "tab", defaultValue = "all") String tabName,
                              @RequestParam(defaultValue = "1", required = false) int page,
                              @RequestParam(defaultValue = "30", required = false) int size){
        model.addAttribute("tab", tabName);
        model.addAttribute("appeals", null);
        return "appeals/appeals";
    }

    @PatchMapping("/{id}/changeStatus")
    @PreAuthorize("hasAuthority('appeals:write')")
    public ResponseEntity changeStatus(@PathVariable(name = "id") long id, @RequestParam(name = "status") String status){
        AppealStatus appealStatus = AppealStatus.valueOf(status);
        appealService.updateStatusById(id, appealStatus);
        return ResponseEntity.ok().body("{\"msg\":\"success\"}");
    }

    @PatchMapping("/changeStatuses")
    @PreAuthorize("hasAuthority('appeals:write')")
    public ResponseEntity changeStatus(@RequestParam(name = "ids") Set<String> ids, @RequestParam(name = "status") String status){
        AppealStatus appealStatus = AppealStatus.valueOf(status);
        ids.forEach(id -> appealService.updateStatusById(Long.valueOf(id), appealStatus));
        return ResponseEntity.ok().body("{\"msg\":\"success\"}");
    }
}
