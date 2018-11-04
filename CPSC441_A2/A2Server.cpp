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
#include <vector>
#include <iomanip>


/*
	CHANGE ONE OF THESE CONSTANTS TO 1 SO THE SERVER KNOWS WHAT HASH FUNCTION IT IS USING
*/
#define SEQINDEXHASH 0
#define WORDSUM 0
#define MYHASH 1

#define MAX_TWEET_LENGTH 140
#define SERVERPORT 12345
int mysocket1;	//This is global so we can close the socket from anywhere with catcher function
using namespace std;
vector<string> seqindex; //DATABASE for sequential index hash function

struct Node{  //structure to hold more complicated hash results
	int id;
	string data;
};
vector<Node> sumword; //DATABASE for sum word hash function

vector<Node> myhash; //DATABASE for the hash I created for this assignment

void closeprog(int sig){
	close(mysocket1);
	cout << "\n PROGRAM TERMINATED \n";
	exit(0);
}

/*
	Evaluation of my hash function.
	
	My hash function uses the same idea as the word sum hash function with additional hash techniques to prevent collisions for words that have the same letters (grate and great)
	I do this by multiplying the length of the word by its first letter and adding that to the sum and also by multiplying each letter by its position in the word before adding it to the sum.
	This hash function works well in this case because our database is so small and we are using a vector instead of an array so we dont have to worry about load capacity as the vector expands over time.
*/
string myHashEncode(string str){
	int i = str.length() * str[0];
	for(int j = 0; j < str.length(); j++){
		i = i * j + str[j];
	}
	//Now check if the word is already in our database
	for(int j = 0;j < myhash.size(); j++){
		if( i == myhash.at(j).id){
			myhash.at(j).data = str; //Overwrite in the case of a collision
			stringstream temp;
			temp << hex << i;
			return temp.str();
		}
	}

	//Not in our database so lets add it and return its id for the client
	Node nodeTemp;
	nodeTemp.data = str;
	nodeTemp.id = i;
	myhash.push_back(nodeTemp);
	stringstream temp;
	temp << hex << i;
	return temp.str();
}

/*
	The decode is very simple because there is no computation, we are just taking the input  hex number and checking if it is stored in the database anywhere.
	This works fine because we are not working with a large database, the server isnt actually saving data anywhere just creating it at runtime.
*/
string myHashDecode(string str){
	int value;
	stringstream temp (str);
	temp >> hex >> value;
	for(int i = 0; i < myhash.size(); i++){
		if(value == myhash.at(i).id){
			return myhash.at(i).data;
		}
	}
	return "xxxx"; //Didnt find the value in the array so return xxxx to keep a little readability for other results that do match a server value
}


/*
This function takes a string and if that strings sum value is in the database it overwrites it,
if not it adds the string into a node and saves it to the database then returns the sum of the string
*/
string wordSumEncode(string str){
	int x = 0;
	for(int i = 0; str[i] != '\0';i++){
		x = x + str[i];
	}
	//Now we have the sum of the letters
	//Check if the word is already in our database
	for(int i = 0; i < sumword.size(); i++){
		if(x == sumword.at(i).id){
			sumword.at(i).data = str; //overwrite the collision
			stringstream temp;
			temp << hex << x;
			return temp.str();
		}
	}

	//Not in database so we add it
	Node nodeTemp;
	nodeTemp.data = str;
	nodeTemp.id = x;
	sumword.push_back(nodeTemp);
	stringstream temp;
	temp << hex << x;
	return temp.str();

}

/*
This function decodes the sum word hash by checking if the sum of the letters matches a word in the database and returns that or else it returns xxxx
*/
string wordSumDecode(string str){
	int value;
	stringstream temp (str);
	temp >> hex >> value;
	for(int i = 0; i < sumword.size();i++){
		if(value == sumword.at(i).id){
			return sumword.at(i).data;
		}
	}
	return "xxxx";

}



/*
This function is responsible for performing the sequential index has for encoding tweets
*/
string seqindexencode(string str){
	for(int i = 0; i < seqindex.size(); i++){
		if(str.compare(seqindex.at(i)) == 0){
		stringstream temp;
		temp << hex << i;
		return temp.str();
		}
	}

	int x = seqindex.size();
	seqindex.push_back(str);
	stringstream temp;
	temp << hex << x;
	return temp.str();
}

/*
This function is responsible for taking the hex number passed to it in a string and decoding it and returning it as a string.

Note: if we dont come up with a mach in the database we return xxxx (This is so that if we encounter a problem the tweet may still be slightly readable)
*/
string seqIndexDecode(string str){
	int index;
	stringstream temp (str);
	temp >> hex >> index;
	if(str.compare("0x0")){

	}
	if(index >= 0 && index < seqindex.size()){
		return seqindex.at(index);
	} else {
		return "xxxx";
	}
}

