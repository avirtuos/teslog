If you are in the market for a tesla, feel free to ask me questions and if you feel like donating to the cause you can always use my referral code [http://ts.la/anthony4325](http://ts.la/anthony4325)

I anticipate the first version of this project to be posted in Jan 2018.

# teslog
A self-contained telemetry tracker for Tesla vehicles that doesn't require handing over your credentials to a 3rd party!

I'm a huge fan of teslafi and encourage everyone to give it a try. It just wasn't right for me because I didn't want to give my credentials (or API token) over to a 3rd party. Until Tesla offers a way to restrict access to read-only APIs (e.g. not the ones that allow you to remote summon the car or open/close doors), access to your tesla telemetry is the same as handing over your keyfob. 

So this project will take shape over the next few months in my spare time and will see its first release after I get my Model X :) but here is what I am planning:

1. Self-contained docker container for gathering and viewing telemetry.
2. Historical Telemetry via MariaDB
3. Option to deploy to AWS Elastic Container Service if you don't want to leave your home computer on.
4. Ability to enable / disable logging via email so you can reduce vampire drain.
5. Will largely be written in either Python or Java.
6. Will use RRDTool for graphs and long term storage (10+ years)



# Docker Instructions

## Pre-requisites

* Docker

## Build the container from scratch

```
$ docker build -t teslog Docker/
```

```
$ docker run -i -t teslog
```

## Download and run prebuilt container from Docker Hub
