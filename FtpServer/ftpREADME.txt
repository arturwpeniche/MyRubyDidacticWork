MakeFile
 ./makefile.sh

correr rmic e rmiregistry

/usr/java/jdk1.7.0_51/bin/rmic FtpImpl
/usr/java/jdk1.7.0_51/bin/rmiregistry 4666


correr o servidor:
java FtpServer -Djava.security.manager -Djava.security.policy==perms.policy

correr cliente
java FtpClient

usage:
open <machine>

cd <pasta>
lcd <pasta>
ls
get <ficheiro>
put <ficheiro>
exit

Compativel com jdk 1.7 & linux (n testado em windows)
