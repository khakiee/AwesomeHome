import RPi.GPIO as gpio
import MFRC522
import signal
import time
import requests


continue_reading = True
gpio.setmode(gpio.BOARD)
gpio.setup(11, gpio.OUT)
gpio.setup(12, gpio.OUT)

def end_read(signal, frame):
    global continue_reading
    print "\nCtrl+c\n"
    continue_reading = False
    gpio.cleanup()

signal.signal(signal.SIGINT, end_read)

MIFAREReader = MFRC522.MFRC522()

while continue_reading:
    fp = open("button_pressed.txt","r")
    inp = fp.readline()
    while inp == None:
        continue
    if "0" in inp:
        (status, TagType) = MIFAREReader.MFRC522_Request(MIFAREReader.PICC_REQIDL)
        if status == MIFAREReader.MI_OK:
            print "Card Detected"
        (status,uid) = MIFAREReader.MFRC522_Anticoll()
        if status == MIFAREReader.MI_OK:
            print "Read UID : %s,%s,%s,%s"%(uid[0], uid[1], uid[2], uid[3])
            key = [0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF]
            MIFAREReader.MFRC522_SelectTag(uid)
            status = MIFAREReader.MFRC522_Auth(MIFAREReader.PICC_AUTHENT1A, 8, key, uid)
            if status == MIFAREReader.MI_OK:
                print("people in")
                MIFAREReader.MFRC522_StopCrypto1()
                gpio.output(12,gpio.LOW)
                gpio.output(11,gpio.HIGH)
                fp = open("button_pressed.txt","w+")
                fp.write("1")
                requests.get("http://13.125.227.46/ultra.php?num=1")
                
            else :
                print "Auth err"
