SUMMARY = "Machine specific partition configurations"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DEPENDS += "qcom-gen-partitions-tool-native"

inherit python3native

PROVIDES += "virtual/partconf"

FILESEXTRAPATHS:prepend := "${THISDIR}:"

SRC_URI = " \
    file://qcm6490-partitions.conf \
    file://qcs9100-partitions.conf \
"

S = "${WORKDIR}"

do_configure[noexec] = "1"

PARTCONF ?= ""
PARTCONF:qcm6490 = "qcm6490-partitions.conf"
PARTCONF:qcs9100 = "qcs9100-partitions.conf"

do_compile() {
    # Generate partition.xml using gen_partition utility
    ${PYTHON} ${STAGING_BINDIR_NATIVE}/gen_partition.py \
        -i ${WORKDIR}/${PARTCONF} \
        -o ${B}/${MACHINE}-partition.xml
}

do_install() {
    install -D -m0644 ${B}/${MACHINE}-partition.xml ${D}${sysconfdir}/${MACHINE}-partition.xml
}

inherit deploy
do_deploy() {
    install -m 0644 ${D}${sysconfdir}/${MACHINE}-partition.xml -D ${DEPLOYDIR}/partition.xml
}
addtask deploy before do_build after do_install

PACKAGE_ARCH = "${SOC_ARCH}"
