package com.vaannila.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

@SuppressWarnings("deprecation")
public class SearchController extends SimpleFormController {

	@Override
	protected ModelAndView onSubmit(Object command) throws Exception {
		Process process = (Process) command;
		
		return new ModelAndView("office", "process_", process);
	}
}
