import requests

us1 = open("us_1.txt",'r')
us2 = open("us_2.txt",'r')

us_stat = [0,0]

while True:
    if us1.read() < 10:
        us_stat[0] = 1
    if us2.read() < 10:
        us_stat [1] = 1
    if us_stat[0] == 1 and us_stat[1] == 1:
        requests.get(url,dict(),)
