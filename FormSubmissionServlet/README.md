# Form Submission Servlet

This project demonstrates a simple web application that uses a servlet to process a form submission. It includes an HTML form that sends data to the servlet, which then outputs the form data to the response.

## Prerequisites

- Java 1.8 or higher
- Maven 3.0 or higher

## Usage

1. Clone or download the project to your local machine.
2. Open a terminal/command prompt and navigate to the project directory.
3. Run the command `mvn clean package` to build the project and generate the WAR file.
4. Deploy the WAR file to a Tomcat server or any other web server that supports WAR deployment.
5. Access the web application by navigating to `http://localhost:<port>/<context-root>/index.html` in a web browser, where `<port>` is the port number on which the server is running, and `<context-root>` is the context root of the application (usually the same as the WAR file name without the `.war` extension).

## Project Structure

- `pom.xml`: Maven project configuration file.
- `src/main/java/com/example/formsubmissionservlet/FormSubmissionServlet.java`: Servlet class that handles the form submission.
- `src/main/webapp/index.html`: HTML form that sends data to the servlet.
- `src/main/webapp/WEB-INF/web.xml`: Deployment descriptor that maps the servlet to a URL pattern and sets its initialization parameters.


TODO
----
Fungerar som den ska --> DOck en kopia av Pierres exempel. 
Kanske göra om lite...

Borde även denna kunna göras endast med JSP? 
