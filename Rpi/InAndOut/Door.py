import RPi.GPIO as gpio
import MFRC522 as mfrc
import signal
import time
import requests

gpio.setmode(gpio.BOARD)
gpio.setup(11,gpio.OUT)
gpio.setup(12,gpio.OUT)
gpio.setup(13,gpio.IN, pull_up_down=gpio.PUD_UP)

con= True
current_state = True

fp = open("button_pressed.txt","w+")

def end_read(signal,frame):
    global con
    print"ctrl+c"
    con = False
    gpio.cleanup()

signal.signal(signal.SIGINT, end_read)

while con:
    rfid_in = fp.readline()
    if rfid_in == '1':
        print("rfid input")
    btn_in = gpio.input(13)
    if(btn_in == False and current_state == True):
        print("people out")
        requests.get("http://13.125.227.46/ultra.php?num=0")
        gpio.output(11,gpio.LOW)
        gpio.output(12,gpio.HIGH)
        fp.write("0")
    current_state = btn_in
    time.sleep(0.1)
