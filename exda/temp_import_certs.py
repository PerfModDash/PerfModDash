import string,os

Command="$JAVA_HOME/bin/keytool -import -alias {ALIAS} -keystore /home/tomw/.keystore -trustcacerts -file /etc/grid-security/certificates/{FILENAME}"

currentDir=os.getcwd()
os.chdir("/etc/grid-security/certificates/")
for file in os.listdir(os.getcwd()):
    if file.find(".0")!=-1:
        #print file
        alias=file.split(".")[0]
        newCommand=Command.replace("{ALIAS}",alias).replace("{FILENAME}",file)
        print newCommand 
        
os.chdir(currentDir)
