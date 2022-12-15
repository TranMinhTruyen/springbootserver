package com.ggapp.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.CompletableFuture;

/**
 * @author Tran Minh Truyen on 07/12/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */
@Controller
public class ThymeleafResource {
    @GetMapping(value = "/view/activate-page")
    public String activePage(@RequestParam(name = "confirmKey", required = false) String confirmKey,
                                                @RequestParam(name = "email", required = false) String email, Model model) {
        return "activatePage";
    }
}
