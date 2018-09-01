#include <SPI.h>
#include <Phpoc.h>
const int httpPort = 80;

int Relaypin1 = 7;                 // IN1
int Relaypin2 = 6;                 // IN2
int Relaypin3 = 5;                 // IN3
int Relaypin4 = 4;                 // IN4
int c1=0;
int c2=0;
int c3=0;
int c4=0;
//char server_name[] = "www.google.com";
//char server_name[] = "216.58.221.36";
char server_name[] = "13.209.64.184";
String info;
void setup() {
  Serial.begin(9600);
  while(!Serial)
    ;  
  Phpoc.begin();
 
  Serial.println("IP address: ");
  Serial.println(Phpoc.localIP());

  pinMode(Relaypin1,OUTPUT);         // 릴레이 제어 1번핀을 출력으로 설정
  pinMode(Relaypin2,OUTPUT);         // 릴레이 제어 2번핀을 출력으로 설정
  pinMode(Relaypin3,OUTPUT);         // 릴레이 제어 3번핀을 출력으로 설정
  pinMode(Relaypin4,OUTPUT);         // 릴레이 제어 4번핀을 출력으로 설정
}

void loop() {
 connect_output();
 consent();
 connect_index();
}
void connect_output(){
  PhpocClient client;
  // Use WiFiClient class to create TCP connections
  
  if (!client.connect(server_name, httpPort)) {
    Serial.println("connection failed");
    return;
  }
  
  client.println(String("GET ")+ "/output HTTP/1.1");
  client.println("Host: 13.209.64.184");
  client.println("Connection: open");
  client.println();
  Serial.println("First Request sent");

  int timeout = millis() + 500;
  while (client.available() == 0) {
    if (timeout - millis() < 0) {
      Serial.println(">>> Client Timeout !");
      client.stop();
      return;
    }
  }
  while (client.connected())
  {  
    String line = client.readStringUntil('\n');
    if(line.charAt(0)>47&&line.charAt(0)<58){
       Serial.println(line);
       c1=(int)line.charAt(0)-48;
       c2=(int)line.charAt(1)-48;
       c3=(int)line.charAt(2)-48;
       c4=(int)line.charAt(3)-48;
       Serial.println(c1);
       Serial.println(c2);
       Serial.println(c3);
       Serial.println(c4);
    }
  }
  client.stop();
}

void connect_index(){
  PhpocClient client;
  // Use WiFiClient class to create TCP connections
  
  if (!client.connect(server_name, httpPort)) {
    Serial.println("connection failed");
    return;
  }
  
  client.println(String("GET ")+ "/index2.php?b1="+c1+"&b2="+c2+"&b3="+c3+"&b4="+c4+" HTTP/1.1");
  client.println("Host: 13.209.64.184");
  client.println("Connection: close");
  client.println();
  Serial.println("Request sent");

 int timeout = millis() + 500;
  while (client.available() == 0) {
    if (timeout - millis() < 0) {
      Serial.println(">>> Client Timeout !");
      client.stop();
      return;
    }
  }

  while (client.connected())
  {  
    String line = client.readStringUntil('\n');
       Serial.println(line);
  }
  client.stop();
}
void consent() {
  digitalWrite (Relaypin1, c1); // 릴레이 ON
  digitalWrite (Relaypin2, c2); // 릴레이 ON
  digitalWrite (Relaypin3, c3); // 릴레이 ON
  digitalWrite (Relaypin4, c4); // 릴레이 ON
 
  delay (500);
}
