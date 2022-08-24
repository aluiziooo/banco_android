package br.ufpe.cin.residencia.banco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//Ver anotações TODO no código
public class TransferirActivity extends AppCompatActivity {

    BancoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operacoes);
        viewModel = new ViewModelProvider(this).get(BancoViewModel.class);

        TextView tipoOperacao = findViewById(R.id.tipoOperacao);
        EditText numeroContaOrigem = findViewById(R.id.numeroContaOrigem);
        TextView labelContaDestino = findViewById(R.id.labelContaDestino);
        EditText numeroContaDestino = findViewById(R.id.numeroContaDestino);
        EditText valorOperacao = findViewById(R.id.valor);
        Button btnOperacao = findViewById(R.id.btnOperacao);

        valorOperacao.setHint(valorOperacao.getHint() + " transferido");
        tipoOperacao.setText("TRANSFERIR");
        btnOperacao.setText("Transferir");

        btnOperacao.setOnClickListener(
                v -> {
                    String numOrigem = numeroContaOrigem.getText().toString();
                    String numDestino = numeroContaDestino.getText().toString();

                    double valor;
                    if (valorOperacao.getText().toString().equals("")){
                        valor = 0;
                        Toast.makeText(this, "Digite dados válidos", Toast.LENGTH_SHORT).show();
                    } else {
                        valor = Double.valueOf(valorOperacao.getText().toString());
                    }

                    if (numOrigem.equals("") || valor == 0 || numDestino.equals("")) {
                        Toast.makeText(this, "Digite dados válidos", Toast.LENGTH_SHORT).show();
                    } else if(valor > 0){
                        viewModel.transferir(numOrigem, numDestino, valor);
                        Toast.makeText(this, "R$" + valor + " transferido", Toast.LENGTH_SHORT).show();   // Se a a variável valor for maior que zero,
                        finish();                                                                                   //irá chamar o método transferr
                    }

                }
        );

    }
}