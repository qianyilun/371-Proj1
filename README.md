# 371-Project 1 - Multi-threaded Web Server

In this project, I develop a Web server that is capable of processing multiple simultaneous requests in parallel. I can demonstrate that Web server is capable of delivering home page to a Web browser.

I implement version 1.0 of HTTP, as defined in RFC 1945, where separate HTTP requests are sent for each component of the Web page. The server is multi-threaded. In the main thread, the server listens on a fixed port. When server receives a TCP connection request, it sets up a TCP connection through another port and services the request in a separate thread. To simplify programming task, I develop the code in two stages. In the first stage, I write a multi-threaded server that simply displays the contents of the HTTP request message that it receives. After this program is running properly, I add the code required to generate an appropriate response. As I am developing the code, I can test my server from a Web browser. 
