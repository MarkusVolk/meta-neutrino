require ffmpeg.inc

#SRCREV = "${AUTOREV}"
SRCREV = "d91718107c33960ad295950d7419e6dba292d723"
PV = "${SRCPV}"
PR = "1"

SRC_URI = "git://github.com/FFmpeg/FFmpeg.git;branch=master \
	   file://0001-add-HDS-ro.patch \
	   file://0004-add-ASF-VC1-Annex-G-and-RCV-bitstream-filters.patch \
	   file://0001-Revert-lavc-Switch-bitrate-to-64bit-unless-compatibi.patch \
"

SRC_URI_append_coolstream-hd1 = "file://0005-Revert-arm-add-a-cpu-flag-for-the-VFPv2-vector-mode.patch \
"

LIC_FILES_CHKSUM = " \
	file://COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
	file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
	file://COPYING.LGPLv2.1;md5=bd7a443320af8c812e4c18d1b79df004 \
	file://COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02 \
"

S = "${WORKDIR}/git"

EXTRA_OECONF += " \
	--enable-postproc \
"

PROVIDES = "ffmpeg_${PV}"
