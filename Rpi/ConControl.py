import requests
import time
import RPi.GPIO as GPIO

# pip install -r requirements.txt로 설치하십셔

switches = [18, 19, 20, 21]  # 릴레이 핀번호


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
    GPIO.setmode(GPIO.BCM)

    GPIO.setup(switches[0], GPIO.OUT)  # gpio18번 셋업 ->릴레이모듈
    GPIO.setup(switches[1], GPIO.OUT)  # gpio18번 셋업 ->릴레이모듈
    GPIO.setup(switches[2], GPIO.OUT)  # gpio18번 셋업 ->릴레이모듈
    GPIO.setup(switches[3], GPIO.OUT)  # gpio18번 셋업 ->릴레이모듈

    time.sleep(0.5)


def main():
    switches = init()

    while(True):
        rr = request("http://13.209.64.184/input", dict(), "GET")
        response = rr.content.decode()

        for idx in range(0, 4):
            if response[idx] == '0':
                GPIO.output(switches[idx], True)
            else:
                GPIO.output(switches[idx], False)


if __name__ == '__main__':
    main()
