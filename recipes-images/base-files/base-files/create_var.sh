#!/bin/sh

PATH=/bin:/usr/bin:/sbin:/usr/sbin

# do not mount or update var, if network is up at this point, ie nfs boot
# the solution from original cst script is not working with iproute2 here.
# changed for better compatibility
cat /sys/class/net/eth0/operstate | grep -qi "up" && exit

# check if we are using gnu coreutils
if [ -e /usr/bin/cut.coreutils ]; then
# coreutils "cut" counts from 1
VARDEV=`grep -i var /proc/mtd | cut -f 1 -s -d :`
else
# busybox "cut" counts from 0
VARDEV=`grep -i var /proc/mtd | cut -f 0 -s -d :`
fi
if [ -z $VARDEV ]; then
        echo no var partition found
else
        if [ ! -d /var_init ]; then
                echo Rename /var to /var_init
                /bin/mv /var /var_init
		if [ ! -h /etc/network/interfaces ]; then
			cp /etc/network/interfaces /var_init/etc/network/
		fi
        fi
        if [ ! -d /var ]; then
                /bin/mkdir /var
        fi

# Factory reset
	if [ -f /var_init/etc/.reset ]; then
		echo Factory reset, erasing var /dev/$VARDEV
		/bin/rm /var_init/etc/.reset
		/bin/cp -rf /var/lib/opkg /var_init/lib/opkg
		touch /var/init/etc/.newimage
               	/usr/sbin/flash_eraseall /dev/$VARDEV
	fi

# Mount var
        VARBLOCK=`grep -i var /proc/mtd | cut -b 4`
        echo mounting /dev/mtdblock$VARBLOCK to /var
        /bin/mount -t jffs2 /dev/mtdblock$VARBLOCK /var
        if [ $? != 0 ]; then
                echo Erasing var /dev/$VARDEV
                /usr/sbin/flash_eraseall /dev/$VARDEV
                echo mounting /dev/mtdblock$VARBLOCK to /var
                /bin/mount -t jffs2 /dev/mtdblock$VARBLOCK /var
        fi
        if [ $? != 0 ]; then
                echo failed to mount /var
                /bin/rmdir /var && /bin/mv /var_init /var
        else

# Move /etc/network/interfaces to /var/etc/network/interfaces
	if [ ! -h /etc/network/interfaces ]; then
		rm /etc/network/interfaces
		if [ -e /var/etc/network/interfaces ]; then
			ln -sf /var/etc/network/interfaces /etc/network/interfaces
			if [ /var/etc/network/interfaces -nt /var_init/etc/network/interfaces ]; then
				cp -a /var/etc/network/interfaces /var_init/etc/network/
			fi
		else
			cp /var_init/etc/network/interfaces /var/etc/network/interfaces
			ln -sf /var/etc/network/interfaces /etc/network/interfaces
			fi
		fi

# Copy var_init to var, if partition is empty
		if [ ! -d /var/tuxbox ]; then
                        /bin/cp -a /var_init/* /var/
                fi
                if [ -f /var_init/etc/.newimage ]; then
			/bin/rm /var_init/etc/.newimage
			/bin/cp /var_init/tuxbox/config/cables.xml /var/tuxbox/config/cables.xml
			/bin/cp /var_init/tuxbox/config/satellites.xml /var/tuxbox/config/satellites.xml
			/bin/cp /var_init/tuxbox/config/encoding.conf /var/tuxbox/config/encoding.conf
			/bin/cp /var_init/tuxbox/config/providermap.xml /var/tuxbox/config/providermap.xml
			echo updating /var/lib/opkg ...
			/bin/cp -rf /var_init/lib/opkg /var/lib/
                fi
        fi
fi
