SUMMARY = "A complete, cross-platform solution to record, convert and stream audio and video."
DESCRIPTION = "FFmpeg is the leading multimedia framework, able to decode, encode, transcode, \
               mux, demux, stream, filter and play pretty much anything that humans and machines \
               have created. It supports the most obscure ancient formats up to the cutting edge."
HOMEPAGE = "https://www.ffmpeg.org/"
SECTION = "libs"

LICENSE = "BSD & GPLv2+ & LGPLv2.1+ & MIT"
LICENSE_${PN} = "GPLv2+"
LICENSE_libavcodec = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPLv2+', 'LGPLv2.1+', d)}"
LICENSE_libavdevice = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPLv2+', 'LGPLv2.1+', d)}"
LICENSE_libavfilter = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPLv2+', 'LGPLv2.1+', d)}"
LICENSE_libavformat = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPLv2+', 'LGPLv2.1+', d)}"
LICENSE_libavresample = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPLv2+', 'LGPLv2.1+', d)}"
LICENSE_libavutil = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPLv2+', 'LGPLv2.1+', d)}"
LICENSE_libpostproc = "GPLv2+"
LICENSE_libswresample = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPLv2+', 'LGPLv2.1+', d)}"
LICENSE_libswscale = "${@bb.utils.contains('PACKAGECONFIG', 'gpl', 'GPLv2+', 'LGPLv2.1+', d)}"
LICENSE_FLAGS = "commercial"

LIC_FILES_CHKSUM = "file://COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://COPYING.LGPLv2.1;md5=bd7a443320af8c812e4c18d1b79df004 \
                    file://COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"

SRC_URI = "https://www.ffmpeg.org/releases/${BP}.tar.xz \
	   file://0001-Revert-lavc-Switch-bitrate-to-64bit-unless-compatibi.patch \
	   file://0002-add-HDS-ro_new.patch \
          "
SRC_URI[md5sum] = "39fd71024ac76ba35f04397021af5606"
SRC_URI[sha256sum] = "6e38ff14f080c98b58cf5967573501b8cb586e3a173b591f3807d8f0660daf7a"

# Build fails when thumb is enabled: https://bugzilla.yoctoproject.org/show_bug.cgi?id=7717
ARM_INSTRUCTION_SET = "arm"

# Should be API compatible with libav (which was a fork of ffmpeg)
# libpostproc was previously packaged from a separate recipe
PROVIDES = "libav libpostproc"

DEPENDS = "alsa-lib zlib libogg yasm-native libroxml rtmpdump openssl virtual/libiconv"

inherit autotools pkgconfig

B = "${S}/build.${HOST_SYS}.${TARGET_SYS}"

FULL_OPTIMIZATION_armv7a = "-fexpensive-optimizations -fomit-frame-pointer -O4 -ffast-math"
BUILD_OPTIMIZATION = "${FULL_OPTIMIZATION}"

EXTRA_FFCONF_armv7a = "--cpu=cortex-a9"
EXTRA_FFCONF ?= ""

#PACKAGECONFIG ??= "bzip2 x264 ${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}"
PACKAGECONFIG ??= "bzip2 rtmpdump libroxml"
PACKAGECONFIG[jack] = "--enable-indev=jack,--disable-indev=jack,jack"
PACKAGECONFIG[bzip2] = "--enable-bzlib,--disable-bzlib,bzip2"
PACKAGECONFIG[schroedinger] = "--enable-libschroedinger,--disable-libschroedinger,schroedinger"
PACKAGECONFIG[gsm] = "--enable-libgsm,--disable-libgsm,libgsm"
PACKAGECONFIG[x264] = "--enable-libx264,--disable-libx264,x264"
PACKAGECONFIG[vpx] = "--enable-libvpx,--disable-libvpx,libvpx"
PACKAGECONFIG[mp3lame] = "--enable-libmp3lame,--disable-libmp3lame,lame"
PACKAGECONFIG[x11] = "--enable-x11grab,--disable-x11grab,virtual/libx11 libxfixes libxext xproto"
PACKAGECONFIG[rtmpdump] = "--enable-librtmp,--disable-librtmp,rtmpdump"
PACKAGECONFIG[libroxml] = "--enable-libroxml,--disable-libroxml,libroxml"

