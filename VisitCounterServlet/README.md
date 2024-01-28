Visit Counter Web Application
============================
This is a simple web application that keeps track of the number of visits made to the website. The application is developed using Jakarta EE 9, including Jakarta Servlets and Jakarta Server Pages (JSP) for creating dynamic web applications.

Prerequisites
-------------
JDK 17 or later installed
Apache Tomcat as the web server and servlet container
IntelliJ IDEA IDE for Java development
Maven for build automation and dependency management

Features
------------
Tracks the number of visits made to the website
Thread-safe visit count incrementing
Proper error handling for a robust web application

Setup
-------------
Clone the repository or download the source code to your local machine.
Open the project in IntelliJ IDEA and ensure that Maven is set up within the IDE for build automation and dependency management.
Configure your Tomcat server in IntelliJ IDEA to deploy the web application.
Deploy the web application to the Tomcat server and access the application through the appropriate context path.

Usage
-------------
Once the web application is deployed and running, access the website through a web browser. The visit count will be displayed on the webpage, incrementing with each refresh.

Project Structure
-----------------
src/main/java/com/example/visitcounterservlet/VisitCounterServlet.java: The servlet implementation that handles visit counting and file I/O.
src/main/webapp/index.jsp: The JSP file that displays the visit count on the webpage.
src/main/webapp/WEB-INF/web.xml: The web deployment descriptor file that provides configuration information for the web application.
git 

Paths
------
/visit-counter-servlet: The context path for the web application.

/visit-counter-servlet/visit-counter: The servlet path for the VisitCounterServlet servlet.

/visit-counter-servlet/index.jsp: The JSP file that displays the visit count on the webpage.

Visit count file: /opt/homebrew/Cellar/tomcat/10.1.8/libexec/bin/visit_count.txt

Later Updates
-------------

unit tests --> Mockito 

Överväga AtomicInteger istället för synchronized???
Koden fungerar. Intellij anropet till Tomcat undviks med en filterklass. Inga unittest har gjort. Borde testa med Mockito. Kan gör det när jag kommit lite längre. 
Ville egentligen ha endast en Servlet + welcome-file-list i web.xml. 
Är concurrency safe?
Hur ska jag packa om det i en annan filstruktur när jag ska lämna in det?
ServletException hanteras inte --> Behöver det göras det? 
MAX --> Snabbkommandon Git??? 
