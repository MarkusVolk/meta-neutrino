#!/bin/sh
#
# Home Sharing script
#
# May only be used if and only if it doesn't violate your cable or sat
# providers terms of service.
#
# Inactive by default. Fill the gaps, and link to /etc/rc5.d to activate.

PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/var/bin:
DAEMON=doscam
DAEMON_OPTS="-b"
NAME=doscam
DESC=

cam_start() {
	echo -n "Starting $DESC: "
	$DAEMON $DAEMON_OPTS 	
	echo "done."
	
}

cam_stop() {
	echo -n "Stopping $DESC: "
	killall  $NAME
	echo "done."
	
}

case $1 in
start)
	cam_start
	;;
stop)
	cam_stop
	;;
restart)
	cam_stop
	sleep 2
	cam_start
	sleep 2
	/usr/bin/pzapit -rz
	;;
init)
	sleep 2
	cam_start
	if grep lastChannelTVScrambled=true /etc/neutrino/config/zapit/zapit.conf
	then
		sleep 5
		/usr/bin/pzapit -rz
	fi	
esac
