package divademo.divademo.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import divademo.divademo.model.EmployeesEntity;
import divademo.divademo.web.model.Employee;
import divademo.divademo.repository.EmployeesRepository;
import divademo.divademo.service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeWebController {
    /* //Below lines to o/p to the view the DB data
    private final EmployeesRepository employeesRepository;

    public EmployeeWebController(EmployeesRepository employeesRepository) {
        this.employeesRepository = employeesRepository;
    }*/

    //Below lines REST API web service
    private final EmployeeService employeeService;
    public EmployeeWebController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    @GetMapping
    public String getEmployeePage(Model model) { // returns the name of the view 
        /*List<EmployeesEntity> empEntities = this.employeesRepository.findAll();
        List<Employee> empList = new ArrayList<>();
        empEntities.forEach(e-> empList.add(new Employee(e.getId(), e.getEmp_name(), e.getGender())));*/

        //Instead of the above lines that gets from the repo, we use the webservices we created

        model.addAttribute("emp", employeeService.getAllEmployees());
        return "emp";
    }
}
