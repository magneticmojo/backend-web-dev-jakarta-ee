# Environment Variables Servlet

This project is a simple web application that displays various environment variables and request headers information using a servlet.

## Project Structure

The project contains the following files:

1. `EnvVarsServlet.java`: A servlet class that fetches and displays environment variables and request headers.
2. `web.xml`: The web application deployment descriptor which defines the servlet and servlet-mapping.
3. `index.jsp`: A JSP file that includes the servlet output.
4. `pom.xml`: The Maven build file that manages project dependencies, packaging, and build configurations.

## Prerequisites

To run the project, you will need:

1. JDK 1.8 or later installed
2. Maven installed
3. A servlet container such as Apache Tomcat

## Building and Running the Project

1. Clone the repository or download the project files.
2. Navigate to the project's root directory using the terminal.
3. Run `mvn clean package` to build the project and create a `.war` file in the `target` directory.
4. Deploy the `.war` file to your servlet container (e.g., Apache Tomcat).
5. Access the application by navigating to `http://localhost:<port>/EnvironmentVarsServlet_war_exploded/` in your web browser (replace `<port>` with the appropriate port number of your servlet container).

The application will display various environment variables and request headers information.

TODO
----

Rensa upp bland de olika variablerna. Många dubbletter.

--> Börja med att skapa en ny branch --> Rensa upp i master och pusha upp den till github. 

ServletException???