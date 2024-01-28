# EchoParamServlet

A simple web application built with Jakarta Servlets that echoes back query parameters in name/value pairs.

## Usage

To use the application, simply run it on a server (such as Apache Tomcat) and visit the following URL:

```
http://localhost:8080/EchoParamServlet/echo-param-servlet?Name=Björn&Age=35&City=Stockholm&Occupation=Software%20developer&Employer=HiQ%20Sweden
```

The query parameters can be modified to include any name/value pairs desired. The application will display the parameters in name/value pair format on the webpage.

## Dependencies

This project uses the following dependencies:

- Jakarta Servlet API 5.0.0
- JUnit Jupiter API 5.8.1
- JUnit Jupiter Engine 5.8.1

## Building

To build the project, use Maven:

```
mvn clean install
```

The built WAR file will be located in the `target` directory.


TODO
----
ServletException???

Denna WEb appen skulle troligen verkligen kunna göras med endast en .jsp fil. 