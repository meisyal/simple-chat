/*
Prorokol cient:
1. CONN -> Connect + username + publickey (CONN:<username>:<public(e)>:<public(n)>:) 
2. LIST -> Meminta daftar pengguna yang online -> Return (LIST:<username>:<public(e)>:<public(n)>:)
3. DONE -> Flag ketika LIST sudah dikirimkan semua
4. SEND -> Mengirim pesan ke sala satu pengguna online (SEND:<sender>:<receiver>:<en-messege>:<en-key>:<hashvalue>:)  
5. DCON -> Disconnect dari server 
*/

#include <stdio.h>            
#include <stdlib.h>            
#include <sys/socket.h>          
#include <sys/types.h>            
#include <string.h>          
#include <netinet/in.h>   
#include <pthread.h> 
#include <arpa/inet.h>  
#include <unistd.h> 
#include <signal.h> 
      
#define PORT 2015

struct Node {	//Struct untuk client    
	int id;  
	char username[32];  
	char pub_e[1024];
	char pub_n[1024];
	int online;
	struct Node *next;  
};

typedef struct Node *ptrtonode;  
typedef ptrtonode head;  
typedef ptrtonode addr;  

head MakeEmpty (head h);	//Mengosongkan List
void Delete (int port, head h);	//Menghapus Client
void Insert (int port,char* username, char* cli_e, char* cli_n, head h,addr a);	//Menambahkan Client  
void Display (head h); //Melihat list user online di server
void DeleteList (head h);
void *server(void *arguments);	//thread server
void Sendlist (head h);	//Menampilkan user online di client
  
head h;                    
char buffer[2048], username[32], notif[30], cli_e[1000], cli_n[1000]; 
int tmpID; 

int main(int argc, char *argv[]) {  
	int sockfd,new_fd,binding, buf;       
	struct sockaddr_in server_addr;     
	struct sockaddr_in client_addr;    
	int cli_size,z;   
	pthread_t thr;               
	int yes=1;
	int acc=0;                       
 
	printf("\nMembangun Server pada PORT: %d\n", PORT);  
	
	h = MakeEmpty (NULL);	//Mengosongkan Link-list 
	
	//Membangun server 
	server_addr.sin_family=AF_INET;          
	server_addr.sin_addr.s_addr = htonl(INADDR_ANY); 
	server_addr.sin_port=htons(PORT);  
	printf("IP ADDRESS:\t%s\n",inet_ntoa(server_addr.sin_addr));  

	//Membangun socket  
	sockfd = socket(AF_INET, SOCK_STREAM, 0);  
	if(sockfd<0){  
		printf("Gagal membuat Socket\n");    
		exit(1);  
	}
	else  
		printf("Socket Berhasil Dibuat\n");  

	//Binding socket 
	binding = bind(sockfd, (struct sockaddr *)&server_addr, sizeof(struct sockaddr));
	if(binding<0){  
		printf("Binding Gagal\n");    
		exit(1);
	}  
	else   
		printf("Binding Berhasil\n");  

	//printf("\t\tPRESS CTRL+z TO VIEW ONLINE CLIENTS\n\n");  

	//Listening  
	listen(sockfd, 10);  
	printf("Menunggu clients......\n");  
	
	int tmpID;
	addr a;    
	while(1){  
		cli_size=sizeof(struct sockaddr_in); 
		
		//Menerima client
		new_fd = accept(sockfd, (struct sockaddr *)&client_addr,&cli_size);   
		if (new_fd>0){
			//strcpy (notif, "CONN");
			//send (new_fd,notif,strlen(notif),0); 
		}		
		
		a = h;
		printf ("%d\n", new_fd);		

		char *temp;
		int c=1;

		bzero(buffer, 2048);            
		buf = recv(new_fd,buffer,sizeof(buffer),0);
		printf ("%s\n", buffer);		

		if(strncmp (buffer, "CONN", 4) == 0) {
			printf ("Ada yang masuk\n"); 
			//Memisah komponen protocol
			
			temp = strtok (buffer, ":"); 
			while (temp!=NULL){
				if (c==2)
					strcpy (username, temp);
				if (c==3)
					strcpy (cli_e, temp);	
				if (c==4)
					strcpy (cli_n, temp);	
				temp = strtok (NULL, ":");
				c++;
				//printf ("%d %s %s %s\n", new_fd, username, cli_e, cli_n);
			}	

			
			if (cli_n[strlen(cli_n)-1]=='\n'){			
      				cli_n[strlen(cli_n)-1]='\0';	
			}			
			
			printf ("%d. %s baru saja masuk. Pubilckey (%s, %s)\n",new_fd,username,cli_e,cli_n);  

			bzero (buffer,2048);          
		
			//Menambhakan client ke link-list
			Insert (new_fd, username, cli_e, cli_n, h, a); 
			Display (h);  
			//Sendlist (h);  			

			printf("Ada client masuk dari: %s & %d\n\n",inet_ntoa(client_addr.sin_addr),new_fd); 
			struct Node args;                  
			args.id=new_fd;  
			strcpy (args.username,username);  
			pthread_create (&thr,NULL,server,(void*)&args);	//Membuat thread untuk masing" client   
			pthread_detach (thr);
			printf ("6,5\n"); 
		}
		else {
			printf ("Ada yang connect tapi gagal\n");
			strcpy (notif, "Input tidak sesuai protocol/n");
			send (new_fd,notif,strlen(notif),0); 
		}		  
	} 
	printf ("7\n");            
	close (sockfd);
	printf ("8\n");  
}

