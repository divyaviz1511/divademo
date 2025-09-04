package divademo.divademo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import divademo.divademo.model.EmployeesEntity;

public interface EmployeesRepository extends JpaRepository<EmployeesEntity, UUID> {
    
}
