package divademo.divademo.model;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="EMPLOYEES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeesEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name="EMP_NAME")
    private String emp_name;

    @Column(name="GENDER")
    private String gender;
}