EXTRA_OECONF = " \
	--disable-stripping \
	--enable-pic \
	--enable-shared \
	--enable-pthreads \
	--enable-gpl \
	--enable-avfilter \
	--cross-prefix=${TARGET_PREFIX} \
	--prefix=${prefix} \
	--enable-ffserver \
	--enable-ffplay \
	--enable-libtheora  \
	--enable-libvorbis \
	--arch=${TARGET_ARCH} \
	--target-os="linux" \
	--enable-cross-compile \
	--extra-cflags="${TARGET_CFLAGS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}" \
	--extra-ldflags="${TARGET_LDFLAGS}" \
	--sysroot="${STAGING_DIR_TARGET}" \
	--enable-hardcoded-tables \
	--disable-runtime-cpudetect \
	--arch=arm \
	--disable-neon \
	--disable-libtheora \
	--disable-libvorbis \
	--disable-decoders \
	--enable-decoder=dca \
	--enable-decoder=dvdsub \
	--enable-decoder=dvbsub \
	--enable-decoder=text \
	--enable-decoder=srt \
	--enable-decoder=subrip \
	--enable-decoder=subviewer \
	--enable-decoder=subviewer1 \
	--enable-decoder=xsub \
	--enable-decoder=pgssub \
	--enable-decoder=movtext \
	--enable-decoder=mp3 \
	--enable-decoder=flac \
	--enable-decoder=vorbis \
	--enable-decoder=aac \
	--enable-decoder=mjpeg \
	--enable-decoder=pcm_s16le \
	--enable-decoder=pcm_s16le_planar \
	--disable-parsers \
	--enable-parser=aac \
	--enable-parser=aac_latm \
	--enable-parser=ac3 \
	--enable-parser=dca \
	--enable-parser=mjpeg \
	--enable-parser=mpeg4video \
	--enable-parser=mpegvideo \
	--enable-parser=mpegaudio \
	--enable-parser=h264 \
	--enable-parser=vc1 \
	--enable-parser=dvdsub \
	--enable-parser=dvbsub \
	--enable-parser=flac \
	--enable-parser=vorbis \
	--disable-demuxers \
	--enable-demuxer=aac \
	--enable-demuxer=ac3 \
	--enable-demuxer=avi \
	--enable-demuxer=mov \
	--enable-demuxer=vc1 \
	--enable-demuxer=mjpeg \
	--enable-demuxer=mpegts \
	--enable-demuxer=mpegtsraw \
	--enable-demuxer=mpegps \
	--enable-demuxer=mpegvideo \
	--enable-demuxer=wav \
	--enable-demuxer=pcm_s16be \
	--enable-demuxer=mp3 \
	--enable-demuxer=pcm_s16le \
	--enable-demuxer=matroska \
	--enable-demuxer=flv \
	--enable-demuxer=rm \
	--enable-demuxer=rtsp \
	--enable-demuxer=hls \
	--enable-demuxer=dts \
	--enable-demuxer=wav \
	--enable-demuxer=ogg \
	--enable-demuxer=flac \
	--enable-demuxer=srt \
	--enable-demuxer=hds \
	--disable-encoders \
	--disable-muxers \
	--enable-muxer=mpegts \
	--disable-filters \
	--disable-protocol=data \
	--disable-protocol=cache \
	--disable-protocol=concat \
	--disable-protocol=crypto \
	--disable-protocol=ftp \
	--disable-protocol=gopher \
	--disable-protocol=httpproxy \
	--disable-protocol=pipe \
	--disable-protocol=sctp \
	--disable-protocol=srtp \
	--disable-protocol=subfile \
	--disable-protocol=unix \
	--disable-protocol=md5 \
	--disable-protocol=hls \
	--enable-openssl \
	--enable-protocol=file \
	--enable-protocol=http \
	--enable-protocol=https \
	--enable-protocol=mmsh \
	--enable-protocol=mmst \
	--enable-protocol=rtp \
	--enable-protocol=tcp \
	--enable-protocol=udp \
	--enable-bsfs \
	--disable-devices \
	--enable-swresample \
	--disable-postproc \
	--disable-swscale \
	--enable-nonfree \
    	${EXTRA_FFCONF} \
"

EXTRA_OECONF_append_coolstream-hd1 = " \
	--cpu=armv6 \
	--disable-vfp \
"

EXTRA_OECONF_append_coolstream-hd2 = " \
	--cpu=cortex-a9 \
	--enable-vfp \
"

do_configure() {
    # We don't have TARGET_PREFIX-pkgconfig
    sed -i '/pkg_config_default="${cross_prefix}${pkg_config_default}"/d' ${S}/configure
    mkdir -p ${B}
    cd ${B}
    ${S}/configure ${EXTRA_OECONF}
    sed -i -e s:Os:O4:g ${B}/config.h
}

do_install_append() {
    install -m 0644 ${S}/libavfilter/*.h ${D}${includedir}/libavfilter/
}

FFMPEG_LIBS = "libavcodec libavdevice libavformat \
               libavutil libpostproc libswscale libavfilter"

PACKAGES += "${PN}-vhook-dbg ${PN}-vhook ffmpeg-x264-presets"

RSUGGESTS_${PN} = "mplayer"
FILES_${PN} = "${bindir} ${datadir}/ffprobe.xsd"
FILES_${PN}-dev = "${includedir}/${PN}"

FILES_${PN}-vhook = "${libdir}/vhook"
FILES_${PN}-vhook-dbg += "${libdir}/vhook/.debug"

FILES_ffmpeg-x264-presets = "${datadir}/*.ffpreset"

LEAD_SONAME = "libavcodec.so"

FILES_${PN}-dev = "${includedir}"
FILES_${PN}-dev += "${datadir}/examples"

python populate_packages_prepend() {
    av_libdir = d.expand('${libdir}')
    av_pkgconfig = d.expand('${libdir}/pkgconfig')

    # Runtime package
    do_split_packages(d, av_libdir, '^lib(.*)\.so\..*',
                      output_pattern='lib%s',
                      description='libav %s library',
                      extra_depends='',
                      prepend=True,
                      allow_links=True)

    # Development packages (-dev, -staticdev)
    do_split_packages(d, av_libdir, '^lib(.*)\.so$',
                      output_pattern='lib%s-dev',
                      description='libav %s development package',
                      extra_depends='${PN}-dev',
                      prepend=True,
                      allow_links=True)
    do_split_packages(d, av_pkgconfig, '^lib(.*)\.pc$',
                      output_pattern='lib%s-dev',
                      description='libav %s development package',
                      extra_depends='${PN}-dev',
                      prepend=True)
    do_split_packages(d, av_libdir, '^lib(.*)\.a$',
                      output_pattern='lib%s-staticdev',
                      description='libav %s development package - static library',
                      extra_depends='${PN}-dev',
                      prepend=True,
                      allow_links=True)

    if d.getVar('TARGET_ARCH', True) == 'i586':
        # libav can't be build with -fPIC for 32-bit x86
        pkgs = d.getVar('PACKAGES', True).split()
        for pkg in pkgs:
            d.appendVar('INSANE_SKIP_%s' % pkg, ' textrel')
}

PACKAGES_DYNAMIC += "^lib(av(codec|device|filter|format|util)|postproc).*"
