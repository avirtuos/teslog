#!/usr/bin/python

import urllib2, json, ssl, requests, time

ctx = ssl.create_default_context()
ctx.check_hostname = False
ctx.verify_mode = ssl.CERT_NONE

data_prev=None
while 1:
    try:
		response = urllib2.urlopen("http://192.168.1.97/api/meters/aggregates", context=ctx)
		data = json.loads(response.read())

		print "load: " + str(data['load']['instant_power'])
		print "battery: " + str(data['battery']['instant_power'])
		print "solar: " + str(data['solar']['instant_power'])
		print "site: " + str(data['site']['instant_power'])

		if data_prev != None:
			battery_wh = data['battery']['energy_imported'] - data_prev['battery']['energy_imported'] + data['battery']['energy_exported'] - data_prev['battery']['energy_exported']
			battery_in_wh = data['battery']['energy_imported'] - data_prev['battery']['energy_imported']
			battery_out_wh = data['battery']['energy_exported'] - data_prev['battery']['energy_exported']
			solar_wh = data['solar']['energy_imported'] - data_prev['solar']['energy_imported'] + data['solar']['energy_exported'] - data_prev['solar']['energy_exported']
			solar_in_wh = data['solar']['energy_imported'] - data_prev['solar']['energy_imported']
			solar_out_wh = data['solar']['energy_exported'] - data_prev['solar']['energy_exported']
			site_wh = data['site']['energy_imported'] - data_prev['site']['energy_imported'] + data['site']['energy_exported'] - data_prev['site']['energy_exported']
			site_in_wh = data['site']['energy_imported'] - data_prev['site']['energy_imported']
			site_out_wh = data['site']['energy_exported'] - data_prev['site']['energy_exported']
			load_wh = data['load']['energy_imported'] - data_prev['load']['energy_imported'] + data['load']['energy_exported'] - data_prev['load']['energy_exported']
			load_in_wh = data['load']['energy_imported'] - data_prev['load']['energy_imported']
			load_out_wh = data['load']['energy_exported'] - data_prev['load']['energy_exported']

			print "battery: total wh: " + str(battery_wh) + " in: " + str(battery_in_wh) + " out: " + str(battery_out_wh)
			print "solar: total wh: " + str(solar_wh) + " in: " + str(solar_in_wh) + " out: " + str(solar_out_wh)
			print "load: total wh: " + str(load_wh) + " in: " + str(load_in_wh) + " out: " + str(load_out_wh)
			print "site: total wh: " + str(site_wh) + " in: " + str(site_in_wh) + " out: " + str(site_out_wh)

			r5 = requests.post(url_string, data='electricity,host=load_wh value=' + str(load_wh))
			r5_1 = requests.post(url_string, data='electricity,host=load_in_wh value=' + str(load_in_wh))
			r5_2 = requests.post(url_string, data='electricity,host=load_out_wh value=' + str(load_out_wh))

			r6 = requests.post(url_string, data='electricity,host=battery_wh value=' + str(battery_wh))
			r6_1 = requests.post(url_string, data='electricity,host=battery_in_wh value=' + str(battery_in_wh))
			r6_2 = requests.post(url_string, data='electricity,host=battery_out_wh value=' + str(battery_out_wh))

			r7 = requests.post(url_string, data='electricity,host=site_wh value=' + str(site_wh))
			r7_1 = requests.post(url_string, data='electricity,host=site_in_wh value=' + str(site_in_wh))
			r7_2 = requests.post(url_string, data='electricity,host=site_out_wh value=' + str(site_out_wh))

			r8 = requests.post(url_string, data='electricity,host=solar_wh value=' + str(solar_wh))
			r8_1 = requests.post(url_string, data='electricity,host=solar_in_wh value=' + str(solar_in_wh))
			r8_2 = requests.post(url_string, data='electricity,host=solar_out_wh value=' + str(solar_out_wh))

		
		data_prev = data

		url_string = 'http://localhost:8086/write?db=home'
		r1 = requests.post(url_string, data='electricity,host=load value=' + str(data['load']['instant_power']))
		r2 = requests.post(url_string, data='electricity,host=battery value=' + str(data['battery']['instant_power']))
		r3 = requests.post(url_string, data='electricity,host=solar value=' + str(data['solar']['instant_power']))
		r4 = requests.post(url_string, data='electricity,host=site value=' + str(data['site']['instant_power']))


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
    except Exception as e:
    	print "Failed to gather data: "+ str(e)
    print "Sleeping..."
    time.sleep(1)
	#print r1.status_code