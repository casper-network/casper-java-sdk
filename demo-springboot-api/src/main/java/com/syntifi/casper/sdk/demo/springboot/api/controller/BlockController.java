package com.syntifi.casper.sdk.demo.springboot.api.controller;

import java.net.MalformedURLException;

import com.syntifi.casper.sdk.model.block.JsonBlockData;
import com.syntifi.casper.sdk.service.CasperService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author Alexandre Carvalho
*/
@Controller
@CrossOrigin("*")
@RequestMapping("/block")
public class BlockController {
	private static String testIP = "144.91.79.58";
	private static int testPort = 7777;

    @GetMapping("")
    @ResponseBody
    public JsonBlockData getLastBlock() throws MalformedURLException {
        CasperService service = CasperService.usingPeer(testIP, testPort);

        return service.getBlock();
    }
}
