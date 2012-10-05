#include <sys/types.h>
#include <dirent.h>
#include <unistd.h>
#include <stdio.h>
#include <io.h>
#include <errno.h>

long flength(char *fname){
     FILE *fptr;
     long length = 0L;

     fptr = fopen(fname, "rb");
     if(fptr != NULL){
         fseek(fptr, 0L, SEEK_END);
         length = ftell(fptr);
         fclose(fptr);
     }
     return length/8;
}

int main(int argc, char *argv[]){  
    /*no arguments:
    print out the name and number of bytes on disk for the current directory
    size of the directory itself do the same for all files/ subdirectories*/
    if(2 != argc){
        DIR *d;
        struct dirent *dir;
        d = opendir(".");
        if (d) {
            while ((dir = readdir(d)) != NULL) {
                printf("%ld B\t%s\n",flength(dir->d_name), dir->d_name);
            }
            closedir(d);
        }
        system("PAUSE");
        return 0;
    }
    DIR *dp = NULL;
    struct dirent *dptr = NULL;
    // Buffer for storing the directory path
    char buff[128];
    memset(buff,0,sizeof(buff));
    /*Filename as an argument:
    print our the name of the file and the number of bytes it takes up on the 
    disk*/
    FILE *fp; 
    if((fp=fopen(argv[1], "r"))!=NULL){
         printf("%ld B\t%s\n", flength(argv[1]), argv[1]);
         return 0;
    }
    /*Directory name as an argument:
    print out the name and number of bytes on disk for the named directory.
    then print out the name and number of byts on disk for each file in the 
    named directory, it will then open the directory and do the same thing for 
    all files inside the directory. and do the same rof the sub-directory.*/
     //copy the path set by the user
    strcpy(buff,argv[1]);
    // Open the directory stream
    if(NULL == (dp = opendir(argv[1])) ){
        printf("\n Cannot open Input directory [%s]\n",argv[1]);
        exit(1);
    } else{
        // Read the directory contents
        while(NULL != (dptr = readdir(dp)) ){
            printf("%ld B\t%s\n", flength(dptr->d_name), dptr->d_name);
        }
        // Close the directory stream
        closedir(dp);
        // Remove the new directory created by us
        rmdir(buff);
        printf("\n");
    }
    /*privide appropriate error checking and handling*/
    /*du command will take no options*/
    system("PAUSE");
    return 0;
}
