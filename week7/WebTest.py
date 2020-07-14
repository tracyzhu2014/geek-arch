import time
import threading

import urllib
import urllib.request, urllib.error, urllib.parse

respon_timelist = []

def testWeb(value1, value2=None):
    print("enter %s threading %s(%s)" % (threading.current_thread().name, value1, value2))
    begin = time.time()
    sendHttpGet(value2)
    end = time.time()
    spent= end - begin
    respon_timelist.append(spent)
    print("done %s threading %s(%f)" % (threading.current_thread().name, value1, spent))
    return value1

def sendHttpGet(url):
    req = urllib.request.Request(url, method= 'GET')
    response = urllib.request.urlopen(req)
    #the_page = response.read().decode()
    #the_page = None
    #print(the_page)

def compute_percent():
    import numpy as np
    a = np.array(respon_timelist)
    rate = np.percentile(a, 95)
    total = 0
    for temp in respon_timelist :
        total = total + temp
    ilen = respon_timelist.__len__()
    avgtime = total/ilen
    print("avgtime=%.3f, 95 percent is less than %.3f" %  (avgtime, rate))


'''
测试运行结果为：
avgtime=0.409,    95 percent is less than 1.081
'''
if __name__ == "__main__":
    from concurrent.futures import ThreadPoolExecutor
    URL = "http://www.baidu.com" #压测web地址
    THREAD_NUM = 10  #并发数
    REQ_COUNT = 1000  # 请求数
    threadPool = ThreadPoolExecutor(max_workers=THREAD_NUM, thread_name_prefix="test_baidu")
    for i in range(0,REQ_COUNT):
        future = threadPool.submit(testWeb, i,URL)
    threadPool.shutdown(wait=True)
    compute_percent()