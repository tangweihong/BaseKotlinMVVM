package com.htt.base_library.network.https

import com.blankj.utilcode.util.LogUtils
import java.io.IOException
import java.io.InputStream
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*
import kotlin.jvm.Throws

/**
 * Created by NullPointerException on 2020/4/30.
 */
object HttpsUtil {

    class SSLParams {
        var sSLSocketFactory: SSLSocketFactory? = null
        var trustManager: X509TrustManager? = null
    }

    fun getSslSocketFactory(
        bksFile: InputStream?,
        password: String?,
        certificates: Array<InputStream?>
    ): SSLParams? {
        val sslParams = SSLParams()
        return try {
            val keyManagers: Array<KeyManager>? = prepareKeyManager(bksFile, password)
            val trustManagers: Array<TrustManager>? = prepareTrustManager(*certificates)
            val sslContext: SSLContext = SSLContext.getInstance("TLS")
            val trustManager: X509TrustManager
            trustManager = if (trustManagers != null) {
                MyTrustManager(chooseTrustManager(trustManagers))
            } else {
                UnSafeTrustManager()
            }
            sslContext.init(keyManagers, arrayOf<TrustManager>(trustManager), null)
            sslParams.sSLSocketFactory = sslContext.getSocketFactory()
            sslParams.trustManager = trustManager
            sslParams
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError(e)
        } catch (e: KeyManagementException) {
            throw AssertionError(e)
        } catch (e: KeyStoreException) {
            throw AssertionError(e)
        }
    }

    private fun prepareTrustManager(vararg certificates: InputStream?): Array<TrustManager>? {
        if (certificates.isEmpty()) return null
        try {
            val certificateFactory: CertificateFactory = CertificateFactory.getInstance("X.509")
            val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)
            for ((index, certificate) in certificates.withIndex()) {
                val certificateAlias = (index).toString()
                keyStore.setCertificateEntry(
                    certificateAlias,
                    certificateFactory.generateCertificate(certificate)
                )
                try {
                    certificate?.close()
                } catch (e: IOException) {
                    LogUtils.e(e)
                }
            }
            val trustManagerFactory: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)
            return trustManagerFactory.trustManagers
        } catch (e: NoSuchAlgorithmException) {
            LogUtils.e(e)
        } catch (e: CertificateException) {
            LogUtils.e(e)
        } catch (e: KeyStoreException) {
            LogUtils.e(e)
        } catch (e: Exception) {
            LogUtils.e(e)
        }
        return null
    }

    private fun prepareKeyManager(
        bksFile: InputStream?,
        password: String?
    ): Array<KeyManager>? {
        try {
            if (bksFile == null || password == null) return null
            val clientKeyStore: KeyStore = KeyStore.getInstance("BKS")
            clientKeyStore.load(bksFile, password.toCharArray())
            val keyManagerFactory: KeyManagerFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            keyManagerFactory.init(clientKeyStore, password.toCharArray())
            return keyManagerFactory.keyManagers
        } catch (e: KeyStoreException) {
            LogUtils.e(e)
        } catch (e: NoSuchAlgorithmException) {
            LogUtils.e(e)
        } catch (e: UnrecoverableKeyException) {
            LogUtils.e(e)
        } catch (e: CertificateException) {
            LogUtils.e(e)
        } catch (e: IOException) {
            LogUtils.e(e)
        } catch (e: Exception) {
            LogUtils.e(e)
        }
        return null
    }

    private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager? {
        for (trustManager in trustManagers) {
            if (trustManager is X509TrustManager) {
                return trustManager as X509TrustManager
            }
        }
        return null
    }

    private class UnSafeTrustManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(
            chain: Array<X509Certificate?>?,
            authType: String?
        ) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(
            chain: Array<X509Certificate?>?,
            authType: String?
        ) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            TODO("Not yet implemented")
            return arrayOf<X509Certificate>()
        }
    }

    private class MyTrustManager(localTrustManager: X509TrustManager?) :
        X509TrustManager {
        private val defaultTrustManager: X509TrustManager?
        private val localTrustManager: X509TrustManager?

        @Throws(CertificateException::class)
        override fun checkClientTrusted(
            chain: Array<X509Certificate?>?,
            authType: String?
        ) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(
            chain: Array<X509Certificate?>?,
            authType: String?
        ) {
            try {
                defaultTrustManager?.checkServerTrusted(chain, authType)
            } catch (ce: CertificateException) {
                localTrustManager?.checkServerTrusted(chain, authType)
            }
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            TODO("Not yet implemented")
            return arrayOf<X509Certificate>()
        }


        init {
            val var4: TrustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            var4.init(null as KeyStore?)
            defaultTrustManager = chooseTrustManager(var4.trustManagers)
            this.localTrustManager = localTrustManager
        }
    }
}