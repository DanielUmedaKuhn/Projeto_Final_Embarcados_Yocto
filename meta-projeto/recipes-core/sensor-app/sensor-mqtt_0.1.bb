SUMMARY = "Script Python para sensor ultrassonico e MQTT"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Nosso script está no subdiretório 'files'
SRC_URI = " \
    file://sensor.py \
    file://sensor.service \
"

# Dependências de runtime (o que precisa estar na imagem final)
RDEPENDS:${PN} = "\
    python3-core \
    python3-paho-mqtt \
    python3-rpi-gpio \
"

# O que fazer durante a instalação
do_install() {

    # Criar o diretório no sistema de arquivos da imagem
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/hcsr04.py ${D}${bindir}/sensor.py

    # Instalar Systemd unity
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/sensor.service ${D}${systemd_system_unitdir}/sensor.service

    # Instalar nosso script e torná-lo executável
   # install -m 0755 ${WORKDIR}/sensor.py ${D}${bindir}/
}

SYSTEMD_SERVICE:${PN} = "sensor.service"
inherit systemd

FILES:${PN} += "${bindir} ${systemd_system_unitdir}"
