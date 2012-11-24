#include <stdio.h>
#include<pthread.h>

#define NUM 4

int count = 0;
 
main(){
	count = 0;
	pthread_t thread[NUM];
	int i;
	void	*incrementCounter(void *);
	for (i=0; i<NUM; i++) {
        	pthread_create(&thread[i], NULL, incrementCounter, count);
    	}
	for (i=0; i<NUM; i++) {
        	pthread_join(thread[i], NULL);
    	}
	printf("count = %d\n",count);
	return 0;
}  

void* incrementCounter( void* m ) { 
	int i; 
	for (i = 0; i < 10; ++i) { 
		int tempValue = count; 
		/*sleep(1);*/ 
		tempValue = tempValue + 1; 
		count = tempValue; 
	} 
	return NULL; 
} 
