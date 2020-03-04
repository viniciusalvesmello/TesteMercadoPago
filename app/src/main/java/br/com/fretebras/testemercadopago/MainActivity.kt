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

        btStartMercadoPagoCheckout.setOnClickListener {
            startMercadoPagoCheckout()
        }
    }

    fun startMercadoPagoCheckout() {
        MercadoPagoCheckout.Builder(
            "TEST-b8777443-0aca-47bd-8d7d-57843dd536ce",
            "532361219-af878194-acb7-4205-98a6-8855abc0cb1a"
        ).build().startPayment(this,
            REQUEST_CODE
        )
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
                    "Resultado: ${payment?.paymentStatus}"
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
