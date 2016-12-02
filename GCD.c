#include <stdio.h>


int isCorrect(char num[]);
int main(int argc, char* argv[]){
	if (argc != 3){
		printf("correct usage ./GCD a b \n");	
		return 1;
	}
	int a;
	int b;
	if(isCorrect(argv[1])>=0){
		a = isCorrect(argv[1]);
	} else{
		goto stop;
	}
	if(isCorrect(argv[2])>=0){
		b = isCorrect(argv[2]);
		goto euclid;
	}
stop: return 2;
euclid: while(b!=0){
		if( a > b){
			a = a-b;
		} else{
			b = b-a;
		}
	}
	printf("the result is: %i \n", a);
	return 0;
}
int isCorrect(char num[]){
	int size = sizeof(num)/sizeof(int)-1;
	if(num[0]==45){
		printf("negative numbers are not allowed as an input. \n");
		return -100;
	}
	int i; 
	for(i=0; i < size; i++){
		if(!isdigit(num[i])){
			printf("Please input only integers. \n");
			return -100;
		}
	}
	int number = atoi(num);
	if(number<0){
		printf("it is invalid to input a negative number as an argument. \n");
		return -100;
	}
	if(number>32767){
		printf("this number is too big. \n");
		return -100;
	}
	return number;
}
