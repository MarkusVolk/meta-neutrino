
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

RDEPENDS_${PN} += "git findutils cronie ${@'perl-module-file-glob' if DISTRO != 'coolstream-hd1_flash' else ''}"

SRC_URI = "git://github.com/joeyh/etckeeper.git;branch=master \
	   file://etckeeper \
	   file://etckeeper.conf \
	   file://create_etc.sh \
	   file://update_etc.sh \
	   file://0001-use-systemwide-gitconfig-to-correct-commiter-name-an.patch \
"

SRC_URI_append_coolstream-hd1 = "${@'' if DISTRO != 'coolstream-hd1_flash' else 'file://0004-Remove-all-remaining-usage-of-Perl.patch'}"

SRC_URI[md5sum] = "439d65fc487910a30b686788b7c6fc99"
SRC_URI[sha256sum] = "76fd0349ff138b98a4dde831a23a13d3fc6608147ef4fef35ce58ebf48f18f23"

SRCREV = "${AUTOREV}"
PV = "${SRCPV}"
PR = "1"

INITSCRIPT_NAME = "update_etc"

S = "${WORKDIR}/git"

inherit autotools-brokensep 

do_configure_prepend () {
	sed -i "s|GIT_USER|${GIT_USER}|" ${WORKDIR}/update_etc.sh
	sed -i "s|MAIL|${MAIL}|" ${WORKDIR}/update_etc.sh
	sed -i "s|GIT_URL|${GIT_URL}|" ${WORKDIR}/update_etc.sh
	sed -i "s|GIT_USER|${GIT_USER}|" ${WORKDIR}/create_etc.sh
	sed -i "s|MAIL|${MAIL}|" ${WORKDIR}/create_etc.sh
	sed -i "s|GIT_URL|${GIT_URL}|" ${WORKDIR}/create_etc.sh
	if [ ${DISTRO} = "coolstream-hd1_flash" ];then
		sed -i "s|nano|vi|" ${WORKDIR}/create_etc.sh
		sed -i "s|nano|vi|" ${WORKDIR}/update_etc.sh
	fi
	if [ ${DISTRO} = "coolstream-hd1" ];then
		sed -i "s|/media/sda1|/media/sdb1|" ${WORKDIR}/create_etc.sh
		sed -i "s|/media/sda1|/media/sdb1|" ${WORKDIR}/update_etc.sh
	fi
}
	
do_install_append () {
	install -d ${D}${sysconfdir}/cron.daily/ ${D}${sysconfdir}/init.d
	install -m755 ${WORKDIR}/etckeeper ${D}/etc/cron.daily/etckeeper
	install -m644 ${WORKDIR}/etckeeper.conf ${D}/etc/etckeeper
	install -m 755 ${WORKDIR}/update_etc.sh ${D}${sysconfdir}/init.d/update_etc.sh
	install -m 755 ${WORKDIR}/create_etc.sh ${D}${sysconfdir}/init.d/create_etc.sh
	update-rc.d -r ${D} update_etc.sh start 08 5 .
}

