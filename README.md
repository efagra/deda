# deda

DEDA is a sememster project for the course Distributed Systems, that I have attended during my Bachelor's degree in the Univercity of Macedonia www.uom.gr.

DEDA stands for Distributed Easy Development Administration. It's a graphical multiuser application that helps a team of developers administer their projects, using repository and issue ticket management through a single standalone application.

The application is designed in a three tier architecture manner. The database application server administers the connection to the local database and handles the incoming queries preparing and sending back the answers.

The server application is responsible for handling the client connections and queries. It also handles user management, remote source code repositories and issue management. As a back-end for the repository management mercurial is used and for the issue management trac’s design is used, but the implementation is simplified to the requirements of the project.

Finally, the client side application logs the user in and provides a graphical way of interacting with basic repository functions and issue management. All the network connections are handled with the use of Java's EE RMI.

