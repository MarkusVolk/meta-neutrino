SUMMARY = "Neutrino HD"
DESCRIPTION = "CST Neutrino HD for Coolstream Settop Boxes."
HOMEPAGE = "http://git.coolstreamtech.de"
SECTION = "libs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS_append += " \
	giflib \
	virtual/stb-hal-libs \
	curl \
	libmad \
	freetype \
	freetype-native \
	libbluray \
	libid3tag \
	libpng \
	jpeg \
	libdvbsi++ \
	ffmpeg \
	flac \
	tremor \
	libsigc++ \
	openthreads \
	lua5.2 \
	luaposix \
"
DEPENDS_coolstream-hd2 += " \
	libiconv \
"

RDEPENDS_${PN} += " \
	tzdata \
	luaposix \
"

RCONFLICTS_${PN} = "neutrino-hd2"

SRCREV = "${AUTOREV}"
PV = "2.15+${SRCPV}"
SRC_URI = " \
	git://coolstreamtech.de/cst-public-gui-neutrino.git;protocol=git;branch=cst-next \
	file://neutrino.init \
	file://timezone.xml \
	file://custom-poweroff.init \
	file://pre-wlan0.sh \
	file://post-wlan0.sh \
	file://COPYING.GPL \
	file://0001-configure_fix.patch \
	file://0002-Y_Tools_Screenshot.yhtm_adjust-hardcoded-path-for-yo.patch \
	file://0003-workaround-wiped-out-resolv.conf-at-boot.patch \
	file://0004-change-version.h-output.patch \
	file://0005-xupnpd_youtube_fix.patch \
"

S = "${WORKDIR}/git"

inherit autotools pkgconfig update-rc.d

INITSCRIPT_PACKAGES   = "${PN}"
INITSCRIPT_NAME_${PN} = "neutrino"
INITSCRIPT_PARAMS_${PN} = "start 99 5 . stop 20 0 1 2 3 4 6 ."

include neutrino-hd.inc

do_configure_prepend() {
	INSTALL="`which install` -p"
	export INSTALL
}

do_compile () {
	# unset CFLAGS CXXFLAGS LDFLAGS
	oe_runmake CFLAGS="${N_CFLAGS}" CXXFLAGS="${N_CXXFLAGS}" LDFLAGS="${N_LDFLAGS}"
}


do_install_prepend () {
	install -d ${D}/${sysconfdir}/init.d ${D}/${sysconfdir}/network
	install -m 755 ${WORKDIR}/neutrino.init ${D}/${sysconfdir}/init.d/neutrino
	install -m 755 ${WORKDIR}/custom-poweroff.init ${D}/${sysconfdir}/init.d/custom-poweroff
	install -m 755 ${WORKDIR}/pre-wlan0.sh ${D}${sysconfdir}/network/
	install -m 755 ${WORKDIR}/post-wlan0.sh ${D}${sysconfdir}/network/
	install -m 644 ${WORKDIR}/timezone.xml ${D}/${sysconfdir}/timezone.xml
	install -d ${D}/var/cache
	install -d ${D}/var/tuxbox/config/
	install -d ${D}/var/tuxbox/plugins/
	echo "version=1200`date +%Y%m%d%H%M`"    > ${D}/.version 
	echo "creator=${CREATOR}"             >> ${D}/.version 
	echo "imagename=Neutrino-HD"             >> ${D}/.version 
	echo "homepage=${HOMEPAGE}"              >> ${D}/.version 
	update-rc.d -r ${D} custom-poweroff start 89 0 .
}

# compatibility with binaries hand-built with --prefix=
do_install_append() {
	install -d ${D}/share/ ${D}/etc/rc5.d 
	ln -s ../usr/share/tuxbox ${D}/share/
	ln -s ../usr/share/fonts  ${D}/share/
	ln -s ../init.d/setdns ${D}${sysconfdir}/rc5.d/S10setdns
}

FILES_${PN} += "\
	/.version \
	${sysconfdir} \
	/usr/share \
	/usr/share/tuxbox \
	/usr/share/iso-codes \
	/usr/share/fonts \
	/usr/share/tuxbox/neutrino \
	/usr/share/iso-codes \
	/usr/share/fonts \
	/share/fonts \
	/share/tuxbox \
	/var/cache \
	/var/tuxbox/plugins \
"

pkg_postinst_${PN} () {
	update-alternatives --install /bin/backup.sh backup.sh /usr/bin/backup.sh 100
	update-alternatives --install /bin/install.sh install.sh /usr/bin/install.sh 100
	update-alternatives --install /bin/restore.sh restore.sh /usr/bin/restore.sh 100
	# pic2m2v is only available on platforms that use "real" libstb-hal
	if which pic2m2v >/dev/null 2>&1; then
		# neutrino icon path
		I=/usr/share/tuxbox/neutrino/icons
		pic2m2v $I/mp3.jpg $I/radiomode.jpg $I/scan.jpg $I/shutdown.jpg $I/start.jpg
	fi
}