void *server (void *arguments){  
	struct Node *args=arguments;  
	char buffer[4096], uname[32], sender[32], rcv[32], key[1024], msg[2048], buf[4096], hash[1024];              
	char *temp; 
	int ts_fd, c, y; 
	ts_fd = args->id;   
	strcpy (uname,args->username);   
	addr a;  
	printf ("1\n");
	//Mengirim list online	
	Sendlist (h);
	printf ("2\n");
	//proses chatting
	while(1){
		bzero(buffer,4096);  
		bzero(buf,4096);
		bzero(uname,32);
		bzero(sender,32);
		bzero(rcv,32);
		bzero(msg,2048);		
		bzero(key,1024);
		bzero(hash,1024);
		y=recv(ts_fd,buffer,4096,0);  
		strcpy (buf,buffer);
		if (y==0){
			printf ("(%d) %s keluar\n",ts_fd,uname);  
			addr a = h ; 
			do{  
				a = a->next;   
				if(a->id == ts_fd)   
					Delete (a->id, h);  
			}while ( a->next != NULL );  
			//printf ("4\n");

			Sendlist (h);
			//printf ("5\n");
			Display( h );  
			//close(ts_fd);
			//printf ("6\n");
		}
		else{
			//Jika user mengirim pesan
			if (strncmp (buffer, "SEND", 4) == 0){
				//printf ("3\n");
				//Memisahkan komponen pesan
				temp = strtok (buffer, ":"); 
				c=1;
				while (temp!=NULL){
					if (c==2)
						strcpy (sender, temp);	
					if (c==3)
						strcpy (rcv, temp);	
					if (c==4)
						strcpy (msg, temp);
					if (c==5)
						strcpy (key, temp);
					if (c==6)
						strcpy (hash, temp);			
					temp = strtok (NULL, ":");
					c++;
				}
				printf ("%s mengirimkan pesan ke %s\nPesan   : %s\nKey     : %s\nHash Val: %s\n", sender, rcv, msg, key, hash);
		
				//Mengirimkan pesan
				a = h;
				strcat (buffer,"\n");
				do{  
					a = a->next; 
					if(strcmp (rcv, a->username) == 0)   
						send(a->id,buf,strlen(buf),0);  
				} while( a->next != NULL );  					
			} 

			//Jika user disconnect
			else if ( strncmp( buffer, "DCON", 4) == 0 ){  
				printf ("(%d) %s keluar\n",ts_fd,uname);  
				addr a = h ; 
				do{  
					a = a->next;   
					if(a->id == ts_fd)   
						Delete (a->id, h);  
				}while ( a->next != NULL );  
				//printf ("4\n");

				Sendlist (h);
				//printf ("5\n");
				Display( h );  
				close(ts_fd);
				//printf ("6\n");
				//return 0;  
			}  
			//else 
			//	printf ("Format pesan dari %s salah\n", uname);
			
		}  
	}
 }

