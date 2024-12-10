package br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.mizaeldouglas.rideway_app_teste_emprego_shopper.R
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Para suporte a bordas estendidas, se necessário
        setContentView(R.layout.activity_main)

        // Ajuste da barra de sistema para trabalhar com bordas estendidas (opcional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuração do botão para começar
        val btnStart = findViewById<MaterialButton>(R.id.btn_start)
        btnStart.setOnClickListener {
            // Navegar para a próxima atividade (pode ser a atividade de histórico de viagens, por exemplo)
            val intent = Intent(this, RequestRideActivity::class.java)
            startActivity(intent)
        }
    }
}
