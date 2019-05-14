#!/usr/bin/python

import urllib2, json, ssl, requests

ctx = ssl.create_default_context()
ctx.check_hostname = False
ctx.verify_mode = ssl.CERT_NONE

response = urllib2.urlopen("http://192.168.1.97/api/meters/aggregates", context=ctx)
data = json.loads(response.read())

print "load: " + str(data['load']['instant_power'])
print "battery: " + str(data['battery']['instant_power'])
print "solar: " + str(data['solar']['instant_power'])
print "site: " + str(data['site']['instant_power'])
print "load_wh: " + str(data['load']['instant_power']/3600)
print "battery_wh: " + str(data['battery']['instant_power']/3600.0)
print "solar_wh: " + str(data['solar']['instant_power']/3600.0)
print "site_wh: " + str(data['site']['instant_power']/3600.0)

url_string = 'http://localhost:8086/write?db=home'
r1 = requests.post(url_string, data='electricity,host=load value=' + str(data['load']['instant_power']))
r2 = requests.post(url_string, data='electricity,host=battery value=' + str(data['battery']['instant_power']))
r3 = requests.post(url_string, data='electricity,host=solar value=' + str(data['solar']['instant_power']))
r4 = requests.post(url_string, data='electricity,host=site value=' + str(data['site']['instant_power']))
r5 = requests.post(url_string, data='electricity,host=load_wh value=' + str(data['load']['instant_power']/3600.0))
r6 = requests.post(url_string, data='electricity,host=battery_wh value=' + str(data['battery']['instant_power']/3600.0))
r7 = requests.post(url_string, data='electricity,host=solar_wh value=' + str(data['solar']['instant_power']/3600.0))
r8 = requests.post(url_string, data='electricity,host=site_wh value=' + str(data['site']['instant_power']/3600.0))


#print r1.status_code
#print r2.status_code
#print r3.status_code
#print r4.status_code

response = urllib2.urlopen("http://192.168.1.97/api/system_status/soe", context=ctx)
data = json.loads(response.read())

print "percentage: " + str(data['percentage'])

url_string = 'http://localhost:8086/write?db=home'
r1 = requests.post(url_string, data='electricity,host=soe value=' + str(data['percentage']))

#print r1.status_code

response = urllib2.urlopen("http://192.168.1.97/api/system_status/grid_status", context=ctx)
data = json.loads(response.read())

print "grid_status: " + str(data['grid_status'])

status = 1
if 'SystemIslandedActive' == data['grid_status']:
	status = 0
elif 'SystemTransitionToGrid' == data['grid_status']:
	status = 2

url_string = 'http://localhost:8086/write?db=home'
r1 = requests.post(url_string, data='electricity,host=grid_status value=' + str(status))

#print r1.status_code