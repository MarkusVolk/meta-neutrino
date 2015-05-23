SUMMARY = "lightweight DLNA/UPnP-AV server targeted at embedded systems"
HOMEPAGE = "http://sourceforge.net/projects/minidlna/"
SECTION = "network"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b1a795ac1a06805cf8fd74920bc46b5c"
DEPENDS = "libexif libjpeg-turbo libid3tag flac libvorbis sqlite3 ffmpeg util-linux virtual/libiconv"

PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/minidlna/minidlna/${PV}/minidlna-${PV}.tar.gz \
		file://minidlna*.conf \
		file://init \
"

SRC_URI[md5sum] = "67c9e91285bc3801fd91a5d26ea775d7"
SRC_URI[sha256sum] = "9814c04a2c506a0dd942c4218d30c07dedf90dabffbdef2d308a3f9f23545314"

S = "${WORKDIR}/${PN}-${PV}"

inherit autotools-brokensep gettext update-rc.d

PACKAGES =+ "${PN}-utils"

FILES_${PN}-utils = "${bindir}/test*"

CONFFILES_${PN} = "${sysconfdir}/minidlna.conf"

INITSCRIPT_NAME = "minidlna"

do_configure_prepend() {
	sed -i "s|Coolstream|${MACHINE}|" ${WORKDIR}/minidlna*.conf
}

do_install_append() {
	install -d ${D}${sysconfdir} ${D}${sysconfdir}/init.d/
	install -m 644 ${WORKDIR}/minidlna.conf ${D}${sysconfdir}
	install -m 755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/${PN}
}

do_install_append_coolstream-hd1 () {
	install -m 644 ${WORKDIR}/minidlna-${DISTRO}.conf ${D}${sysconfdir}/minidlna.conf
}

do_install_append_coolstream-hd2 () {
	install -m 644 ${WORKDIR}/minidlna-${DISTRO}.conf ${D}${sysconfdir}/minidlna.conf
}
