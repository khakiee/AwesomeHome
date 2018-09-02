import requests
import time
import wiringpi2 as wp

# pip install -r requirements.txt
# pip install RPi.GPIO

switches = (5, 23, 20, 21)  # relay pin number


def request(url):
    kwargs = dict()
    rr = requests.get(url, **kwargs)
    return rr


def init():
    wp.wiringPiSetupGpio()
    for i in switches:
        wp.pinMode(i,1)
        time.sleep(0.5)

def main():
    init()
    '''
    while(True):
        for idx in range(0,4):
            wp.pinMode(switches[idx],1)
            time.sleep(0.5)  
        for idx in range(0,4):
            wp.pinMode(switches[idx],0)
            time.sleep(0.5)
    '''
    while(True):
        rr = request("http://13.209.64.184/input")
        response = rr.content.decode()
        print(response)
        
        for idx in range(0, 4):
            if response[idx] == '0':
                print "oh"
                wp.pinMode(switches[idx],1)
                time.sleep(0.1)
            else:
                print "hoho"
                wp.pinMode(switches[idx],0)
                time.sleep(0.1)
    
    

if __name__ == '__main__':
    main()
