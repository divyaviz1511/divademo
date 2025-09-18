## About this Repository
Holds Mini Tasks/Assignments to help track Java Spring Boot Learnings. 
## SetUp 
*	Installed JDK 21 (can check using javac –version)
*	Install Maven (It’s a tool developed by Apache and it is used for building and managing dependencies for Java-based projects.).We modify PATH of MAC and include the directory of Maven.
	* PATH acts as a base reference for the system to find the executable. When we add that bin directory to /etc/paths file, then the system knows to find that executable in bin directory. Apache is a web server that delivers web content thru the internet.
* Spring Initializer

  Go to https://start.spring.io
  Choose Maven, Java, Spring Boot 3.5.6, Provide name and package name, choose Packaging jar and Java 17. Click Generate.
  This will generate a ZIP file that you can unZIP import into your favorite IDE and start building.
  You can also do this process from you IDE as well. I used VSCode.
* The Application open with 8080 as default port. We can configure this to anything else using application.properties file.

## What the Repo covers and the corresponding learnings
This section is a quick overview of the concepts learnt, built, executed in this project along with more deeper learnings. I have documented this here for future reference as well as to give an idea of the concepts I gained familiarity with. 
### pom.xml file
This file is used by Maven. This file defines 3rd party libraries, versions, configurations, plugins etc.. Common dependencies include :
 * spring-boot-started-web : Build Rest APIs
 * spring-boot-starter-data-jpa : work with DB using JPA
 * spring-boot-starter-security: For Spring Security features
 * spring-boot-starter-thymeleaf : Rendering the HTML view
Leverage your IDE built-in features to run maven so it picks the changes we made to this file.

### application.properties
It is a runtime config. It sets for example the server port, DB credentials etc.. basically the app settings. You can set DB settings, loggin level settings, Thymeleaf settings etc…
We can also specify these settings the YAML way that could be more readable and clean. But they both do exactly the same job. 

We can create multiple property files for different environments.(dev, test, prod, stage etc..)
And we can activate whichever file we want to apply by specifying the following entry in the original properties/yaml file.
	
 
 `spring.profile.active = dev` (application-dev.yaml) 
 or 
	`java -jar app.jar --spring.profiles.active=dev`


Spring merges properties values in profile-specific files override values in the base file.

### Spring Beans
It is just an object that is managed by the Spring Container. Once the class is registered as bean, it can do the following anytime/anywhere:
* Instantiate it
* Inject it 
* Control its lifecycle


Usually @Component, @Service, @Repository etc are beans. This means that Spring can register them and use/inject them as needed (via @Autowired or constructor injection ) when it scans.

Example 1: 

```
@Component
public class MyBean {
    public void doSomething() {
        System.out.println("Doing something...");
    }
}

@Service 
public class MyService(){
	public String getService(){
		return “Serviced”;
	}
}

public class MyController{
	private final MyService myservice;
	
	//Contructor Injection
	public MyController(MyService myservice){
		this.myservice = myservice;
	}

	@Autowired //Field Injection – not recommended not very clear why.
	private MyBean mybean;

	//or we can do through Service Injection
	@Autowired
	public void setMyService(MyService myService) {
  		this.myService = myService;
	}

	//Do something later with mybean.
}
```

We can also custom create our own Beans and we annotate with @Bean

`@Bean :`  This tells Spring that create object of this class, manage it and make it available for injections.

### Spring Boot AutoConfiguration 
Spring Boot automatically configures your application based on dependencies present in your classpath. It dynamically scans configuration classes and loads them into the application.
* `@SpringBootApplication = @EnableAutoConfiguration + @ComponentScan + @Configuration`
* `@ComponentScan` : It allows Spring to automatically discover and register beans in the current package and sub-packages
* `@EnableAutoConfiguration` : Automatically configures our application 

### Data/Model Layer of Spring Boot
* `@Entity` - It tells that this Java class needs to be mapped to a database table. Optionally creates tables (if configured with spring.jpa.hibernate.ddl-auto=create/update) in the application.properties file.
* `@Table` - You can specify the name of the table like this `(name=”users”)` if Entity Class name is different from table name (not case-sensitive)
* `@Id` - Marks the Primary key of the table.
* `@GeneratedValue(strategy = …) ` - autoincrements IDs or autogenerates etc..
* `@Column(name = “”)` - specify columns of table.

