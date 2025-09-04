package divademo.divademo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import divademo.divademo.model.EmployeesEntity;
import divademo.divademo.repository.EmployeesRepository;
import divademo.divademo.web.model.Employee;

@Service
public class EmployeeService {
    private final EmployeesRepository employeesRepository;

    public EmployeeService(EmployeesRepository employeesRepository) {
        this.employeesRepository = employeesRepository;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        List<EmployeesEntity> employeesEntities = this.employeesRepository.findAll();
        for (EmployeesEntity employeesEntity : employeesEntities) {
            employees.add(this.getEmployeeFromEmployeeEntity(employeesEntity));
        }

        return employees;
    }

    public Employee getEmployeeById(UUID id) {
        Optional<EmployeesEntity> employeesEntity = this.employeesRepository.findById(id);
        if (employeesEntity.isEmpty()) return null;
        else return this.getEmployeeFromEmployeeEntity(employeesEntity.get());
    }

    public Employee addEmployee(Employee employee){
        EmployeesEntity employeesEntity = this.getEmployeesEntityFromEmployee(employee);
        employeesEntity = this.employeesRepository.save(employeesEntity);
        return this.getEmployeeFromEmployeeEntity(employeesEntity);
    }

    public Employee updatEmployee(Employee employee) {
        EmployeesEntity employeesEntity = this.getEmployeesEntityFromEmployee(employee);
        employeesEntity = this.employeesRepository.save(employeesEntity);
        return this.getEmployeeFromEmployeeEntity(employeesEntity);
    }

    public void deleteEmployee(UUID id) {
        this.employeesRepository.deleteById(id);
    }

    //Convert Employee to EmployeeEntity and vice verse

    private Employee getEmployeeFromEmployeeEntity(EmployeesEntity employeesEntity) {
        return new Employee(employeesEntity.getId(), employeesEntity.getEmp_name(), employeesEntity.getGender());
    }

    private EmployeesEntity getEmployeesEntityFromEmployee(Employee employee) {
        return new EmployeesEntity(employee.getId(), employee.getName(), employee.getGender());
    }
}
