# Base this image on core-image-minimal

include neutrino-image-base.inc

IMAGE_INSTALL += " \
	${NEUTRINO_FLAVOUR} \
	neutrino-plugins \
	neutrino-lua-plugins \
	neutrino-plugin-rss \
	neutrino-plugin-localtv \
	neutrino-plugin-myspass \
	neutrino-plugin-mediathek \
	neutrino-plugin-rockpalast \
	neutrino-plugin-startup \
	"
