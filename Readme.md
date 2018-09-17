# Hantor Summer Project

## Google Drive Spreadsheet
Add requirement to below link

## Android
API Version 28
- dependencies

<pre><code>
	apply plugin: 'com.android.application'

android {
    compileSdkVersion 28
    defaultConfig {
        applicationId "kjh.hantor.hantor_smarthome"
        minSdkVersion 15
        targetSdkVersion 28
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
repositories {
    jcenter()
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:28.0.0-rc01'
    implementation 'com.android.support.constraint:constraint-layout:1.1.2'
    implementation 'com.android.support:support-v4:28.0.0-rc01'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
    api 'com.squareup.okhttp3:okhttp:3.8.0'
    api 'com.jpardogo.materialtabstrip:library:1.1.1'
}

</code></pre>

## Web, Server

## Arduino and Rpi
### Raspberry Pi

와이파이 IP가 바뀌는 경우 -> serial 통신

mac -> sudo screen -L /dev/tty.usbserial -L 9600

경로로 이동 - cd HantorSummerProject/Rpi

- InAndOut
	cd InAndOut
	sudo python Door.py&
	sudo python RFID.py

	실행 후 버튼 몇번 누르면 동작

- plug
	cd plug
	sudo python ConControl.py

### Calendar and weather

- weather
	hantor_home_iot_ino file
	```
	char ssid[] = "your AP's id"
	char pass[] = "yout AP's password"
	```
	hotspot recommanded

	```
	char localIpAddress[] ="Your Local Computer's ip";
	```


- ruby server
	requirements : postgreSQL, rbenv, ruby == 2.5.1, rails
	
	db create
	```
	rake db:create
	```

	server start
	```
	sudo rails s
	```

	http://localhost:3000/calendar -> db에 일정 저장
	http://localhost:3000/redirect -> google 아이디로 가입
	http://localhost:3000/test -> arduino에서 표시되는 일정 