//Me-NULL kan linklist
head MakeEmpty (head h) {  
	if (h != NULL) 
		 DeleteList (h); 
	
	h = malloc (sizeof(struct Node));  
	if (h == NULL)  
		printf ("Out of memory!");  
	h->next = NULL;  
	return h;
}  

void DeleteList( head h ) {  
	addr a, Tmp;  
	a = h->next;   
	h->next = NULL;  
	while( a != NULL ) {  
		Tmp = a->next;  
		free( a );  
		a = Tmp;  
	}  
}

//Deletelist 
void Delete (int port, head h){  
	addr a, TmpCell;  

	a=h;
	while (a->next != NULL && a->next->id != port)  
		a = a->next;
  	a->next->online=0;
	
	/*if (a->next != NULL){             
		TmpCell = a->next;  
		a->next = TmpCell->next;   
		free (TmpCell);  
	} */ 
} 

//Insertlist
void Insert (int port, char *username, char *cli_e, char *cli_n, head h, addr a) {  
	addr TmpCell;  
	TmpCell = malloc (sizeof(struct Node)); 
	int ada =0;
	a=h; 
	
	if (TmpCell == NULL)  
		printf ("Out of space!!!");  
	else{			
		while( a->next != NULL){
			if (strcmp (a->next->username, username)==0){
				ada=1;
				break;			
			}			
			a = a->next; 
		}  
		
		if (ada==1){
			a = a->next; 
			a->online=1;		
			a->id = port;
			bzero (a->pub_e, 1024);
			bzero (a->pub_n, 1024);   
			strcpy (a->pub_e,cli_e);  
			strcpy (a->pub_n,cli_n);	
			printf ("%s kembali online\n", a->username);	
		}
		else{
			//a=h;
			TmpCell->id = port;   
			strcpy (TmpCell->username,username);
			strcpy (TmpCell->pub_e,cli_e);  
			strcpy (TmpCell->pub_n,cli_n);
			TmpCell->online=1;
			TmpCell->next = a->next;  
			a->next = TmpCell;
			printf ("Ada user baru: %s\n", a->next->username);
		}			
		//printf ("%d %s (%s, %s) status: %d\n", TmpCell->id, TmpCell->username, TmpCell->pub_e, TmpCell->pub_n, TmpCell->online);  
	}
}  

//Diplay LInklist
void Display (head h) {  
	addr a = h ;  
	if( h->next == NULL )  
		printf( "Tidak ada client yang online\n" );  
	else {  
		do {  
			a = a->next;  			
			printf( "%d. %s (%s, %s) status: %d --> ", a->id,a->username,a->pub_e,a->pub_n,a->online);  
		} while( a->next != NULL );  
		printf( "\n" );  
	}  
}  

void Sendlist (head h) {
	addr a;
	addr b = h;
	char *buf;	
	
	if (h->next != NULL){
		do {
			a =h;
			b = b->next;
			do { 
				a = a->next;
				if (a->online==1){
					bzero (buf,256); 
					strcpy (buf,"LIST:");
					strcat (buf,a->username);
					strcat (buf,":");
					strcat (buf,a->pub_e);
					strcat (buf,":");
					strcat (buf,a->pub_n); 
					strcat (buf,":\n");
					//strcat (buf,"\n");  
					send (b->id,buf,strlen(buf),0); 
				} 
			} while( a->next != NULL );  
			bzero (buf,256);	
			strcpy (buf, "DONE\n");
			send (b->id,buf,strlen(buf),0); 
		} while( b->next != NULL );  
	}
}
