Server is hosted on Microsoft Azure Cloud Platform. 
Subscription: Azure for Students
Disk: 30GiB SSD
Operating System: Linux (ubuntu 18.04)
Size: Standard B1s (1vcpus, 1Gib memory)
Location: East US
Documents created by azure on the deployment of the server are included in this folder under "/Server Development Files"

If you need to connect to the server via shell. 
1. Open Putty or prefered ssh application
2. Server IP is 40.114.79.153 Port 22
3. Username: workflow (no caps)
4. Password: https://trello.com/c/Pe6IJxnR

NOTE:
The "workflow" user has sudo access. You can install anything that is needed for the project/deployment.
Contact me at nadeems@kean.edu or on skype for any questions or issues. 

Installing application/program on machine:
1. sudo apt-get install <package name>
2. Enter Password if applicable (On trello)

CURRENT PORTS OPEN ON AZURE:
1. MySQL: 3306 (Opened for testing for each developer)
2. SSH: 22 (Used for connecting via ssh)
3. HTTP: 80 (Used for webserver)
4. HTTPS: 443 (Forwarded incase of inclusion of SSL)
Contact Syed if any ports need to be fowarded. 