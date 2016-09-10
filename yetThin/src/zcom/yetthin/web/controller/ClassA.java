package zcom.yetthin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/testing")
public   class ClassA {
	@Autowired
	public ClassB classB;
	@Autowired
	public ClassC classC;
	@RequestMapping("/a")
	public String get(){
		classB.print();
		classC.print();
		return "redirect:/index.jsp";
	}
}
