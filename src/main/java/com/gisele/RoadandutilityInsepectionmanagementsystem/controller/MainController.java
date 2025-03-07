package com.gisele.RoadandutilityInsepectionmanagementsystem.controller;


import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.Project;
import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.User;
import com.gisele.RoadandutilityInsepectionmanagementsystem.service.ProjectService;
import com.gisele.RoadandutilityInsepectionmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/Inspection")
public class MainController {

    private Logger logger = Logger.getLogger(getClass().getName());

    private ProjectService projectService;

    private UserService userService;
    @Autowired
    public MainController(ProjectService projectService, UserService theuserService) {
        this.projectService = projectService;
        this.userService = theuserService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder)
    {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }


    private static String UPLOAD_DIR = "./uploads/";
    private static String IMAGE_DIR = "./images/";
    @GetMapping("/mainPage")
    public String mainPage(Model theModel, HttpSession session) throws IOException {

        Project project = new Project();
        theModel.addAttribute("project", project);

        // get the userRoles list from session and add it to the model
        List<String> userRoles = (List<String>) session.getAttribute("roles");
        User user = (User) session.getAttribute("user");
       if((userRoles==null) || user == null){
           //response.sendRedirect(request.getContextPath() + "/");
           logger.info("the credentials are null");
           return "redirect:/";
       }

        logger.info(">>>>>>>>>>Current User = " + user.getFirstName());
        for(String role:userRoles){
            logger.info(">>>>>>>>>>role = " + role);
        }
        theModel.addAttribute("userId", (Long)session.getAttribute("userId"));
        theModel.addAttribute("user", user);

        // clear the session
        //session.invalidate();
        return "Thymeleaf/mainPage";
    }

