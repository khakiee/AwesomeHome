#include "SPI.h"
#include "WiFi.h"
#include <Wire.h>                        
#include "LiquidCrystal_I2C.h"      
LiquidCrystal_I2C lcd(0x3F,16,2);
char ssid[] = "KJH";       //와이파이 SSID
char pass[] = "12343212";   //와이파이 password 

byte dot[8] = { 
  0b00111,
  0b00101,
  0b00111,
  0b00000,
  0b00000,
  0b00000,
  0b00000,
  0b00000,
};

//인스턴스 변수 초기화
WiFiServer server(80);
WiFiClient client;
WiFiClient todoclient;
 
IPAddress hostIp;
IPAddress localIp;

char localIpAddress[] ="192.168.43.149";
uint8_t ret;
uint8_t localRet;

int loop_count = 0;
int temp = 0;

String weather_str="";
String wt_temp="";
String wt_wfKor="";
String wt_wfEn="";
String wt_reh="";

String reveal_string = "";

int loop_checker = 2;

String lcdInfo1 = "";
String lcdInfo2 = "";
boolean blankCheck = false;
    
void setup() {
  //각 변수에 정해진 공간 할당
  Serial.begin(115200);    
   lcd.init();                      // LCD 초기화
  // Print a message to the LCD.
  lcd.backlight();                // 백라이트 켜기
  lcd.createChar(1, dot);
  delay(10);
  //WiFi연결 시도
  Serial.println("Connecting to WiFi....");  
  initLcd();
  printLcd(0,"Connecting");
  printLcd(1,"to WiFi....");
  WiFi.begin(ssid, pass);  //WiFi가 패스워드를 사용한다면 매개변수에 password도 작성
  
  server.begin();
  Serial.println("Connect success!");
  initLcd();
  printLcd(0,"Connect success!");
  Serial.println("Waiting for DHCP address");
  printLcd(1,"Waiting...");
  //DHCP주소를 기다린다
  while(WiFi.localIP() == INADDR_NONE) {
    Serial.print(".");
    delay(300);
  }
  
  
  Serial.println("\n");
  printWifiData();
  initDatabase();
  connectToServer();
  connectToLocalServer();
 
}

void loop() {
  
  loop_count++;
  
  if (loop_count % 2 == 0) {

    while (client.available() && loop_checker) {
      
      String line = client.readStringUntil('\n');

      String wt_hour = "";
      String wt_temp = "";
      String wt_twfEn = "";
      String wt_reh = "";
      //시간
      int temp11= line.indexOf("</hour>");
      if(temp11>0) {
        String tmp_str="<hour>";
        wt_hour = line.substring(line.indexOf(tmp_str)+tmp_str.length(),temp11);
//        Serial.print("hour is "); 
        
//        Serial.println(wt_hour);  
      }
      
      //온도
      int temp= line.indexOf("</temp>");
      if(temp>0) {
        String tmp_str="<temp>";
        wt_temp = line.substring(line.indexOf(tmp_str)+tmp_str.length(),temp);
//        Serial.print("temperature is ");
//        Serial.println(wt_temp);  
        lcdInfo1 = wt_temp+" C / ";
      }
      
      //날씨 정보
      int wfEn= line.indexOf("</wfEn>");
      if(wfEn>0) {
        String tmp_str="<wfEn>";
        wt_twfEn = line.substring(line.indexOf(tmp_str)+tmp_str.length(),wfEn);
//        Serial.print("weather is ");
//        Serial.println(wt_twfEn);  
        lcdInfo2 = wt_twfEn;
      }
      
      //습도
      int reh= line.indexOf("</reh>");
      if(reh>0) {
        String tmp_str="<reh>";
        wt_reh = line.substring(line.indexOf(tmp_str)+tmp_str.length(),reh);
//        Serial.print("Humidity is ");
//        Serial.println(wt_reh);
        lcdInfo1 += wt_reh+"%"+" SU";
      }
    }
        lcd.clear();
        printLcd(0, lcdInfo1);
        printLcd(1, lcdInfo2);
        lcd.setCursor(4,0);
        lcd.write(byte(1));
        Serial.println(lcdInfo1);
        Serial.println(lcdInfo2);
        loop_checker--;
        delay(3000);   
  }
  
if(loop_count % 2 == 1){
  
  while (todoclient.available()&&loop_checker) {
    String event_data = "";
    String line = todoclient.readStringUntil('\n');
    int event_data_line= line.indexOf("</li>");
    if(event_data_line>0) {
      String tmp_str="<li>";
      event_data = line.substring(line.indexOf(tmp_str)+tmp_str.length(), event_data_line);
      Serial.println(event_data);  
      reveal_string = event_data;
      }
     loop_checker--;
    }
    lcd.clear();
    printLcd(0, reveal_string);
    printLcd(1, "+ more task!!");
    delay(3000);
    
  }

  
  
}

