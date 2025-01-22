# INSTALLATION GUIDE

## Backend

**Spring Boot 3**

- Database: PostgreSQL
- Migration: Flyway
- Mapper/ORM: MyBatis

---

### Prepare Database

Create a database on a server, then take the database URL, username, and password. These will be set during the environment variables setup.

---

### Environment Variables Setup

We need to set up environment variables in two files:

1. **In the `pom.xml` for migration**

   ```xml
   <build>
       <plugins>
           <plugin>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-maven-plugin</artifactId>
           </plugin>

           <plugin>
               <groupId>org.flywaydb</groupId>
               <artifactId>flyway-maven-plugin</artifactId>
               <version>10.20.1</version>
               <configuration>
                   <url>${MRMS_API_DBURL}</url>
                   <user>${MRMS_API_DBUSERNAME}</user>
                   <password>${MRMS_API_DBPASSWORD}</password>
                   <locations>
                       <location>filesystem:src/main/resources/db/migration</location>
                   </locations>
               </configuration>
           </plugin>
       </plugins>
   </build>
   ```

2. **In src/main/resources/application.properties**

   ```xml
   spring.application.name=mrms_api
    spring.profiles.active=dev # Activate the 'dev' profile

    spring.jpa.hibernate.ddl-auto=false
    spring.flyway.enabled=false
    spring.flyway.locations=classpath:db/migration

    spring.datasource.url=${MRMS_API_DBURL}
    spring.datasource.username=${MRMS_API_DBUSERNAME}
    spring.datasource.password=${MRMS_API_DBPASSWORD}

    debug=true

   ```

### Migrating The Database

```bash
    mvn flyway:migrate
```

### Migrating The Database

After verifying everything runs perfectly, you can start the application.
