package org.web3j.protocol.aion

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService

@Testcontainers
class AionIT {

    @Test
    fun testGetUncleCountByBlockHash() {
        val hash = "0x34e9d4af6d2bace6a54b6039843e55bdc1d4ff425cd8e8c8f3fa96ca89f58a53"
        val countByBlockHash = web3j.ethGetUncleCountByBlockHash(hash).send()

        assertThat(countByBlockHash.result).isNull()
        assertThat(countByBlockHash.error.code).isEqualTo(-32601)
        assertThat(countByBlockHash.error.message).isEqualTo("Method not found")
    }

    @Test
    fun testGetBalance() {
        val address = "0xa0b2e70f995df21e7988d645f1fdbaa94324f82b1d162b9b45cdbef7b5b51bd2"
        val balance = web3j.ethGetBalance(address, DefaultBlockParameterName.PENDING).send()

        assertThat(balance.error).isNull()
        assertThat(balance.balance).isZero()
    }

    companion object {
        private lateinit var web3j: Aion

        @BeforeAll
        @JvmStatic
        fun initClient() {
            val url = "http://${AION.containerIpAddress}:${AION.getMappedPort(8545)}/"
            web3j = Aion.build(HttpService(url))
        }

        @Container
        @JvmStatic
        private val AION = KGenericContainer("aionnetwork/aion:0.3.3")
                .withExposedPorts(8545)
    }

    class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)
}