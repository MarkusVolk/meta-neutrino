SUMMARY = "Standard full-featured Linux system utils"
DESCRIPTION = "Package group bringing in utility packages needed for a more traditional full-featured Linux system"
PR = "r6"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-custom-utils \
    "

RDEPENDS_packagegroup-custom-utils = "\
    bash \
    acl \
    attr \
    bc \
    coreutils \
    cpio \
    e2fsprogs \
    ed \
    ethtool \
    file \
    findutils \
    gawk \
    gmp \
    grep \
    hdparm \
    makedevs \
    man \
    man-pages \
    mc \
    mc-fish \
    mc-helpers \
    mc-helpers-perl \
    mc-helpers-python \
    mktemp \
    nano \
    ncurses \
    net-tools \
    pax \
    popt \
    psmisc \
    sed \
    tar \
    time \
    tmux \
    util-linux \
    vim \
    zlib \
    "
