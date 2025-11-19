SUMMARY = "Script Python para sensor ultrassonico e MQTT"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Nosso script está no subdiretório 'files'
SRC_URI = " \
    file://sensor.py \
    file://sensor.service \
"

# Dependências de runtime 
RDEPENDS:${PN} = "\
    python3-core \
    python3-paho-mqtt \
    python3-rpi-gpio \
"

# O que fazer durante a instalação
do_install() {

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/sensor.py ${D}${bindir}/sensor.py

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/sensor.service ${D}${systemd_system_unitdir}/sensor.service

}

SYSTEMD_SERVICE:${PN} = "sensor.service"
inherit systemd
SYSTEMD_AUTO_ENABLE= "enable"
FILES:${PN} += "${bindir} ${systemd_system_unitdir}"
