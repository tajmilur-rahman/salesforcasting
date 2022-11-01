package sales.forecast.com.controllers;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import tech.tablesaw.api.Table;

@Controller
@RequestMapping
public class fileUploadController {
	
	@GetMapping("/uploadfile")
	public String uploadImage() {
		return "uploadfile";
		
	}
	@PostMapping("/upload")
	public String save(Model model,@RequestParam("csvfile") MultipartFile file) {
	    try {
	    	Table t = Table.read().csv(file.getInputStream());
	    	System.out.println(t.structure());
	      
	    } catch (IOException e) {
	      
	    }
	    return "uploadfile";
	  }
	

}