**Lombok : Java library that auto-generates common code at compile time. Its a code shrinker for getters, setters, constructors, toString(), logging etc..**

Lombok is used in services where **@AutoWired** is not needed to inject through constructor. Similarly in Controllers as well.

* `@NoArgsContructor ` : creates constructor with no args
* `@AllAgrsConstructor ` : creates constructor with all the class variables specified
* `@Data` : can be used to replace all of the above annotations.

### Service Layer of Spring Boot

This is where business logic resides. Like insert/update/delete/retrieve data. Below is the usual work-flow
* Define Entity (that maps to the table)
* Create interface repository that **extends JpaRepository**

  Here we need to pass two things to Java generics **JpaRepository<T, ID>**. The 1st one is the Table Entity and the 2nd one is the primary key type. This MUST match
  whatever type you annotated your primary field as @Id. If it doesn’t match, it throws out an error.

  If we have **composite primary key** then, there are two ways to handle that.
  * Create a composite Key Class that has variables of our two/three composite primary fields. And this must be annotated with **@Embeddable, Serializable and override hashCode() and equals()**

    Example

    ```
    @Embeddable
	public class UserId implements Serializable {
 		private String email;
    private String countryCode;
	}
  
	public class User {
	@EmbeddedId
	private UserId userid;
  
	}

	public interface UserRepository extends JpaRepositoy<User, UserId> {
    }
    ```

    Now that you have created your repository, you can start using save(), findById() So no need to write any further logic here.

* Create Service Class and call the repo methods to perform DB operations.

  ```
  @Autowired
  private UserRepository userRepository;

  public void createUser(User user){
	userRepository.save(user); //use findAll(), findById(id),
  }

  ```

  We didn’t implement and save() function yet it gets saved and that’s because **Spring Data JPA dynamically creates the class at runtime** that implements all the methods (save, findById, delete, etc.) **based on the generic types** that we passed. 
So under the hood, there is somebody called **EntityManager**. He is the guy that interacts **with DB via Hibernate** usually. It uses Hibernate to generate correct SQL statements. **Hibernate handles object-to-db mapping and JDBC is used to run the queries**.


 The above is for single create/update save. It is more efficient to do batch saves. For this, Hibernate provides saveAll(). And we can specify in our application.properties the size of this batch. We can also use entitymanager to flush & clear. We can persists every say User create/update and we can flush them in batches. entityManager.persist(), 


So Thumb of Rule :
* Use Repos when you are adding one user, retrieving list of users etc…
* Use Entity Manager when you want to do batch inserts/updates etc.. or have complex queries for optimization purpose

### Rest Controller (API)
* `@RequestMapping(“/api/users”) ` - this is more like a base url for api call.
* `@PostMapping` - where we pass `@RequestBody` (what happens behind the hoods with this is that, it converts the incoming HTTP request body into Java object. It uses some HTTP converters for that)
* `@GetMapping (“/{id}”)` - for HTTP GET
* `@RequestParam` – To get the query parameters.

  Eg:

  ```
  @GetMapping ("/users/search")
  public ResponseEntity<?> searchUsers(@RequestParam String name, @RequestParam(required = False) Integer. age ) {
  }
  Request URL would be like : …./users/search?name=”divya”&age=1
  ```

  `ResponseEntity<User> response = ResponseEntity.ok(user);` - Returns HTTP 200 with the user object in the body.

  `ResponseEntity.noContent().build();` - Returns HTTP 204 No Content

  
* `@PutMapping (“/{id}”)` - we pass `@PathVariable` (Getting part of the path) and @RequestBody

  Eg:

  ```
  @PutMapping(“/users/{id}”)
  public void updateUsers(@PathVariable UUID id, @RequestBody User user) {
	//get the user using the id. 
	//update this record with the passed user object data. 
	//then save or persist or whatever.
  }
  ```

### Data Transfer Object (Ongoing)

To Transfer data to and from the Rest APIs, we define DTOs  instead of using Java JPA entities directly (so that sensitive data may not be exposed).  We need DTOs especially when there is requestBody that requires validation.

