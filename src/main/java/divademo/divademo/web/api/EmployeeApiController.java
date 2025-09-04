package divademo.divademo.web.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import divademo.divademo.service.EmployeeService;
import divademo.divademo.web.model.Employee;

@RestController
@RequestMapping("/api/employees")
public class EmployeeApiController {

    private final EmployeeService employeeService;

    public EmployeeApiController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("")
    public List<Employee> getAllEmployees() {
        return this.employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable(name="id") UUID id) {
        return this.employeeService.getEmployeeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee addEmployee(@RequestBody Employee employee) {
        return this.employeeService.addEmployee(employee);
    }

    @PutMapping("/{id}")
    public Employee updatEmployee(@PathVariable(name="id") UUID id, @RequestBody Employee employee) {
        return this.employeeService.updatEmployee(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable(name="id") UUID id) {
        this.employeeService.deleteEmployee(id);
    }
    
}
