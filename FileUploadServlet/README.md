# File Upload Java Web Application

This is a Java web application demonstrating how to upload a file using Servlets, JavaBeans, and JSP with the help of the Jakarta EE 9 platform. The application supports uploading text files, JPEG, PNG, and GIF images. It limits the maximum file size and the number of concurrent uploads, and displays the file information and content accordingly.

## Features

- File upload form with `multipart/form-data` encoding
- Support for mime-types: `text/plain`, `image/jpeg`, `image/png`, and `image/gif`
- File size limit enforced using `@MultipartConfig` annotation
- Limit on the number of concurrent uploads using a Semaphore
- Separation of concerns using Servlets, JavaBeans, and JSP
- Error handling for exceeded file size limits and unsupported mime-types
- Display of file information and content

## Components

- `index.html`: An HTML form for file uploading, with enctype set to "multipart/form-data" and action targeting the FileUploadServlet.
- `FileUploadServlet.java`: A Servlet that processes the incoming request, checks if a file is attached, validates its mime-type, and populates a FileData JavaBean instance with the appropriate properties. It then forwards the request to the `result.jsp` file.
- `FileData.java`: A JavaBean that encapsulates the uploaded file's data and related properties, such as file name, mime-type, file size, and file content. It includes getters and setters for its properties.
- `result.jsp`: A JSP file that renders the HTML response based on the properties of the FileData JavaBean and any error messages passed as request attributes. It uses JSP Expression Language (EL) and JSTL tags to display the results or error messages.

## Setup and Usage

1. Clone the repository or download the source code.
2. Import the project into your favorite IDE (e.g., IntelliJ IDEA, Eclipse).
3. Ensure that you have the required dependencies added to your build configuration (e.g., Maven, Gradle).
4. Deploy the application on a servlet container like Apache Tomcat or GlassFish.
5. Access the application in your web browser using the URL `http://localhost:<port>/file-upload-java-webapp/` (replace `<port>` with your servlet container's port number).

## Dependencies

- Jakarta Servlet API
- Jakarta JSP API
- Jakarta JSTL
- Jakarta EL
- Lombok

Note: Make sure to add the appropriate dependencies to your build configuration (Maven, Gradle) and configure your servlet container according to your setup.

TODO
---

NOT WORKING!!!
TO MANY PARTS? --> Start from scratch? 