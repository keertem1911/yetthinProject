package com.yetthin.web.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;

import com.yetthin.web.service.PhoneVersionService;

@RequestMapping("/help")
public class PhoneVersionController {
	@Resource(name="PhoneVersionService")
	private PhoneVersionService phoneVersionService;
	
}