//서버와 연결
void connectToServer() {
  Serial.println("connecting to server...");
  initLcd();
  printLcd(0,"connecting");
  printLcd(1,"to server...");
  String content = "";
  if (client.connect(hostIp, 80)) {
    Serial.println("Connected! Making HTTP request to www.kma.go.kr");
    initLcd();
    printLcd(0,"Connected!");
    printLcd(1,"");
    //Serial.println("GET /data/2.5/weather?q="+location+"&mode=xml");
    client.println("GET /wid/queryDFSRSS.jsp?zone=4111571000 HTTP/1.1\r"); 
    //위에 지정된 주소와 연결한다.
    client.print("HOST: www.kma.go.kr\r\n");
//    client.println("User-Agent: launchpad-wifi");
    client.println("Connection: close");
    
    client.println();
    Serial.println("Weather information for ");
  }
}

void connectToLocalServer() {
  Serial.println("connecting to server...");
  initLcd();
  printLcd(0,"connecting");
  printLcd(1,"to server...");
  String content = "";
 
  if (todoclient.connect(localIp, 3000)) {
    Serial.println("Connected! Making HTTP request to localhost");
    initLcd();
    printLcd(0,"Connected!");
    printLcd(1,"");
    todoclient.println("GET /test HTTP/1.1"); 
    //위에 지정된 주소와 연결한다.
    todoclient.print("Host: ");
    todoclient.print(localIpAddress);
    
    todoclient.println("Connection: close");
    
    todoclient.println();
    Serial.println("To do list ");
  }
}

void initDatabase(){
  Serial.println("Init Database");
  if(todoclient.connect(localIp, 3000)){
    Serial.println("Database Connected!!");
    initLcd();
    printLcd(0,"Connected!");
    printLcd(1,"");
    todoclient.println("GET /calendars HTTP/1.1"); 
    //위에 지정된 주소와 연결한다.
    todoclient.print("Host: ");
    todoclient.print(localIpAddress);

//    client.println("User-Agent: launchpad-wifi");
    todoclient.println("Connection: close");
    todoclient.println();
  }
}

void printHex(int num, int precision) {
  char tmp[16];
  char format[128];
  
  sprintf(format, "%%.%dX", precision);
  
  sprintf(tmp, format, num);
  Serial.print(tmp);
}

void printWifiData() {
  // Wifi쉴드의 IP주소를 출력
  Serial.println();
  Serial.println("IP Address Information:");  
  IPAddress ip = WiFi.localIP();
  localIp = ip;
  Serial.print("IP Address: ");
  Serial.println(ip);
  
  //MAC address출력
  byte mac[6];  
  WiFi.macAddress(mac);
  Serial.print("MAC address: ");
  printHex(mac[5], 2);
  Serial.print(":");
  printHex(mac[4], 2);
  Serial.print(":");
  printHex(mac[3], 2);
  Serial.print(":");
  printHex(mac[2], 2);
  Serial.print(":");
  printHex(mac[1], 2);
  Serial.print(":");
  printHex(mac[0], 2);
  Serial.println();
  //서브넷 마스크 출력
  IPAddress subnet = WiFi.subnetMask();
  Serial.print("NetMask: ");
  Serial.println(subnet);
  
  //게이트웨이 주소 출력
  IPAddress gateway = WiFi.gatewayIP();
  Serial.print("Gateway: ");
  Serial.println(gateway);
  
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());
  
  ret = WiFi.hostByName("www.kma.go.kr", hostIp);
  localRet = WiFi.hostByName(localIpAddress, localIp);
  Serial.print("ret: ");
  Serial.println(ret);
  Serial.print("local ret: ");
  Serial.println(localRet);
  Serial.print("Host IP: ");
  Serial.println(hostIp);
  Serial.println("");
}
void initLcd(){
  for(int i = 0; i < 2; i++){
    for(int j = 0; j <16; j ++ ){
      lcd.setCursor(j, i); 
      lcd.print(" ");    
    }
  }
}
void printLcd(int line, String info){
  lcd.setCursor(0,line);
  lcd.print(info);
}

int getInt(String input){
  int i = 2;
  
  while(input[i] != '"'){
    i++;
  }
  input = input.substring(2,i);
  char carray[20];
  //Serial.println(input);
  input.toCharArray(carray, sizeof(carray));
  //Serial.println(carray);
  temp = atoi(carray);
  //Serial.println(temp);
  return temp;
}
