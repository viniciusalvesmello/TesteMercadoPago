package br.com.fretebras.testemercadopago

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bt.com.fretebras.testemercadopago.R
import com.mercadopago.android.px.core.MercadoPagoCheckout
import com.mercadopago.android.px.model.Payment
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        publicKey.setText("TEST-a4c8441d-d804-4b8f-bd88-540b917a39e1")
        preferenceId.setText("532361219-42881c36-35ca-4481-9a39-8016f84caac5")

        btStartMercadoPagoCheckout.setOnClickListener {
            startMercadoPagoCheckout(
                publicKey.text.toString(),
                preferenceId.text.toString()
            )
        }
    }

    fun startMercadoPagoCheckout(publicKey: String, preferenceId: String) {
        MercadoPagoCheckout.Builder(publicKey, preferenceId).build()
            .startPayment(this, REQUEST_CODE)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            val message: String = when {
                resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE -> {
                    val payment =
                        data?.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT) as? Payment
                    "Resultado: ${payment?.paymentStatus} - $payment"
                }
                resultCode == Activity.RESULT_CANCELED && (data?.extras?.containsKey(
                    MercadoPagoCheckout.EXTRA_ERROR
                ) ?: false) -> {
                    val mercadoPagoError =
                        data?.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR) as? MercadoPagoError
                    "Error: ${mercadoPagoError?.message}"
                }
                else -> {
                    "Pagamento Cancelado"
                }
            }
            mp_results.text = message
        }
    }

    companion object {
        const val REQUEST_CODE = 1
    }
}
