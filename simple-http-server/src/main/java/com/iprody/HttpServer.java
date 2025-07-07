package com.iprody;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HttpServer {
    public static void main(String[] args) {
        final String rootDirectory = "static";

        Path staticPath = Paths.get(rootDirectory).toAbsolutePath();

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started at http://localhost:8080");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                    String requestLine = in.readLine();
                    if (requestLine == null) {
                        System.out.println("Empty request, skipping");
                        continue;
                    }
                    System.out.println("Request " + requestLine);
                    String path = "";
                    String[] parts = requestLine.split(" ");
                    if (parts.length >= 2) {
                        path = parts[1];
                    }
                    if (path.equals("/") || path.isEmpty()) {
                        path = "/index.html";
                    }
                    Path filePath = staticPath.resolve(path.substring(1)).normalize();
                    if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
                        sendErrorResponse(out, 404, "Not Found");
                        continue;
                    }
                    if (!filePath.startsWith(staticPath.normalize())) {
                        sendErrorResponse(out, 403, "Forbidden");
                        continue;
                    }
                  if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                      String contentType = getContenType(filePath);
                      byte[] fileContent = Files.readAllBytes(filePath);

                      out.write("HTTP/1.1 200 OK \r\n");
                      out.write("Content-Type:" + contentType+ "\r\n");
                      out.write("Content-Length: " + fileContent.length + "\r\n");
                      out.write("\r\n");
                      out.flush();

                      clientSocket.getOutputStream().write(fileContent);
                  } else {
                      sendErrorResponse(out, 404, "Not Found");
                  }

                } catch (IOException e) {
                    System.err.println("Client error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static String getContenType(Path filePath) {
        String filename = filePath.getFileName().toString();
        if (filename.endsWith(".html")) return "text/html";
        if (filename.endsWith(".css")) return "text/css";
        if (filename.endsWith(".js")) return "application/javascript";
        if (filename.endsWith(".png")) return "image/png";
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
        return "text/plain";
    }
    private static void sendErrorResponse(BufferedWriter out, int statusCode, String statusText) throws IOException {
        String response = "<h1> " + statusCode + " " + statusText +"</h1>";
        out.write("HTTP/1.1 " + statusCode +  " " + statusText
                + "\r\n");
        out.write("Content-Type: text/html\r\n");
        out.write("Content-Length: " + response.length() + "\r\n");
        out.write("\r\n");
        out.write(response);
        out.flush();
    }
}

