#!/usr/bin/env python
# coding: utf8

import urllib2
import json
import time

jsonData = {
    'Headquarters': {
      'WR-118412E': {
        'type': 'ELECTRICITY_CONSUMPTION_ENERGY_ABSOLUTE',
        'readings': [
          {
            'timestamp': int(time.time()),
            'value': 128013.0
          }
        ]
      }
    }
  }

url = "http://api.solar-fox.com/import"
headers = { "Content-Type": "application/json", "Authorization": "test" }
req = urllib2.Request(url, headers = headers, data = json.dumps(jsonData))

try:
    res = urllib2.urlopen(req)
except URLError as e:
    print e.reason
else:
    print "Status:", res.getcode()
    print "Headers:", dict(res.info())
    print "Body:", res.read()
