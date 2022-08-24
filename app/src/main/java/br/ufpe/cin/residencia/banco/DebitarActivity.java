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
public class DebitarActivity extends AppCompatActivity {
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

        valorOperacao.setHint(valorOperacao.getHint() + " debitado");
        tipoOperacao.setText("DEBITAR");
        btnOperacao.setText("Debitar");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        totalDeDinheiro = preferences.getInt(TOTAL, 0);

        btnOperacao.setOnClickListener(
                v -> {
                    String numOrigem = numeroContaOrigem.getText().toString();
                    double valor;

                    if (valorOperacao.getText().toString().equals("")){        // Verificando de o valor passado é nulo
                        valor = 0;
                        Toast.makeText(this, "Digite dados válidos", Toast.LENGTH_SHORT).show();// se for joga um toaster na tela
                    } else {
                        valor = Double.valueOf(valorOperacao.getText().toString()); // se não converte a string para double
                    }

                    if (numOrigem.equals("") || valor == 0) { //Verifica se o campo de conta está vazio e o valo é igual a zero
                        Toast.makeText(this, "Digite dados válidos", Toast.LENGTH_SHORT).show();// joga um toaster pedindo para que os dados passados sejam validos
                    } else if(valor > 0) {    //se estiver tudo correto debite!!
                        viewModel.debitar(numOrigem, valor);
                        Toast.makeText(this, "R$" + valor + " reais debitado", Toast.LENGTH_SHORT).show();
                        totalDeDinheiro -= valor;
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