#
# Copyright (C) 2010 Intel Corporation
#

SUMMARY = "Standard full-featured Linux system services"
DESCRIPTION = "Package group bringing in recommended packages needed for a more traditional full-featured Linux system"
PR = "r6"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-custom-recommended \
    "


RDEPENDS_packagegroup-custom-recommended ?= "\
    autofs \
    cifs-utils \
    dosfstools \
    etckeeper \
    exfat-utils \
    htop \
    inotify-tools \
    libevent \
    logrotate \
    mc \
    mc-fish \
    mc-helpers \
    mc-helpers-perl \
    mc-helpers-python \
    minidlna \
    nano \
    neutrino-plugin-logo \
    nfs-utils \
    nfs-utils-client \
    ntfs-3g \
    ntfsprogs \
    ni-logos \
    openssh \
    openssl \
    parted \
    rpcbind \
    samba \
    sysklogd \
    tmux \
    wpa-supplicant \
"
    

