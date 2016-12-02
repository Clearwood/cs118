#include <stdio.h>
#include <string.h>
#include <math.h>
void usage();
int counter(char* count);
int main(int argc, char* argv[]){
	if(argc!=3){
		printf("argc\n");
		usage();
		return 1;
	}

	if(strlen(argv[1])!=strlen("00000000")||strlen(argv[2])!=strlen("00000000")){
		printf("strlen\n");
		usage();
		return 1;	
	}
	int overhang = 0;
	char* first= argv[1];
	char* sec = argv[2];
	int fir;
	printf("%s\n",first);
	printf("%s\n",sec);
	int sec2;
	if(counter(first)!=100000){
		fir = counter(first);
	} else{
		return 1;
	}
	if(counter(sec)!=100000){
		sec2 = counter(sec);
	} else{
		return 1;	
	}
	printf("first: %i", fir);
	printf("second: %i", sec2);
	int tmp = fir + sec2;
	printf("tmp: %i\n",tmp);
	char result[] = "000000000";
	int tmp2 = 0;
	int pow8 =-(int) pow(2,8);
	if(tmp < 0 ){
		result[0]=49;
		tmp2 = pow8;
	}
	printf("tmp2: %i\n",tmp2);
	int tmp3=0;
	int i;
	for(i=0; i<8; i++){
		int pow4 = pow(2, 7-i);
		tmp3 = tmp2 + pow4;
		if(tmp3<=tmp){
			result[i+1]=49;
			tmp2 += pow4;
		}
	}
	printf("result: %s\n", result);
	return 0;

}
int counter(char* count){
	int fir = 0;
	int i = 0;
	printf("count: %s\n",count);
	int fzero = (int) pow(2,0);
	printf("%i\n",fzero);
	for(i = 0; i < 8; i++){
		if(i < 7){
			if(count[7-i]==49){
				printf("%i\n", i);
				fir += (int) pow(2, i);
			}
			else if(count[7-i]!=48&&count[7-i]!=49){
				//ASCII numbers not equal 0-10??
				printf("not zero \n");
				usage();
				return 100000;
			}
		} else{
			if(count[0]==49){
				fir -= (int)pow(2, 7);
			}
			else if(count[0]!=48&&count[0]!=49){
				printf("zero \n");
				usage();
				return 100000;
			}
		} 
	}
	printf("%i\n",fir);
	return fir;
}
void usage(){

	printf("correct usage ./bitAdder 8-bit number a 8-bit number b \n");
}
