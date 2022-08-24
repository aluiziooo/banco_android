package br.ufpe.cin.residencia.banco;

import static br.ufpe.cin.residencia.banco.MainActivity.TOTAL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//Ver anotações TODO no código
public class CreditarActivity extends AppCompatActivity {
    BancoViewModel viewModel;
    private int totalDeDinheiro = 0;

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
        labelContaDestino.setVisibility(View.GONE);
        numeroContaDestino.setVisibility(View.GONE);
        valorOperacao.setHint(valorOperacao.getHint() + " creditado");
        tipoOperacao.setText("CREDITAR");
        btnOperacao.setText("Creditar");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        totalDeDinheiro = preferences.getInt(TOTAL, 0);

        btnOperacao.setOnClickListener(
                v -> {
                    String numOrigem = numeroContaOrigem.getText().toString();
                    double valor;

                    if (valorOperacao.getText().toString().equals("")){ //Se o campo valor da operação for vazio, vai fazer a variável valor igual a zero.
                        valor = 0;
                        Toast.makeText(this, "Digite dados válidos", Toast.LENGTH_SHORT).show(); // exiba um toaster com dados invalidos
                    } else {
                        valor = Double.valueOf(valorOperacao.getText().toString());
                    }

                    if (numOrigem.equals("") || valor == 0) { // se o campo de numero da conta for vazio
                        Toast.makeText(this, "Digite dados válidos", Toast.LENGTH_SHORT).show(); // mostra o toaster de dados
                    } else if (valor > 0){ //se estiver tudo correto credite!!
                        viewModel.creditar(numOrigem, valor);
                        Toast.makeText(this, "R$" + valor + " reais creditado", Toast.LENGTH_SHORT).show();
                        totalDeDinheiro += valor;
                       /*Abaixo adicionei o preferences para atualizar o valortotal da main activity*/
                        preferences
                                .edit()
                                .putInt(TOTAL, totalDeDinheiro)
                                .apply();
                        finish();
                    }

                }
        );
    }
}