    @PostMapping("/process")
    public String ProcessData(@RequestParam("report") MultipartFile report, @RequestParam("pic")MultipartFile pic,
                              @Valid @ModelAttribute("project") Project theproject,
                              BindingResult theBindingResult,
                              @RequestParam("userId") int AuthId,
                              Model theModel,
                              HttpServletRequest request) throws ParseException {

        logger.info(">>>>>>>>>> Stage 1 ");
        if(theBindingResult.hasErrors()){
            HttpSession session = request.getSession();
            logger.info(">>>>>>>>>> You have errors>>>>>>>>>>>>>>>."+theBindingResult.toString());
            theModel.addAttribute("userId", (Long)session.getAttribute("userId"));
            return "Thymeleaf/mainPage";
        }
        Project project = new Project();

        // Date Formatting

        Date startDate = null;
        Date currentDate = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if(theproject.getSdate()!=null){
            startDate = formatter.parse(theproject.getSdate());
        }
        if(theproject.getCdate()!=null){
             currentDate = formatter.parse(theproject.getCdate());
        }

//        LocalDate startDate = LocalDate.parse(theproject.getSdate(), DateTimeFormatter.ISO_LOCAL_DATE);
//        LocalDate currentDate = LocalDate.parse(theproject.getCdate(), DateTimeFormatter.ISO_LOCAL_DATE);
        logger.warning("==============Author==============");
        Long userId = (long) AuthId;
        User existingUser = userService.findById(userId);
        logger.warning("\n " + existingUser.toString()+"\n");

        // now Add the userId to the session of future reference in the session
        HttpSession session = request.getSession();
        if (session.getAttribute("userId") == null) {
            session.setAttribute("userId", userId);
            logger.warning("================AuthId set as session attribute============");
        }

        logger.warning("================Project============");
        logger.warning(theproject.toString());

        Project dbProject = null;
        if(theproject.getId()==0){
            dbProject = new Project();
            logger.info(">>>>>>>>>>>>>>>>>>>>>> New Project instance");
        }else{
            dbProject = projectService.findById(theproject.getId());
            logger.info(">>>>>>>>>>>>>>>>>>>>>> Project to update " + dbProject);
        }

        dbProject.setPname(theproject.getPname());
        dbProject.setLocation(theproject.getLocation());
        dbProject.setStart_date(startDate);
        dbProject.setTheCurrent_date(currentDate);
        dbProject.setProjectType(theproject.getProjectType());
        dbProject.setSafety(theproject.getSafety());
        dbProject.setWeatherConditions(theproject.getWeatherConditions());
        dbProject.setStatus(theproject.getStatus());
        dbProject.setAesthetics(theproject.getAesthetics());

        // Saving file to the server
        if (report.isEmpty() || pic.isEmpty()) {
            logger.warning("The report or Image is empty");
        }
        else{
            /// Save file to the server no duplicates
            try {
                byte[] bytes = report.getBytes();
                String originalFileName = report.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + originalFileName);

                if (Files.exists(path)) {
                    // File with the same name already exists, do not save
                    logger.info("File with name " + originalFileName + " already exists, skipping save.");
                    dbProject.setReportName(originalFileName);
                } else {
                    // Save the file
                    Files.write(path, bytes);
                    dbProject.setReportName(originalFileName);
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>Path for file " + path);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Save image to server No Duplicate
            try {
                byte[] bytes = pic.getBytes();
                String originalPicName = pic.getOriginalFilename();
                Path filePath = Paths.get(IMAGE_DIR + originalPicName);
                if (Files.notExists(filePath)) {
                    Files.write(filePath, bytes);
                    dbProject.setImageName(originalPicName);
                    logger.info(">>>>>>>>>>>>>>>>>>>>>Path for Image" + filePath);
                } else {
                    dbProject.setImageName(originalPicName);
                    logger.info(">>>>>>>>>>>>>>>>>>>>>Image already exists: " + filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        projectService.save(dbProject, userId);
        if(theproject.getId()==0){
            session.setAttribute("saveSuccess", "Project captured Successfully");
            return "redirect:/Dashboard";
        }
        else{
            return "redirect:/Inspection/list";
        }
    }

    @GetMapping("/list")
    public String getAllProjects(Model theModel, HttpServletRequest request){
//>>>>>>>>>>>>>>Normal GetAll>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//        List<Project> theProjects = projectService.findAll();
//        if(theProjects!=null){
//            logger.info(">>>>>>>>>>>>>>>>>>>>>Full List");
//        }
//
//       //  add to the spring model
//        theModel.addAttribute("theProjects", theProjects);
//>>>>>>>>>>>>>>Normal GetAll>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        String keyword = null;
        return listByPage(theModel, 1, keyword);
    }

    @GetMapping("/page/{pageNumber}")
    public String listByPage(Model theModel, @PathVariable("pageNumber") int currentPage,
        @Param("keyword") String keyword){

        //pagination
        Page<Project> page = projectService.findAll(currentPage, keyword);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        // get employees from DB
        List<Project> theProjects = page.getContent();

        if(theProjects!=null){
            logger.info(">>>>>>>>>>>>>>>>>>>>>Full List");
        }else{
            logger.info(">>>>>>>>>>>>>>>>>>>>>Empty List");
        }
        //add to the spring model
        theModel.addAttribute("currentPage", currentPage);
        theModel.addAttribute("totalItems", totalItems);
        theModel.addAttribute("totalPages", totalPages);
        theModel.addAttribute("theProjects",theProjects);
        theModel.addAttribute("keyword",keyword);
        return "Thymeleaf/projects-list";

    }

    @GetMapping("/view/{filename:.+}")
    public void viewFile(@PathVariable String filename, HttpServletResponse response) throws IOException {
        Path file = Paths.get(UPLOAD_DIR + filename);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>Name of file at download "  + filename);
        if (Files.exists(file)) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + filename);
            Files.copy(file, response.getOutputStream());
            response.getOutputStream().flush();
        }
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = new FileSystemResource(IMAGE_DIR + filename);
        if (file.exists() && file.isReadable()) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("projectId") int theId, Model theModel,
        HttpServletRequest request){
        // get the employee from the service
        Project theProject = projectService.findById(theId);

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date Start_date  = theProject.getStart_date(); //
        String sDate = formatter.format(Start_date);
       // String sDate = date.format(formatter);
        Date Current_date  = theProject.getTheCurrent_date(); //
        //String cDate = date2.format(formatter);
        String cDate = formatter.format(Current_date);
        theProject.setSdate(sDate);
        theProject.setCdate(cDate);
        // set employee in the model to prepopulate the form
        theModel.addAttribute("project", theProject);
        logger.info(">>>>>>>>>> Start Date "+ sDate);
        logger.info(">>>>>>>>>> End Date "+ cDate);

        // The author id
        HttpSession session = request.getSession();
        theModel.addAttribute("userId", (Long)session.getAttribute("userId"));
        // send over to our form
        return "Thymeleaf/mainPage";
    }

    @PostMapping("/deleteProject")
    public String deleteStudent(@RequestParam("projectId") int theId){
        projectService.deleteById(theId);
        return "redirect:/Inspection/list";
    }


}

