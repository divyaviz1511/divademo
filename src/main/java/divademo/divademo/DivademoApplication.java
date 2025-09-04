package divademo.divademo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import divademo.divademo.repository.EmployeesRepository;

@SpringBootApplication
public class DivademoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DivademoApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(EmployeesRepository employeesRepository) {
		System.out.println("Hello");
		return args -> {
			employeesRepository.findAll().forEach(System.out::println);
		};
	}

}
