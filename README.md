# 🍰 Enhanced Cake Manager Micro Service (Fictitious)

---

## 🔧 Changes

### 📦 Project Structural Reforms
1. Used **Spring Boot** for faster development and easy containerization, supporting cloud-native development. Offers broad support for open-source libraries.
2. Replaced `javax.persistence.*` with `jakarta.persistence.*`, as the former is unsupported in Spring 3.x.
3. Adopted **JpaRepository** to eliminate boilerplate DAO code.
4. Separated persistence (`entity`) and data transfer (`dto`) layers.
5. Refactored package structure—added `persistence`, `security`, `dto`, `service`, and grouped classes accordingly.

### 🔐 Security Additions
1. Implemented **authentication** and **role-based access** for APIs:
    - `Create`, `Update`, `Delete`: accessible by `ROLE_ADMIN`
    - `Get` APIs: accessible by both `ROLE_ADMIN` and `ROLE_CUSTOMER`
2. Enabled **SSL configuration** to enforce HTTPS protocol.
3. Added **XSS protection** at both parameter and JSON body levels.
4. Introduced a **global exception handler** to prevent internal exceptions from leaking to API consumers.
5. Changed the application context path from `/` to `/cakeshop` to reduce vulnerability to generic attacks on the root path.

### 📊 Monitoring Support
1. Added **logging** and **performance monitoring** using Spring AOP:
    - Logs method entry, parameters, exit, return values, and execution time for service and controller methods.

### ⚙️ CI/CD
1. Included a **Dockerfile** for containerization.
2. Set up **CI** using **GitHub Actions**.
3. Verified CI locally using [`act`](https://github.com/nektos/act) and Docker.
4. Wrote:
    - Unit and integration tests for the **service** layer.
    - **Controller-level integration tests** using **Postman Collection**, executed with `newman` CLI.
      > 📌 Requires Newman: `npm install -g newman`

### 📚 Documentation
1. Added **Swagger** documentation for all REST APIs.
2. Added comprehensive **Javadoc** to improve maintainability.

---

## ▶️ Actions

1. **Run Spring Boot App from Command Line**

   ```bash
   mvn clean install spring-boot:run
   ```

    - App context path: [https://localhost:9901/cakeshop](https://localhost:9901/cakeshop)

2. **Access Swagger UI Documentation**

   [https://localhost:9901/cakeshop/swagger-ui/index.html](https://localhost:9901/cakeshop/swagger-ui/index.html)

3. **Run E2E Postman Collection Test (Requires Newman)**

   ```bash
   newman run src/test/resources/CakeController.postman_collection.json \
     -e src/test/resources/CakeController.env.json --insecure
   ```

4. **Test CI Support Using GitHub Actions Locally (Requires `act`)**

   ```bash
   act push
   ```

---

## 🚀 Further Improvements

1. Integrate with a real database (e.g., **MySQL**) instead of in-memory persistence.
2. Replace in-memory user auth with **IAM-based user authentication** (e.g., OAuth2 or JWT).
3. Build a **frontend web UI** or **mobile app** to make the APIs user-friendly.
4. Extend the ER data model to include more features:
    - Entities like **Pricing**, **Cart**, **Order**, **Delivery**, etc.
    - Provide a more enriched and real-world commerce experience.

---
