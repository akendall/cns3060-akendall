#include <stdlib.h>
#include <stdio.h>  
#include <string.h>

#define LINELEN 512      
main(int argc, char *argv[]) 
{  
   FILE *fp, *fopen(); 
   int show_lines = 0;
   int cmpResult;
   int n;
   int i = argc;
   if (argc == 1){ 
      stdin_stdout(stdin);
      exit(0);
   } 
   if((n = strncmp(argv[1], "-n", LINELEN))==0)
         n=0;
   while (--argc > 0) { 
         fp= fopen(*++argv, "r");
         cmpResult = strncmp(*argv, "-n", LINELEN);
         if (fp == NULL && argc < 2) { 
            fprintf(stderr, "cat: can't open %s\n", *argv);  
            exit(1);  
         } 
         else{
             if (cmpResult != 0 && n==0)
                ln_print(fp, show_lines);
             else
                 copy_file(fp);
             
         }
   }  
   exit(0);  
}  
    ln_print(fp, show_lines)
    FILE *fp;
    int show_lines;
    {
        int ch;
        printf("%d ",++show_lines);
        while ((ch = getc(fp)) != EOF){ 
            fputc(ch, stdout);
            if (ch =='\n')
               printf("%d ",++show_lines);
            }
        putchar('\n'); 
        fclose(fp);
    }
    copy_file(fp)
    FILE *fp;
    {
       int ch;
        while ((ch = getc(fp)) != EOF)
            fputc(ch, stdout);
        putchar('\n'); 
        fclose(fp);
    }
    stdin_stdout(fp)    /* copy file fp to standard output */  
    FILE *fp;  
    {  
        int ch;
        while ((ch = getchar()) != EOF && ch !='Q')
              putchar(ch);
    }  

