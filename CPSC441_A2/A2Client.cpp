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
using namespace std;

#define HOSTNAME "csx.cs.ucalgary.ca"
#define MAX_HOSTNAME_LENGTH 64
#define MAX_TWEET_LENGTH 140
#define CLIENTPORTNUM 12346

int mysocket1;


void closeprog(int sig){
	close(mysocket1);
	cout << "\n PROGRAM TERMINATED \n";
	exit(0);
}

void displayprompt(){
	cout << "1 - Encode tweet\n2 - Decode tweet\n3 - Quit";
    cout << "\nPlease Enter an integer from the list above\n";
  }
  

  
int main(void){
	signal(SIGINT,closeprog);
	char messageout [MAX_TWEET_LENGTH];
	char messagein [MAX_TWEET_LENGTH];
	int length;
	char character;
	
	//Creating socket
	struct sockaddr_in address;
	memset(&address,0,sizeof(address));
	address.sin_family = AF_INET;
	address.sin_port = htons(CLIENTPORTNUM);
	address.sin_addr.s_addr = htonl(INADDR_ANY);
	
	//get host by name
	struct hostent *hp;
	string hostname = {HOSTNAME};
	hp = gethostbyname(hostname.c_str());
	if(hp == NULL)
	{
		cout << "\n\nUNKNOWN HOST ERROR \n\n";
		exit(1);
	}
	bcopy(hp->h_addr,&address.sin_addr, hp->h_length);
	
	mysocket1 = socket(AF_INET, SOCK_STREAM, 0);
	if(mysocket1 == -1){
		cout << "creation of socket failed\n";
	}
	
	//Connecting socket
	int status;
	status = connect(mysocket1, (struct sockaddr *) &address, sizeof(struct sockaddr_in));
	if(status == -1){
		cout << "connecting to server failed\n";
	}
	
	while(1){
		length = 0;
		displayprompt();
		character = getchar();
		if(character == '1'){		//ENCODE TWEET BASED ON WHAT THE SERVER IS SET TO
			cout << "\n Please enter the tweet you would like encoded: ";
			getchar();
			messageout[length++] = '1';
			while((character = getchar()) != '\n'){
				messageout[length++] = character;
			}
			messageout[length] = '\0';
			if(send(mysocket1,messageout,sizeof(messageout),0) == -1){ //sending message
				cout << "\n\n ERROR SENDING MESSAGE TO SERVER \n\n";
			}
			if(recv(mysocket1,messagein,sizeof(messagein),0) == -1){ //receiving message
				cout << "\n\n Error on ther server \n\n";
			} else {
				cout << "\n\n Your encoded tweet is :	" << messagein << "\n\n"; 
			}
			
			
		} else if(character == '2'){		//DECODE TWEET INTO ORIGINAL BASED ON WHAT THE SERVER IS SET TO
			cout << "\n Please enter the tweet you would like decoded:	";
			getchar();
			messageout[length++] = '2';
			while((character = getchar()) != '\n'){
				messageout[length++] = character;
			}
			messageout[length] = '\0';
			if(send(mysocket1,messageout,sizeof(messageout),0) == -1){ //sending message
				cout << "\n\n ERROR SENDING MESSAGE TO SERVER \n\n";
			}
			if(recv(mysocket1,messagein,sizeof(messagein),0) == -1){ //receiving message
				cout << "\n\n Error on ther server \n\n";
			} else {
				cout << "\n\n Your decoded tweet is :	" << messagein << "\n\n"; 
			}
			
			
		} else if(character == '3'){ //Terminte the program
			messageout[0] = character;
			send(mysocket1,messageout,sizeof(messageout),0);
			close(mysocket1);
			cout << "\n PROGRAM TERMINATED \n";
			exit(0);
		} else {//Reloop until we recieve a valid character 
			cout << "\nINVALID ENTRY\n\n";
		}
		
	}
	
	
}