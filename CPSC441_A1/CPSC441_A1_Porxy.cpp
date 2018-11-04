#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>
#include <signal.h>
#include <iostream>
#include <string.h>
#include <strings.h>
#include <unistd.h>
#include <cstdlib>
int msocket1;	//This is global so we can close the socket from anywhere with catcher function
int NUMBOFERRORS = 5;   //Number of errors we want to insert into the page

using namespace std;

/*
 *	This Program was created by Evan McPhee for CPSC441 A1.
 *	Some of the code is adapted from slides provided by the TA for creating and using sockets.
 *	The function catcher was adapted from a proxy provided by the professor to last years class and I used it to make sure I had a way to kill the program without leaving the socket open.
 */
void catcher(int sig){
	close(msocket1);
	cout << "\n PROGRAM TERMINATED \n";
	exit(0);
}

int main(void){
	signal(SIGINT, catcher);
	int listen_port = 8123;
	
	//Address Initialization
	struct sockaddr_in address;
	memset(&address, 0 , sizeof(address));
	address.sin_family = AF_INET;
	address.sin_port = htons(listen_port);
	address.sin_addr.s_addr = htonl(INADDR_ANY);
	cout << "Done Address Initialization";
	
	//Socket Creation (Listening)
	msocket1;
	msocket1 = socket(AF_INET,SOCK_STREAM,0);
	if(msocket1 == -1){
		cout << "\nSocket Creation Failed";
	}
	
	//Binding 
	int status;
	status = bind(msocket1,(struct sockaddr *) &address, sizeof(struct sockaddr_in));
	if(status == -1){
		cout << "\nFailed Binding";
	}
	
	// Listening
	if(listen(msocket1,5) == -1){
		cout << "\nFailed Listening";
	}
	while(1){
		//Connection Acceptance picked up web browser
		int msocket2;
		msocket2 = accept(msocket1, NULL, NULL);
		if(msocket2 == -1){
			cout << "\nFailed Conection Acceptance";
		} else {
			cout << "\nConnection Accepted";
		}
		
		//Recieving from client 
		int count = 0;
		char mout[10000];
		bzero(mout,sizeof(mout));
		char min[10000];
		bzero(min,sizeof(min));

		count = recv(msocket2, min, sizeof(min), 0);
		if(count == -1){
			cout << "\nFailed to recieve HTML from client";
		} else {
			cout << "\nRecieved HTML from client\n";
		}
		strcpy(mout,min);

		//Fixing if the Get request has an if modified line so that we always get a refresh. This is just a way around dealing with 304
		string  str(mout);
			int temp = str.find("If-Modified-Since:");
			if(temp != string::npos){	
				mout[temp + 31] = '1';
				mout[temp + 32] = '9';
				mout[temp + 33] = '9';
				mout[temp + 34] = '8';
			}
		
		//Parsing HTTP
		char host[strlen(mout)];
		char url[strlen(mout)];
		
		
		char * pathholder = strtok(min,"\r\n");
		cout <<"\nPath \n";
		cout << pathholder;
		stpcpy(url,pathholder + 11);
		cout << "\nURL \n";
		cout << url;
		int cursindex = 10;
		for(int i = 0; i < strlen(url); i++){
			if(url[i] == '/'){
				strncpy(host,url,i);
				host[i] = '\0';
				break;
			}
		}
		cout <<"\nHost \n";
		cout << host;
		
		//Connecting to host
		struct sockaddr_in server_addr;
		struct hostent * saddress;
		memset(&server_addr, 0 , sizeof(server_addr));
		saddress = gethostbyname(host);
		bcopy((char*) saddress->h_addr,(char*) &server_addr.sin_addr.s_addr,saddress->h_length);
		if(saddress == NULL){
			cout << "\nError in gethostbyname()";
		} else {
			cout << "\nServer:	";
			cout << saddress->h_name;
		}
		
		//Creating a socket to talk to web server
		int msockweb = socket(AF_INET,SOCK_STREAM,0);
		server_addr.sin_family = AF_INET;
		server_addr.sin_port = htons(80);
		if(msockweb == -1){
			cout << "\nWeb socket failed creation";
		} else {
			cout << "\nWeb socket created";
		}
		
		//Connection to web server
		if(connect(msockweb,(struct sockaddr *) &server_addr, sizeof(server_addr)) == -1){
			cout << "\nProblem connecting to webserver";
		} else {
			cout << "\nConnected to web server";
		}
		
		//Send message to web server
		count = send(msockweb,mout,sizeof(mout),0);
		if(count == -1){
			cout << "\nMessage failed to send to web server";
		} else {
			cout << "\nMessage sent to web server was: \n" << mout << "\n done";
		}
		
		//Receive response from server
		count = recv(msockweb,min,sizeof(min),0);
		if(count == -1){
			cout << "\nMessage not received from web server";
		} else {
			cout << "\nMessage received from web server\n";
			for(int i = 0; i < 4; i++){
				count += recv(msockweb,&min[count],sizeof(min),0);
			}
			strcpy(mout,min);
			cout <<"\n\nthis is the message received from server unedited\n" << mout << "\n\n\nTHE END OF SERVER MESSAGE \n\n";
		}
		
		
		/*
		
		Determine if we got a 200 Ok or not
		
		*/
		string okcheck = "200 OK";
		string strcheck(min);
		int whatDo = 0;
		if(strcheck.find(okcheck) != string::npos){
			whatDo = 1;
		}
		
		/*
		DETERMINE CONTENT TYPE AND MESS WITH IT		
		*/
		if(whatDo == 1){
			string htmltest = "Content-Type: text/html;";
			string contlength = "Content-Length: ";
			bool ishtml;
			string str(min);
			int contentlength = 0;
			char cmout[20000];
		
			//Get content length
			int clindex = str.find(contlength);
			if(clindex != string::npos){
				clindex += 16;
				string contentlengthstorage;
				while(min[clindex] != '\n'){
					contentlengthstorage = contentlengthstorage + (min[clindex]);
					clindex++;
				}
				contentlength = stoi(contentlengthstorage);
			}
		
		
			//Dealing with text on a plain text or HTML basis
			if(int index =  str.find(htmltest) != string::npos)
			{
				/*
				
				DEALING WITH HTML
				
				*/
				cout << "\nDEALING WITH HTML\n";
				int textindex = str.find("<body>");
				textindex += 6;
				int errors = NUMBOFERRORS;
				while(errors > 0){
					int x = rand() %contentlength + 0;
					x += textindex;
					if((str[x] > 96 && str[x] < 123)){
						int temp = x;
						while(temp < contentlength+textindex){
							temp++;
							if(str[temp] == '<'){  //We have found text between >< so we can change it without messing up formating
								int y = rand() %26 + 0;
								if(97 + y != str[x]){
									str[x] = 97 + y;
									errors--;
								}
								break;
							} else if (str[temp] == '>'){  //We have found text inside <> so we need to ignore it
								break;
							}
						}
					}
				}
				strcpy(cmout,str.c_str());
				
				//Send changed response to browser
				count = send(msocket2,cmout,sizeof(cmout),0);
				if(count == -1){
					cout << "\nProblem sending response back to web browser";
				} else {
					cout << "\nSent html to web browser with changed HTML\n";
				}
			
				/*
				
				DEALING WITH PLAIN TEXT
				
				*/
			} else if(str.find("text/plain;") != string::npos) { 
				cout << "\nDEALING WITH PLAIN TEXT\n";
				int textindex = str.find("charset=UTF-8");
				textindex += 15;
				int errors = NUMBOFERRORS;
				while(errors > 0){
					int x = rand() %contentlength + 0;
					x += textindex;
					if((str[x] > 96 && str[x] < 123)){
						int y = rand() %26 + 0;
						if(97 + y != str[x]){
							str[x] = 97 + y;
							errors--;
						}
					}
				}
				strcpy(cmout,str.c_str());
				
				//Send changed response to browser
				count = send(msocket2,cmout,sizeof(cmout),0);
				if(count == -1){
					cout << "\nProblem sending response back to web browser";
				} else {
					cout << "\nSent html to web browser with changed plain text\n";
				}
			} else {
			
				//Send response to browser because we didnt hit a tag of html or plan text
				count = send(msocket2,min,sizeof(min),0);
				if(count == -1){
					cout << "\nProblem sending response back to web browser";
				} else {
					cout << "\nSent html to web browser but unchanged because there was no html or text tag\n";
				}
			}
			
		} else {
			//Send unchanged response to browser because we got something other then a 200 OK so let 
			count = send(msocket2,mout,sizeof(mout),0);
			if(count == -1){
			cout << "\nProblem sending response back to web browser";
			} else {
			cout << "\nSent html to web browser but unchanged because we didnt get a 200 Ok \n";
			}
		}
	} 
    close(msocket1);
	return 0;
}