We can also have one request body that can be mapped to multiple entities. In this case, we create nested DTOs. And within the service layer, we can extract data and put them in the corresponding table.

```
class UserDTO {
	private String name;
	private String email;
	private AddressDTO address;
}

```

### ThymeLeaf

Its used for rendering HTML in Spring boot web applications. It lets us create dynamic HTML pages that can interact with your backend Java/Spring code. 
`th:each for looping over, th:if/th:else for hiding parts of the page, th:text=${variable}`

eg: `<span th:text="${user.name}">User</span>` 

user.name comes from the Java controller, which passes data to the HTML via the Model object in Spring MVC. And this is how it is passed in the controller -

```
User user = userService.getUserObject();
model.addAttribute(“user”, user); //Basically user object is assigned to the model attribute user 
return "view_name"
```

### Spring Security
* Add dependency `spring-boot-starter-security` to the pom.xml file.
* Default security (when no custom security is defined). When spring-boot-starter-security is added but there is no custom security defined, it tells Spring Boot to enable custom security instead of default security (form login + basic auth). Default Login will be a simple user (user by default) and random generate password that needs to be taken from the console.
* Custom Security  - We annotate with **@EnableWebSecurity** and configure how the security filters should behave using **securityFilterChain**.

  `http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());` -> Allow all access

  `http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());` -> Means ask for authentication for any/all requests.

  `http.csrf().disable()` -> for stateless APIs we can disable it. Stateless APIs (resend the authentication and server information with every requests)  Vs Stateful APIs (stored the information)

  *CSRF – it is a security attack where hackers tricks user’s browser into sending unwanted requested to the site where they are authenticated. It requires two things for the attack to happen, the user is logged in & their authentication info is stored in cookie. And browser sends that cookie with every action without user’s action. Since Stateless API doesn’t really stored user sessions, CSRF can be disabled. You need CSRF token only if API uses session cookie for authentication*

* Define multiple Security configuration. One for each URLs

  `.requestMatchers("/admin/reports/**").hasRole("MANAGER")` - this means that we allow the authenticated user having role Manager to access this URL.

  ```
  http
  .securityMatcher("/api/**") // chain applies to /api/**
  .authorizeHttpRequests(auth -> auth
   .requestMatchers(HttpMethod.GET, "/api/public").permitAll()
   .requestMatchers("/api/admin/**").hasRole("ADMIN")
   .anyRequest().authenticated()
  );
  .formLogin()
  ```

  When we hit login page, The Spring Security looks up a **UserDetailsService** bean. We can use in-memory for demo/test purpose which is what we used in this repo, but we can also fetch from DB. And the below code has a layout for that.

  ```
  MyUserDetailsService that extends UserDetailsService {
	//In here, we use userRepo to findall the users 
	//We also override the function loadUserByUsername(String username). Find this guy if he exists, get all his info and return it.
  }
  ```

  Guess after that Spring will kind of internally check the passwd from DB and the user typed passwd to check if they are the same using password encoder.
  **Spring Boot (via Spring Security) uses BCrypt by default to hash passwords.** OAuth is delegating the login process to the trusted provider like Google, FB, linkedin etc.. In code, instead of `.formLogin()`, we call `.oauth2Login();`

### Configuration (Again not Covered in the repo. Educational learning)
* `@Configuration ` - It tells Spring that this class contains beans that needs managing.
* **Conditional Loading** - Spring boot uses conditional annotations to load the Beans only when a certain conditions are met.
  	* `@ConditionalOnClass` - Only load when a certain class is on the classpath.
  	* `@ConditionalOnMissingBean` -  Used if another bean is not defined already. Used when you want to offer a default.
  	* `@ConditionalOnProperty` - Only load when the property exists and matches the value. Used for Feature toggle.

  	  Eg: `@ConditionalOnProperty(name = "feature.x.enabled", havingValue = "true")`