/*
*
*	MAIN STARTS HERE
*
*/

int main(void){
	signal(SIGINT,closeprog);
	//int listen_port = 8123;
	int mysocket2;

	//Address Initialization
	struct sockaddr_in address;
	memset(&address, 0 , sizeof(address));
	address.sin_family = AF_INET;
	address.sin_port = htons(SERVERPORT);
	address.sin_addr.s_addr = htonl(INADDR_ANY);
	cout << "Done Address Initialization";

	//Socket Creation (Listening)
	mysocket1;
	mysocket1 = socket(PF_INET,SOCK_STREAM,0);
	if(mysocket1 == -1){
		cout << "\nSocket Creation Failed";
	}

	//Binding
	int status;
	status = bind(mysocket1,(struct sockaddr *) &address, sizeof(address));
	if(status == -1){
		cout << "\nFailed Binding";
	}

	// Listening
	if(listen(mysocket1,5) == -1){
		cout << "\nFailed Listening";
	}
        cout << "\nServer Started\nUse ctrl+c to stop the server when you are done\n";
	while(1){
		//Connection Acceptance picked up web browser
		mysocket2 = accept(mysocket1, NULL, NULL);
		if(mysocket2 == -1){
			cout << "\nFailed Conection Acceptance";
		} else {
			cout << "\nConnection Accepted";
		}

#if SEQINDEXHASH
		while(1){
			//Receiving from client
				int count = 0;
				char mout[MAX_TWEET_LENGTH];
				bzero(mout,sizeof(mout));
				char min[MAX_TWEET_LENGTH];
				bzero(min,sizeof(min));

				count = recv(mysocket2, min, sizeof(min), 0);
				if(count == -1){
					cout << "\nFailed to recieve message from client";
				} else {
					cout << "\nRecieved message from client\n";
				}

                                if(min[0] == '3'|| min[0] == NULL){		//Client has quit
					close(mysocket2);
					break;

				} else if(min[0] == '1'){		//ENCODING A TWEET WITH SEQUENTIAL INDEX HASH FUNCTION
					int returnindex = 0;
					string str;
					char c;
					int i = 1;
					while((c = min[i++])!= '\0'){
						tolower(c);
						str.insert(str.end(),c);
						if(c == ' '){ //was c == ' '
							string x = seqindexencode(str);
							mout[returnindex++] = '0';
							mout[returnindex++] = 'x';
							for(int i = 0; x[i] ; i++){
								mout[returnindex++] = x[i];
							}
							mout[returnindex++] = ' ';
							str.clear();
						}
					}
					string x = seqindexencode(str);
					mout[returnindex++] = '0';
					mout[returnindex++] = 'x';
					for(int i = 0; x[i] ; i++){
						mout[returnindex++] = x[i];
					}
					mout[returnindex++] = '\0';

					//Sending the encoded tweet back
					if(send(mysocket2,mout,sizeof(mout),0) == -1){
					cout << "\nFailed to send message to client";
					} else {
					cout << "\nSent message to client\n";
					}
					bzero(mout,sizeof(mout));

				} else if(min[0] == '2'){		//DECODING A TWEET WITH SEQUENTIAL INDEX HASH FUNCTION
					int returnindex = 0;
					string str;
					char c;
					int i = 1;
					while((c = min[i++])!= '\0'){
						tolower(c);
						str.insert(str.end(),c);
						if(c == ' '){
							string x = seqIndexDecode(str);
							for(int i = 0; x[i] ; i++){
								mout[returnindex++] = x[i];
							}
							mout[returnindex++] = ' ';
							str.clear();
						}
					}
					string x = seqIndexDecode(str);
					for(int i = 0; x[i] ; i++){
						mout[returnindex++] = x[i];
					}
					mout[returnindex++] = '\0';

					//Sending the decoded tweet back
					if(send(mysocket2,mout,sizeof(mout),0) == -1){
					cout << "\nFailed to send message to client";
					} else {
					cout << "\nSent message to client\n";
					}
					bzero(mout,sizeof(mout));

				} else { //Just an error check
					cout << "\n\n SOMETHING WENT WRONG\nValue of min[0] was: " << min[0];
				}

		}
#endif
#ifdef WORDSUM
		while(1){
			//Receiving from client
				int count = 0;
				char mout[MAX_TWEET_LENGTH];
				bzero(mout,sizeof(mout));
				char min[MAX_TWEET_LENGTH];
				bzero(min,sizeof(min));

				count = recv(mysocket2, min, sizeof(min), 0);
				if(count == -1){
					cout << "\nFailed to recieve message from client";
				} else {
					cout << "\nRecieved message from client\n";
				}

                                if(min[0] == '3'|| min[0] == NULL){		//Client has quit
					close(mysocket2);
					break;

				} else if(min[0] == '1'){		//ENCODING A TWEET WITH WORD SUM HASH FUNCTION
					int returnindex = 0;
					string str;
					char c;
					int i = 1;
					while((c = min[i++])!= '\0'){
						tolower(c);
						str.insert(str.end(),c);
						if(c == ' '){
							string x = wordSumEncode(str);
							mout[returnindex++] = '0';
							mout[returnindex++] = 'x';
							for(int i = 0; x[i] ; i++){
								mout[returnindex++] = x[i];
							}
							mout[returnindex++] = ' ';
							str.clear();
						}
					}
					string x = wordSumEncode(str);
					mout[returnindex++] = '0';
					mout[returnindex++] = 'x';
					for(int i = 0; x[i] ; i++){
						mout[returnindex++] = x[i];
					}
					mout[returnindex++] = '\0';

					//Sending the encoded tweet back to client
					if(send(mysocket2,mout,sizeof(mout),0) == -1){
					cout << "\nFailed to send message to client";
					} else {
					cout << "\nSent message to client\n";
					}
					bzero(mout,sizeof(mout));

				} else if(min[0] == '2'){		//DECODING A TWEET WITH WORD SUM HASH FUNCTION
					int returnindex = 0;
					string str;
					char c;
					int i = 1;
					while((c = min[i++])!= '\0'){
						tolower(c);
						str.insert(str.end(),c);
						if(c == ' '){
							string x = wordSumDecode(str);
							for(int i = 0; x[i] ; i++){
								mout[returnindex++] = x[i];
							}
							mout[returnindex++] = ' ';
							str.clear();
						}
					}
					string x = wordSumDecode(str);
					for(int i = 0; x[i] ; i++){
						mout[returnindex++] = x[i];
					}
					mout[returnindex++] = '\0';

					//Sending the decoded tweet back to the client
					if(send(mysocket2,mout,sizeof(mout),0) == -1){
					cout << "\nFailed to send message to client";
					} else {
					cout << "\nSent message to client\n";
					}
					bzero(mout,sizeof(mout));

				} else {	//Just some error checking in case we get a weird message from client not setup properly
					cout << "\n\n SOMETHING WENT WRONG\nValue of min[0] was: " << min[0];
				}

		}
#endif
#ifdef MYHASH
		while(1){
			//Receiving from client
				int count = 0;
				char mout[MAX_TWEET_LENGTH];
				bzero(mout,sizeof(mout));
				char min[MAX_TWEET_LENGTH];
				bzero(min,sizeof(min));

				count = recv(mysocket2, min, sizeof(min), 0);
				if(count == -1){
                                        cout << "\nChanged Clients";
				} else {
					cout << "\nRecieved message from client\n";
				}

                                if(min[0] == '3'|| min[0] == NULL){		//Client has quit
					close(mysocket2);
					break;

				} else if(min[0] == '1'){		//ENCODING A TWEET WITH MY HASH FUNCTION
					int returnindex = 0;
					string str;
					char c;
					int i = 1;
					while((c = min[i++])!= '\0'){
						tolower(c);
						str.insert(str.end(),c);
						if(c == ' '){
							string x = myHashEncode(str);
							mout[returnindex++] = '0';
							mout[returnindex++] = 'x';
							for(int i = 0; x[i] ; i++){
								mout[returnindex++] = x[i];
							}
							mout[returnindex++] = ' ';
							str.clear();
						}
					}
					string x = myHashEncode(str);
					mout[returnindex++] = '0';
					mout[returnindex++] = 'x';
					for(int i = 0; x[i] ; i++){
						mout[returnindex++] = x[i];
					}
					mout[returnindex++] = '\0';

					//Sending the encoded tweet back to client
					if(send(mysocket2,mout,sizeof(mout),0) == -1){
					cout << "\nFailed to send message to client";
					} else {
					cout << "\nSent message to client\n";
					}
					bzero(mout,sizeof(mout));

				} else if(min[0] == '2'){		//DECODING A TWEET WITH MY HASH FUNCTION
					int returnindex = 0;
					string str;
					char c;
					int i = 1;
					while((c = min[i++])!= '\0'){
						tolower(c);
						str.insert(str.end(),c);
						if(c == ' '){
							string x = myHashDecode(str);
							for(int i = 0; x[i] ; i++){
								mout[returnindex++] = x[i];
							}
							mout[returnindex++] = ' ';
							str.clear();
						}
					}
					string x = myHashDecode(str);
					for(int i = 0; x[i] ; i++){
						mout[returnindex++] = x[i];
					}
					mout[returnindex++] = '\0';

					//Sending the decoded tweet back to the client
					if(send(mysocket2,mout,sizeof(mout),0) == -1){
					cout << "\nFailed to send message to client";
					} else {
					cout << "\nSent message to client\n";
					}
					bzero(mout,sizeof(mout));

                                }
                                break;
		}

#endif
	}
}
