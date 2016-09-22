.PHONY: default

default: all

all: dbms server client

dbms: ddeps difaces dsys
	javac -cp .:src src/dbms/DedaDBMS.java -d bin/
	rmic -d bin/ -classpath .:bin/ dbms.DedaDBMS

ddeps:
	javac -cp .:src src/dbms/deps/CurrentDate.java -d bin/

difaces:
	javac -cp .:src src/dbms/ifaces/Dbms.java -d bin/
	javac -cp .:src src/dbms/ifaces/UserDB.java -d bin/

dsys:
	javac -cp .:src src/dbms/sys/SysDB.java -d bin/
	rmic -d bin/ -classpath .:bin/ dbms.sys.SysDB

server: sdeps sifaces sdbs sgui ssys sstubs
	javac -cp .:src src/server/DedaServer.java -d bin/
	javac -cp .:src src/server/DedaAdmin.java -d bin/

sdeps:
	javac -cp .:src src/server/deps/CurrentDate.java -d bin/
	javac -cp .:src src/server/deps/DeleteDir.java -d bin/
	javac -cp .:src src/server/deps/Globals.java -d bin/

sifaces:
	javac -cp .:src src/dbms/ifaces/Dbms.java -d bin/
	javac -cp .:src src/dbms/ifaces/UserDB.java -d bin/
	javac -cp .:src src/server/ifaces/BaseAdmin.java -d bin/
	javac -cp .:src src/server/ifaces/BaseRep.java -d bin/
	javac -cp .:src src/server/ifaces/BugAdmin.java -d bin/
	javac -cp .:src src/server/ifaces/BugIntrf.java -d bin/
	javac -cp .:src src/server/ifaces/PrjAdmin.java -d bin/
	javac -cp .:src src/server/ifaces/ProjIntrf.java -d bin/
	javac -cp .:src src/server/ifaces/UserAccount.java -d bin/
	javac -cp .:src src/server/ifaces/UserLogin.java -d bin/

sdbs:
	javac -cp .:src src/server/dbs/TracDB.java -d bin/

sgui:
	javac -cp .:src src/server/gui/MyProfile.java -d bin/
	javac -cp .:src src/server/gui/HomeAdmin.java -d bin/
	javac -cp .:src src/server/gui/Login.java -d bin/

ssys:
	javac -cp .:src src/server/sys/BaseActivator.java -d bin/
	javac -cp .:src src/server/sys/BaseRepository.java -d bin/
	javac -cp .:src src/server/sys/Bug.java -d bin/
	javac -cp .:src src/server/sys/BugList.java -d bin/
	javac -cp .:src src/server/sys/Project.java -d bin/
	javac -cp .:src src/server/sys/ProjectList.java -d bin/
	javac -cp .:src src/server/sys/User.java -d bin/
	javac -cp .:src src/server/sys/UserList.java -d bin/

sstubs:
	rmic -d bin/ -classpath .:bin/ server.sys.BaseActivator
	rmic -d bin/ -classpath .:bin/ server.sys.BaseRepository
	rmic -d bin/ -classpath .:bin/ server.sys.Bug
	rmic -d bin/ -classpath .:bin/ server.sys.BugList
	rmic -d bin/ -classpath .:bin/ server.sys.Project
	rmic -d bin/ -classpath .:bin/ server.sys.ProjectList
	rmic -d bin/ -classpath .:bin/ server.sys.User
	rmic -d bin/ -classpath .:bin/ server.sys.UserList

client: cdeps cifaces csys cgui
	javac -cp .:src src/client/DedaClient.java -d bin/

cdeps:
	javac -cp .:src src/client/deps/CurrentDate.java -d bin/
	javac -cp .:src src/client/deps/Globals.java -d bin/

cifaces:
	javac -cp .:src src/server/ifaces/BaseAdmin.java -d bin/
	javac -cp .:src src/server/ifaces/BaseRep.java -d bin/
	javac -cp .:src src/server/ifaces/BugAdmin.java -d bin/
	javac -cp .:src src/server/ifaces/BugIntrf.java -d bin/
	javac -cp .:src src/server/ifaces/ProjIntrf.java -d bin/
	javac -cp .:src src/server/ifaces/UserAccount.java -d bin/
	javac -cp .:src src/server/ifaces/UserLogin.java -d bin/

csys:
	javac -cp .:src src/client/sys/LocalRepository.java -d bin/

cgui:
	javac -cp .:src src/client/gui/BugEditor.java -d bin/
	javac -cp .:src src/client/gui/History.java -d bin/
	javac -cp .:src src/client/gui/MyProfile.java -d bin/
	javac -cp .:src src/client/gui/ProjectHome.java -d bin/
	javac -cp .:src src/client/gui/Login.java -d bin/
	javac -cp .:src src/client/gui/Home.java -d bin/

clean: 
	rm -rvf log
	rm sys/sqlite.db*
	rm -rvf bin/*

execdbms:
	java -Djava.security.policy=sys/dbms.policy -cp bin:sqlitejdbc-v054.jar dbms.DedaDBMS

execserver:
	java -Djava.security.policy=sys/server.policy -cp bin:sqlitejdbc-v054.jar server.DedaServer

execserverconf:
	java -Djava.security.policy=sys/server.policy -cp bin:sqlitejdbc-v054.jar server.DedaAdmin

execclient:
	java -Djava.security.policy=sys/client.policy -cp bin: client.DedaClient

