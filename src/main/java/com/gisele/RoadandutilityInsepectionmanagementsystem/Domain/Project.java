package com.gisele.RoadandutilityInsepectionmanagementsystem.Domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name="Construction_Project")
//@DateRange(startDate = "sdate", endDate = "cdate")
public class Project {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @NotNull(message="Project Name is Required")
    @Size(min=5, message="At least five character required")
    @Column(name="Project_Name")
    private String pname;
    @Pattern(regexp = "^K[GNK]\\s*\\d{3}\\s*ST$", message = "Sample : KG 636 ST, KN 635 ST, KK 344 ST")
    @NotNull(message="Road number is Required")
   // @Size(min=5, message="At least five character required")
    private String location;
    //@NotNull(message="Enter the Start date")
    private Date start_date;
    //@NotNull(message="Enter the current date")
    private Date theCurrent_date;
    @Size(min=1, message="Specify the project type")
    @NotNull(message="projectType value is Required")
    private String projectType;
    @Size(min=1, message="Specify the project safety")
    @NotNull(message="safety value is Required")
    private String safety;
    @Size(min=1, message="Specify the Weather Condition")
    @NotNull(message="weatherConditions value is Required")
    private String weatherConditions;
    @Size(min=1, message="Specify the Progress Status")
    @NotNull(message="status value is Required")
    private String status;
    @Size(min=1, message="Specify the Infrastructure Aesthetics")
    @NotNull(message="aesthetics value is Required")
    private String aesthetics;
    private String reportName;
    private String ImageName;

    @Transient
    @NotNull(message="Enter the Start date")
    private String sdate;

    @Transient
    @NotNull(message="Enter the current date")
    private String cdate;
//    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE,
//            CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinColumn(name="project_manager_id")
   // private ProjectManager projectManager;

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name="inspector_id")
    private User user;

    public Project() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Project(String pname, String location, Date start_date, Date theCurrent_date, String projectType, String safety, String weatherConditions, String status, String aesthetics, String reportName, String imageName) {
        this.pname = pname;
        this.location = location;
        this.start_date = start_date;
        this.theCurrent_date = theCurrent_date;
        this.projectType = projectType;
        this.safety = safety;
        this.weatherConditions = weatherConditions;
        this.status = status;
        this.aesthetics = aesthetics;
        this.reportName = reportName;
        ImageName = imageName;
    }

    public Project(String pname, String location, Date start_date, Date theCurrent_date, String projectType, String safety, String weatherConditions, String status, String aesthetics) {
        this.pname = pname;
        this.location = location;
        this.start_date = start_date;
        this.theCurrent_date = theCurrent_date;
        this.projectType = projectType;
        this.safety = safety;
        this.weatherConditions = weatherConditions;
        this.status = status;
        this.aesthetics = aesthetics;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

//    public LocalDate getEnd_date() {
//        return end_date;
//    }
//
//    public void setEnd_date(LocalDate end_date) {
//        this.end_date = end_date;
//    }


    public Date getTheCurrent_date() {
        return theCurrent_date;
    }

    public void setTheCurrent_date(Date theCurrent_date) {
        this.theCurrent_date = theCurrent_date;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
    }

    public String getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAesthetics() {
        return aesthetics;
    }

    public void setAesthetics(String aesthetics) {
        this.aesthetics = aesthetics;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }



    @Override
    public String toString() {
        return "Project{" +
                "pname='" + pname + '\'' +
                ", location='" + location + '\'' +
                ", projectType='" + projectType + '\'' +
                ", safety='" + safety + '\'' +
                ", weatherConditions='" + weatherConditions + '\'' +
                ", status='" + status + '\'' +
                ", aesthetics='" + aesthetics + '\'' +

                ", sdate='" + sdate + '\'' +
                ", cdate='" + cdate + '\'' +
                '}';
    }
}
