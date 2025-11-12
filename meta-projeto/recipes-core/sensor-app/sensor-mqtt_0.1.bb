SUMMARY = "Script Python para sensor ultrassonico e MQTT"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Nosso script está no subdiretório 'files'
SRC_URI = "file://sensor.py"

# Dependências de runtime (o que precisa estar na imagem final)
RDEPENDS:${PN} = " python3-paho-mqtt python3-rpi-gpio"

# O que fazer durante a instalação
do_install() {
    # Criar o diretório no sistema de arquivos da imagem
    install -d ${D}${bindir}
    
    # Instalar nosso script e torná-lo executável
    install -m 0755 ${WORKDIR}/sensor.py ${D}${bindir}/
}
