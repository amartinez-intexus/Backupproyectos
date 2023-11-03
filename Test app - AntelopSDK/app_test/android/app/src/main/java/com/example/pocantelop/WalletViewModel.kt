package com.example.pocantelop

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.pocantelop.core.entities.Result
import com.example.pocantelop.core.entities.Tarjeta
import com.example.pocantelop.core.entities.io.GetAntelopDataOut
import com.example.pocantelop.core.entities.io.NotificarTarjetaIn
import com.example.pocantelop.core.entities.io.ObtenerTarjetaAGenerarIn
import com.example.pocantelop.core.entities.io.RegistrarWalletIn
import com.example.pocantelop.core.repositories.AntelopRepository
import com.example.pocantelop.core.repositories.TarjetaDigitalRepository
import fr.antelop.sdk.AntelopCallback
import fr.antelop.sdk.AntelopError
import fr.antelop.sdk.Wallet
import fr.antelop.sdk.authentication.CustomerAuthenticatedProcess
import fr.antelop.sdk.authentication.CustomerAuthenticationMethodType
import fr.antelop.sdk.authentication.DefaultCustomerAuthenticatedProcessCallback
import fr.antelop.sdk.authentication.prompt.CustomerAuthenticationPrompt
import fr.antelop.sdk.authentication.prompt.CustomerAuthenticationPromptBuilder
import fr.antelop.sdk.card.Card
import fr.antelop.sdk.card.EmvApplicationActivationMethod
import fr.antelop.sdk.digitalcard.DigitalCard
import fr.antelop.sdk.util.OperationCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class WalletViewModel(private val tarjetaDigitalRepository: TarjetaDigitalRepository, private val antelopRepository: AntelopRepository): ViewModel() {

    var passcode: ByteArray? = null
    var wallet: Wallet? = null
    var registrarWalletMessage = MutableLiveData<String>()
    var tarjetasAGenerar = MutableLiveData<List<Tarjeta>>()

    fun getCards(context: Activity): ArrayList<DigitalCard> {
        val cards = wallet?.digitalCards(false)?.values!!
        cards.filter{ card -> !card.issuerNfcWalletService.isCardInIssuerNfcWallet }.forEach {
            try {
                it.issuerNfcWalletService.secureCardPush.launch(context, object : DefaultCustomerAuthenticatedProcessCallback {
                    override fun buildCustomerAuthenticationPrompt(
                        p0: CustomerAuthenticationMethodType,
                        p1: CustomerAuthenticationPromptBuilder
                    ): CustomerAuthenticationPrompt {
                        return p1.build()
                    }

                    override fun onProcessStart(p0: CustomerAuthenticatedProcess) {
                    }

                    override fun onProcessSuccess(p0: CustomerAuthenticatedProcess) {
                    }

                    override fun onError(p0: AntelopError, p1: CustomerAuthenticatedProcess) {
                    }

                    override fun onAuthenticationDeclined(p0: CustomerAuthenticatedProcess) {
                    }

                })
            }
            catch (ex: Exception){
                println(ex)
            }
        }
        return ArrayList(cards)
    }

    fun getAntelopCardData(tarjeta: Tarjeta) = liveData {
        withContext(Dispatchers.Main) {
            when (val result = antelopRepository.getAntelopData(tarjeta)) {
                is Result.Success ->
                    emit(result.data.enrollmentData)
                is Result.Error ->
                    emit("")
            }
        }
    }

    fun registrarWallet(ctx: Context, documento: String) {
        viewModelScope.launch {
            when (val result = tarjetaDigitalRepository.registrarWallet(RegistrarWalletIn(com.example.pocantelop.core.entities.Wallet(wallet!!.walletId, documento)))) {
                is Result.Success ->
                    registrarWalletMessage.postValue(ctx.getString(R.string.wallet_created_success))
                is Result.Error ->
                    registrarWalletMessage.postValue("${ctx.getString(R.string.wallet_created_error)}. ${result.error}")
            }
        }
    }

    fun notificarTarjeta(ctx: Context, tarjeta: Tarjeta) {
        viewModelScope.launch {
            when (val result = tarjetaDigitalRepository.notificarTarjeta(NotificarTarjetaIn(tarjeta))) {
                is Result.Success ->
                    Log.i("NotificarTarjeta", "Tarjeta notificada correctamente")
                is Result.Error ->
                    Log.i("NotificarTarjeta", "Error notificando la tarjeta, ${result.error}")
            }
        }
    }

    fun getCardsToGenerateFromIHI() {
        viewModelScope.launch {
            when (val result = tarjetaDigitalRepository.obtenerTarjetasAGenerar(ObtenerTarjetaAGenerarIn(com.example.pocantelop.core.entities.Wallet(wallet!!.walletId)))) {
                is Result.Success -> {
                    tarjetasAGenerar.postValue(result.data.result.tarjetaDigital)
                }
                is Result.Error ->
                    tarjetasAGenerar.postValue(listOf())
            }
        }
    }

    fun deleteCards(cards: ArrayList<DigitalCard>, ctx: Context) {
        cards.forEach {
            it.delete(ctx, object : AntelopCallback {
                override fun onSuccess() {
                }

                override fun onError(p0: AntelopError) {
                }

            })
        }
    }

}