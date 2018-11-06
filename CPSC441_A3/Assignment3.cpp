#include <stdio.h>
#include <queue>
#include <list>
#include <stdlib.h>
using namespace std;

#define FILENAME "file.txt"
#define Mbps 11
#define BUFFERSIZE 50

double total = 0;

double getSize(FILE*fp){
	double x;
	fscanf(fp, "%lf", &x);
	return x;
}

double getArrival(FILE*fp){
	double x;
	fscanf(fp,"%lf",&x);
	return x;
}

//Here we update the queue for the passed time
void update(double time, queue<double> &myqueue){
	if(!myqueue.empty()){
		double temp = myqueue.front();
		while((!myqueue.empty()) && temp < time){
			myqueue.pop();
			temp = myqueue.front();
		}
	}
}

double bps = Mbps * 1000000 / 8 /1000;



int main(){
	queue <double> packetQueue;
	FILE *fp;
	fp = fopen(FILENAME, "r");
    if (fp == NULL) {
		printf("Failed to open file");
		return (1);
    }
	double arrTime;
	double droppedPackets;
	int count = 0;
	double packetsQueued = 0;
	double queueDelay = 0;
	
	while (!feof(fp)) {
		arrTime = (getArrival(fp)) * 1000;
		update(arrTime,packetQueue); // rid queue of all packets that had event time before arrTime
		if(packetQueue.size() == BUFFERSIZE){
			//drop packet
			droppedPackets++;
			getSize(fp);
			total++;
		} else if(packetQueue.empty()){
			//empty queue
			double temp = (getSize(fp)/ (bps)); //Processing time
			temp += arrTime;
			packetQueue.push(temp);
			//printf("Departure Time = %lf\n",temp);
			//printf("Current Time = %lf\n",arrTime);
			total++;
		} else {
			packetsQueued++; // This packet has a queueDelay
			double temp = packetQueue.back(); 
			queueDelay += temp - arrTime;
			temp = temp + (getSize(fp) / (bps)); //Processing time for the new packet plus the end time of the previous packet
			packetQueue.push(temp);
			//printf("Departure Time = %lf\n",temp);
			//printf("Current Time = %lf\n\n",arrTime);
			total++;
		}
    }
	printf("With buffer size %d and download speed %d Mbps your QoS is as follows:\n\n",BUFFERSIZE,Mbps);
	printf("Total queue delay = %0.3lf ms \n",queueDelay);
	printf("Average queue delay = %0.3lf ms\n\n", queueDelay/packetsQueued);
	printf("Total Packets = %d\n",(int) total);
	printf("Packets Recieved = %d\n", (int) (total-droppedPackets));
	printf("Dropped Packets = %d\n", (int) droppedPackets);
	printf("Dropped %0.2lf%% of packets\n",(droppedPackets/total)*100);
 

    fclose(fp);
    return (0);
	
	
}


