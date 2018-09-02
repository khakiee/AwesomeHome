import requests
import time
import wiringpi2 as wp

# pip install -r requirements.txt
# pip install RPi.GPIO

switches = (5, 23, 20, 21)  # relay pin number


def request(url, params, http_method, headers=None):
    kwargs = dict()

    if http_method == 'POST':
        kwargs['data'] = params

    if headers:
        kwargs['headers'] = headers

    try:
        if http_method == 'DELETE':
            rr = requests.delete(url, **kwargs)
        elif http_method == 'HEAD':
            rr = requests.head(url, **kwargs)
        elif http_method == 'GET':
            rr = requests.get(url, **kwargs)
        elif http_method == 'OPTIONS':
            rr = requests.options(url, **kwargs)
        elif http_method == 'PATCH':
            rr = requests.patch(url, params, **kwargs)
        elif http_method == 'POST':
            rr = requests.post(url, **kwargs)
        elif http_method == 'PUT':
            rr = requests.put(url, params, **kwargs)
        else:
            print('unsupported HTTP method')
            return False
    except Exception as ee:
        print('failed to api call: %s' % str(ee))
        return False

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
        rr = request("http://13.209.64.184/input", dict(), "GET")
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