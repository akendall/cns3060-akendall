#include 	<stdio.h>
#include 	<signal.h>
#include 	<termios.h>
#include 	<unistd.h>

#define PAGELEN 24
#define LINELEN 512

void do_more(FILE *, int len);
int see_more(FILE *, int len);
int flength( char * );
char getch(){
	char buff = 0;
	struct termios old = {0};
	if (tcgetattr(0,&old)<0)
		perror("tcsetattr()");
	old.c_lflag &= ~ICANON;
	old.c_lflag &= ~ECHO;
	old.c_cc[VMIN] = 1;
	old.c_cc[VTIME] = 0;
	if(tcsetattr(0, TCSANOW, &old)<0)
		perror("tcsetattr ICANON");
	if(read(0, &buff, 1)<0)
		perror("read()");
	old.c_lflag |= ICANON;
	old.c_lflag |= ECHO;
	if(tcsetattr(0, TCSADRAIN, &old)<0)
		perror("tcsetattr ~ICANON");
	return(buff);
}
int main (int ac, char *av[])
{
	FILE *fp;
	int length = 0;
	if (ac == 1)
		do_more( stdin, 0l );
	else
		while(--ac)
			if ((fp = fopen( *++av, "r"))!= NULL)
			{
				length = flength(*av);	
				do_more( fp, length );
				fclose( fp );
			}
			else
				return -1;
}
void do_more ( FILE *fp, int length )
/*
 * read PAGELEN lines, then call see_more() for further instructions
*/
{
	char line[LINELEN];
	int num_of_lines = 0;
	int see_more(), reply;
	int count = PAGELEN;
	double perc = 0;
	int bytes = 0;
	FILE *fp_tty;

	fp_tty = fopen("/dev/tty", "r");
	if(fp_tty == NULL)
		return;

	while (fgets(line, LINELEN, fp ))
	{
		if (num_of_lines == PAGELEN)
		{	
			perc = 100-length/count;
			
			reply = see_more(fp_tty, (int)perc);
			if(reply == PAGELEN)
				count+=PAGELEN;
			if(reply == 1)
				count+=1;
			if (reply == 0)
				break;
			num_of_lines -= reply;
		}
		if (fputs(line, stdout) == EOF){	
			return;
		}
		num_of_lines++;	
	}
}
int see_more(FILE *cmd, int length)
/*
* print message, wait for response, return # of lines to advance
* q means no, space means yes, cr means one line
*/
{
	int c;
	char a = '%';
	printf("\033[7m more? \033[m(%d%c)", length, a);
	fflush(stdout);
	signal(SIGINT, SIG_IGN);
	while(1){
		sleep(1);	
		while((c=getch()) != EOF){
			if (c == '\n'){
				fputs("\003[A\033[2K\033[A\033[2K", stdout);
				//rewind(stdout);
				ftruncate(1,0);
				return 1;
			}if (c == 'q' ){
				return 0;
			}if (c == ' '){
				fputs("\003[A\033[2K\033[A\033[2K", stdout);
				//rewind(stdout);
				ftruncate(1,0);
				return PAGELEN;
			}
		}
	}	
	return 0;
}

int flength(char *fname){
	FILE *fp;
	int length = 0;
	
	fp = fopen(fname, "rb");
	if (fp != NULL){
		fseek(fp, 0, SEEK_END);
		length = ftell(fp);
		fclose(fp);	
	}
	return length;
}
