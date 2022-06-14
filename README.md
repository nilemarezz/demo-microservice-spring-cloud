## Microservices with Java Spring Boot and Spring Cloud

![](https://www.img.in.th/images/2c83c38f459b9520ff1ed4753dcea737.png)

[Course Detail](https://www.udemy.com/course/microservices-with-java-spring-boot-spring-cloud-eureka-api-gateway/)

### Spring Cloud Project
- Spring Cloud Open Feign - Communicate with other services via http
- Spring Cloud Netflix Eureka - Service Discovery & Registry 
- Spring Cloud Load Balancer - Balance the request when come to service
- Spring Cloud API Gateway - Entry point when request come to our microservice (Authentication , Logging)
- Fault Tolerance - When some service down , it should not impact to another service
- Sleuth and Zipkin - can track the request from one to another services
- Config server - Centralize the all config of the services 

### Structure

![](https://www.img.in.th/images/ea7fcf2daceaf609c7c00a598c95e1b7.png)
![](https://www.img.in.th/images/f1b848d846b9965c01c8e605c5077fe5.png)
![](https://www.img.in.th/images/d95464d7ecbadafb67a853c1abca92fe.png)


### 1. Eureka Server - [eureka-server](https://github.com/nilemarezz/udemy-study/tree/master/spring/microservice-with-springboot/eureka-server)
- เป็น springboot application
- Service Discovery & Registry (default port - 8762)
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

```properties
server.port = 8761
spring.application.name=eureka-server
eureka.client.register-with-eureka=false // ไม่ register ตัวเอง
eureka.client.fetch-registry=false // ไม่ fetch ตัวเอง
```

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}
}
```

### 2. Config Server - [config-server](https://github.com/nilemarezz/udemy-study/tree/master/spring/microservice-with-springboot/config-server)
- เป็น springboot application
- เป็น service ที่ provide config ให้ service อื่นๆ (Centralize)
- register server กับ Eureka Server
- สร้าง [application.properties](https://github.com/nilemarezz/udemy-study/blob/master/spring/microservice-with-springboot/application.properties) ไว้บน git แล้วให้ server ไปอ่าน
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-config-server</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
```java
@SpringBootApplication
@EnableEurekaClient
@EnableConfigServer
public class ConfigServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}
}
```

```properties
server.port=8888
spring.application.name=config-server
# สำหรับ regiseter กับ Eureka server
eureka.client.service-url.defaultZone= http://localhost:8761/eureka
# สำหรับ อ่าน config file
spring.cloud.config.server.git.uri=https://github.com/nilemarezz/udemy-study
spring.cloud.config.server.git.search-paths=spring/microservice-with-springboot
spring.cloud.config.server.git.username=nilemarezz
spring.cloud.config.server.git.password=<password>
```

### 3. Sleuth & Zipkin
- Sleuth - เอาไว้สร้าง Track ID เมื่อมีการ request , response ระหว่าง Service (add dependency ในแต่ละ service)
- Zipkin 
  - เป็น jar file หรือ docker (default port : 9411) 
  - เป็น Dashboard เพื่อแสดงว่า Request วิ่งไป service ไหนบ้าง (รัน [zipkin.jar](https://github.com/nilemarezz/udemy-study/tree/master/spring/microservice-with-springboot))
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
```

![](https://www.img.in.th/images/0e4edeaf2929320816a2e5fe558318b8.png)


### 4. API Gateway - [api-gateway](https://github.com/nilemarezz/udemy-study/tree/master/spring/microservice-with-springboot/api-gateway)
- เป็น springboot application
- เป็นเหมือนประตู ที่ทุก request,response จะวิ่งผ่านที่นี้
- ทำ load balancer ในตัว
- ต้อง register กับ eureka
- ทำ authentication หรือ request,response filter ที่นี้
- เวลาเรียก service จะเป็น http://localhost:9090/{ขื่อservice} 

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>

// สำหรับ regiseter กับ Eureka server
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

// สำหรับ zipkin และ sleute
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
```
```properties
server.port=9090
spring.application.name=api-gateway
# สำหรับ regiseter กับ Eureka server
eureka.client.service-url.defaultZone= http://localhost:8761/eureka
# สำหรับ config gateway 
spring.cloud.gateway.discovery.locator.enabled=true
spring.cloud.gateway.discovery.locator.lower-case-service-id=true
# สำหรับ zipkin และ sleute
spring.sleuth.reactor.instrumentation-type=decorate_on_each
spring.zipkin.base-url=http://localhost:9411
```

### 5. Address Service - [address-service](https://github.com/nilemarezz/udemy-study/tree/master/spring/microservice-with-springboot/address-service)
#### 5.1 Setting Config
   1. เปลี่ยน จาก application.properties เป็น bootstrap.properties
   2. โหลด config จาก config server [application.properties](https://github.com/nilemarezz/udemy-study/blob/master/spring/microservice-with-springboot/application.properties)
```properties
spring.application.name=address-service
server.port=8082
# โหลด config จาก config server
spring.cloud.config.enabled=true
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.service-id=config-server
spring.config.import=optional:configserver:
```
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```
#### 5.2 เชื่อมต่อ Sleuth & Zipkin
   1. จะอ่านค่า config จาก config server เรื่อง zipkin อยู่แล้ว
```properties
# อยู่ใน config server อยู่แล้ว
# spring.zipkin.base-url=http://localhost:9411
```
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```
#### 5.3 Register Service กับ Eureka Server
- อยู่ใน config server อยู่แล้ว
```properties
# eureka.client.service-url.defaultZone= http://localhost:8761/eureka
```
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
#### 5.4 เขียน api เพื่อ getAddressByUserID
```
GET http://localhost:9090/address-service/api/address/1

{
 address_id : 1,
 city : Delhi,
 street : Happy Street
}
```

### 6. Student Service [student-service](https://github.com/nilemarezz/udemy-study/tree/master/spring/microservice-with-springboot/student-service)
- มีการเชื่อมต่อกับ Address Service เพื่อเอาข้อมูลที่อยู่มาใช้
#### 6.1 Setting Config
  1. เปลี่ยน จาก application.properties เป็น bootstrap.properties
  2. โหลด config จาก config server [application.properties](https://github.com/nilemarezz/udemy-study/blob/master/spring/microservice-with-springboot/application.properties)
```properties
spring.application.name=student-service
server.port=8084
# โหลด config จาก config server
spring.cloud.config.enabled=true
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.service-id=config-server
spring.config.import=optional:configserver:
```
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```
#### 6.2 เชื่อมต่อ Sleuth & Zipkin
1. จะอ่านค่า config จาก config server เรื่อง zipkin อยู่แล้ว
```properties
# อยู่ใน config server อยู่แล้ว
# spring.zipkin.base-url=http://localhost:9411
```
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```
#### 6.3 Register Service กับ Eureka Server
- อยู่ใน config server อยู่แล้ว
```properties
# eureka.client.service-url.defaultZone= http://localhost:8761/eureka
```
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
#### 6.4 สร้าง AddressFeignClient เพื่อเรียก Api Address โดยใช้ Openfeign
- value = "api-gateway" คือ เรียกผ่าน gateway
- address ที่เรียกจะเป็น http://localhost:9090/address-service/api/address/{id}

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

```java
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class StudentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentServiceApplication.class, args);
    }
}
```
[AddressFeignClient.java](https://github.com/nilemarezz/udemy-study/blob/master/spring/microservice-with-springboot/student-service/src/main/java/com/example/studentservice/feignclients/AddressFeignClient.java)
```java
@FeignClient( value = "api-gateway")
public interface AddressFeignClient {
    @GetMapping("/address-service/api/address/{id}")
    public AddressDTO getAddressById(@PathVariable(name = "id") int id);
}
```
#### 6.5 สร้าง AddressService เพื่อเรียก AddressFeignClient และใช้ Circuit breaker (resilience4j)
- Circuit breaker ใช้เมื่อเชื่อมต้อกับ service อื่น เพื่อลดปัญหาที่เกิดขึ้นจากการที่ service ปลายทาง DOWN
- ใช้คู่กับ AOP และ Actuator
- Setting ต่างๆเกี่ยวกับ Circuit Breaker
  - sliding-window-size - กี่ request ล่าสุดที่เราจะเอามาคำนวน  <br /> (เช่น มี request 1000 รายการ , window-size=100 , นำ request ที่ 900 - 1000 มาคำนวน)
  - failure-rate-threshold - เปอร์เซ็นการ fail จาก request ทีเรานำมาคำนวนจาก window-size ถ้าเกินเปอร์เซ็นที่กำหนด จะ break ทันที  <br />
  (เช่น failure-rate-threshold=50, จาก request ที่ 900 - 1000 ถ้า fail เกิน 50% จะ break request ที่ไป service ปลายทาง (DOWN) )
  - wait-duration-in-open-state - เวลาที่จะหยุด break (ms)
  - automatic-transition-from-open-to-half-open-enabled - เมื่อถึงเวลาที่ wait-duration-in-open-state กำหนด จะเปลี่ยนสถานะเป็น กึ่งเปิดกึ่งปิด(UNKNOW)
  - permitted-number-of-calls-in-half-open-state - จำนวน request ที่ success แล้วจะกลับขึ้นมา (UP) <br />
    (เช่นเมื่อเข้าสู่สถานะ กึ่งเปิดกึ่งปิด(UNKNOW) ถ้า 5 request นั้นสำเร็จจะเปลี่ยนเป็น UP  แต่ถ้าไม่สำเร็จ จะเปลี่ยนเป็น DOWN แล้ววนซ้ำหลือนเดิม)
- fallbackMethod คือเมื่อ service ปลายทาง fail , จะเรียกใช้ method นี้แทน

`addressService ใน config จะต้องตรงกับ  @CircuitBreaker(name = "addressService",...) `
```properties
# circuit breaker config
resilience4j.circuitbreaker.instances.addressService.sliding-window-size=10
resilience4j.circuitbreaker.instances.addressService.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.addressService.wait-duration-in-open-state=30000
resilience4j.circuitbreaker.instances.addressService.automatic-transition-from-open-to-half-open-enabled=true
resilience4j.circuitbreaker.instances.addressService.permitted-number-of-calls-in-half-open-state=5
# actuator check circuit
resilience4j.circuitbreaker.instances.addressService.allow-health-indicator-to-fail=true
resilience4j.circuitbreaker.instances.addressService.register-health-indicator=true
management.health.circuitbreakers.enabled=true
management.endpoints.web.exposure.include=health
management.endpoint.health.show-details=always
```
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
[AddressService.java](https://github.com/nilemarezz/udemy-study/blob/master/spring/microservice-with-springboot/student-service/src/main/java/com/example/studentservice/services/AddressService.java)
```java
@Service
public class AddressService {

    Logger logger = LoggerFactory.getLogger(AddressService.class);
    int i = 1;
    
    @Autowired
    private AddressFeignClient addressFeignClient;

    @CircuitBreaker(name = "addressService",fallbackMethod = "fallbackGetAddressById")
    public AddressDTO getAddressById(int id){
        logger.info("Count : " + i);
        i++;
        return addressFeignClient.getAddressById(id);
    }

    public AddressDTO fallbackGetAddressById(int id , Throwable th){
        logger.error("Error : " + th);
        return new AddressDTO();
    }
}
```
#### 6.6 สร้าง API Student 
```
GET http://localhost:9090/student-service/api/student/1
{
  id:1,
  first_name : Raj ,
  last_name : Dave,
  email : rej_dave@yahoo.com , 
  address : {
    address_id : 1,
    city : Delhi,
    street : Happy Street
  }
}
```
