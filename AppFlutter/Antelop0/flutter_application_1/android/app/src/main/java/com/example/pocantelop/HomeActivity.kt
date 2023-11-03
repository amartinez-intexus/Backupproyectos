package com.example.pocantelop

import android.content.Context
import android.content.Intent
import android.nfc.NfcManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pocantelop.databinding.ActivityHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.antelop.sdk.AntelopCallback
import fr.antelop.sdk.AntelopError
import fr.antelop.sdk.WalletLockReason
import fr.antelop.sdk.WalletNotificationServiceCallback
import fr.antelop.sdk.authentication.CustomerAuthenticatedProcess
import fr.antelop.sdk.authentication.CustomerAuthenticationMethodType
import fr.antelop.sdk.authentication.DefaultCustomerAuthenticatedProcessCallback
import fr.antelop.sdk.authentication.prompt.CustomerAuthenticationPrompt
import fr.antelop.sdk.authentication.prompt.CustomerAuthenticationPromptBuilder
import fr.antelop.sdk.card.CardStatus
import fr.antelop.sdk.card.EmvApplicationActivationMethod
import fr.antelop.sdk.digitalcard.DigitalCard
import org.koin.android.ext.android.inject
import java.util.*


class HomeActivity : AppCompatActivity(), ActionMode.Callback {

    private lateinit var binding: ActivityHomeBinding
    private val walletViewModel: WalletViewModel by inject()
    private lateinit var actionMode: ActionMode
    private val selectedCards = arrayListOf<DigitalCard>()
    private var multiSelect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        walletViewModel.getCardsToGenerateFromIHI()
        walletViewModel.tarjetasAGenerar.observe(this) {
            it.forEach { card ->
                card.walletId = walletViewModel.wallet!!.walletId
                walletViewModel.getAntelopCardData(card).observe(this) { base64Data ->
                    if (base64Data.isNotEmpty()) {
                        walletViewModel.wallet?.enrollDigitalCard(baseContext, base64Data, object : AntelopCallback {
                            override fun onSuccess() {
                                walletViewModel.notificarTarjeta(baseContext, card)
                            }

                            override fun onError(p0: AntelopError) {
                                Toast.makeText(applicationContext, applicationContext.getString(R.string.create_card_error), Toast.LENGTH_LONG)
                                println(p0.message)
                            }
                        })
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        loadCards()
                    }, 10000)
                }
            }
        }
        loadCards()

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            (binding.cardsRecycler.adapter as CardsAdapter).setCards(walletViewModel.getCards(this@HomeActivity))
            (binding.cardsRecycler.adapter as CardsAdapter).notifyDataSetChanged()
        }
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu?): Boolean {
        actionMode = mode!!
        mode.menuInflater.inflate(R.menu.card_toolbar_menu, menu)
        return true
    }

    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete) {
            MaterialAlertDialogBuilder(this@HomeActivity)
                .setMessage(R.string.delete_reader)
                .setPositiveButton(R.string.accept) { _, _ ->
                    walletViewModel.deleteCards(selectedCards, this@HomeActivity)
                    mode?.finish()
                    Handler(Looper.getMainLooper()).postDelayed({
                        loadCards()
                    }, 5000)
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        multiSelect = false
        selectedCards.clear()
        (binding.cardsRecycler.adapter as CardsAdapter).selectedCards = selectedCards
        binding.cardsRecycler.adapter?.notifyDataSetChanged()
    }

    private fun loadCards() {
        binding.progressIndicator.show()
        binding.cardsRecycler.layoutManager = GridLayoutManager(applicationContext, 1)
        binding.cardsRecycler.adapter = CardsAdapter(object: CardsAdapter.OnItemClickListener {
            override fun onItemClick(card: DigitalCard, adapterPosition: Int) {
                if (multiSelect) {
                    selectItem(card, adapterPosition)
                }
                else {
                    if (card.issuerNfcWalletService.issuerNfcCard!!.status != CardStatus.Active)
                        Toast.makeText(
                            applicationContext,
                            applicationContext.getText(R.string.provisioning_paycard_error),
                            Toast.LENGTH_LONG
                        ).show()
                    else {
                        val nfcAdapter =
                            (applicationContext.getSystemService(NFC_SERVICE) as NfcManager).defaultAdapter
                        if (nfcAdapter != null && nfcAdapter.isEnabled) {
                            walletViewModel.wallet!!.setNextTransactionCard(card.issuerNfcWalletService.issuerNfcCard!!.id)
                            card.cardDisplayService.secureCardDisplay.launch(
                                applicationContext,
                                object : DefaultCustomerAuthenticatedProcessCallback {
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

                                    override fun onError(
                                        p0: AntelopError,
                                        p1: CustomerAuthenticatedProcess
                                    ) {
                                    }

                                    override fun onAuthenticationDeclined(p0: CustomerAuthenticatedProcess) {
                                    }

                                })
                        } else {
                            val intent = Intent(Settings.ACTION_NFC_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                            startActivity(intent);
                        }
                    }
                }
            }

            override fun onItemLongClick(card: DigitalCard, adapterPosition: Int) {
                if (!multiSelect) {
                    multiSelect = true
                    startActionMode(this@HomeActivity)
                }
                selectItem(card, adapterPosition)
            }
        })
        (binding.cardsRecycler.adapter as CardsAdapter).setCards(walletViewModel.getCards(this@HomeActivity))
    }

    private fun selectItem(card: DigitalCard, position: Int) {
        if (selectedCards.contains(card)) {
            selectedCards.remove(card)
        }
        else {
            selectedCards.add(card)
        }
        if (selectedCards.size == 0) {
            actionMode.finish()
        }
        (binding.cardsRecycler.adapter as CardsAdapter).selectedCards = selectedCards
        binding.cardsRecycler.adapter?.notifyItemChanged(position)
    }

}