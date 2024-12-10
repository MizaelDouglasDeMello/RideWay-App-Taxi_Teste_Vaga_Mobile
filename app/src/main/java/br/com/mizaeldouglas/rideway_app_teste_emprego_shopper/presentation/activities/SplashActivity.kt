package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Temporizador para exibir a Splash por 2 segundos
        Handler().postDelayed({
            // Após o tempo, iniciar a MainActivity (Home)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Finaliza a SplashActivity para não permitir voltar para ela
        }, 2000)  // 2000 milissegundos = 2 segundos
    }
}