### Actuators 
* Add the `spring-boot-starter-actuator` dependency to your project.
* Production ready endpoints to monitor and manage your app. They are secured by default, so if you want to expose them, you will need to configure your application.properties as to which endpoints you want to particularly expose.
* These endpoints will hold sensitive data so its good idea to secure them. Add them to SecutyFilterChain we created above. Attach them with role permissions etc..

   ```
   .requestMatchers("/actuator/health", "/actuator/info").permitAll()
   .requestMatchers("/actuator/**").hasRole("ADMIN")
   ```
* You can create custom endpoints as well by defining a new class and annotating it with @Component, @Endpoint, @ReadOperation.

### RabbitMQ (Ongoing)
* Message Broker. Different part of the application communicate with one another by sending messages through queues.
* A Real good example that helped me understand is : Say New User Registration, app needs to send out welcome email.

  `Traditional workflow : UserController -> EmailService.sendEmail(….)`
  Problem is that user registration will end up failing if (for some reason) sendEmail fails. As sendEmail marks the completion of user registration. But we dont want that, we want to be able to send the email later and make through user registration 

  So the idea is to use RabbitMQ here.
  `UserController -> sends the message to send email to this user -> sits in the queue -> EmailService picks it up`
* RabbitMQ uses AMQP protocol
* Work-flow :-
  	* Add RabbitMQ dependency spring-boot-starter-amqp
  	* Configure and run RabbitMQ with the port credentials etc..
  	* Configure Spring boot application.properties with Rabbit port, username, pwd etc..
  	* Declare Queue and an Exchange and Bind them with routing key all this in a class say RabbitMQConfigClass

  	  Exchange : help route the messages to the right queue. We bind queue, exachange with a routing key

  	  `BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);`
  
  	* Next we create Message Producer - This guy will send message using **Exchange_name**,**routing key** and **injecting AMQPTemplate amqpTemplate** that helps convert and send.

  	  ```
  	  public void sendEmail(EmailRequest emailRequest) {
  	  	amqpTemplate.convertAndSend("emailExchange","email.send",emailRequest);
  	  }
  	  ```
  	* Next we create consumer side - Who will now handle this message from the queue.

 
  	  ```
  	  @RabbitListener(queue= RabbitMQClass.QUEUE_NAME)
  	  public void handleEmail(EmailRequest emailRequest) {
  	  	//call EmailService.sendEmail(emailRequest) or whatever.
  	  }
  	  ```

     * If you want RabbitMQ to treat the queue as a priority queue, you pass special arguments when defining the queue.

       ```
       @Bean
       public Queue priorityQueue() {
       	return QueueBuilder.durable("priority-email-queue")
        .withArgument("x-max-priority", 10) // this is the key!
        .build();
       }
       ```
  * ✅ Real-World Use Cases for RabbitMQ
    
    	1. Email/SMS Notifications
    	•	Use Case: User signs up, gets welcome email/SMS
    	•	Why? Sending emails synchronously slows down UX and adds failure risk.
    	•	How RabbitMQ helps: Decouples the request flow from actual email processing.
 
	```2. User Activity Logging / Auditing
  	•	Use Case: Track user actions (login, file upload, API usage).
	•	Why? Don’t want logging logic to slow down business logic.
	•	How RabbitMQ helps: Push logs into a queue; process them later into DB, Elasticsearch, or files.```

  
 
	```3. Image or File Processing
	•	Use Case: A user uploads a file/image/video; it must be resized, scanned, or converted.
	•	Why? These are heavy processes that shouldn't run in the main request thread.
	•	RabbitMQ: Queue the processing tasks and handle them in background workers.```

  
 
	```4. Database Synchronization Between Services
	•	Use Case: Multiple microservices have their own databases. When one updates a record, others need to stay in sync.
	•	Example: UserService updates email, OrderService must update cached email.
	•	RabbitMQ: Publish update events, other services subscribe and update their data.```

  
 
	```5. Payment Processing
	•	Use Case: Handle success/failure, retries, and audit logs for payments.
	•	RabbitMQ: Queue payment requests; safely retry failed ones; avoid double-charging.```

  
 
	```6. Bulk Inserts or Updates (Batch Jobs)
	•	Use Case: You want to insert or update millions of record
	•	RabbitMQ helps: Break work into chunks, distribute it over consumers (workers) to avoid DB spikes.```

  ##THANK YOU
		


  	  

  	  
  	  


 



