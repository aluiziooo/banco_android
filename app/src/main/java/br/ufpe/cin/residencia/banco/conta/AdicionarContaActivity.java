package br.ufpe.cin.residencia.banco.conta;

import static br.ufpe.cin.residencia.banco.MainActivity.TOTAL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ufpe.cin.residencia.banco.R;

//Ver anotações TODO no código
public class AdicionarContaActivity extends AppCompatActivity {

    ContaViewModel viewModel;
    private int totalDeDinheiro = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_conta);
        viewModel = new ViewModelProvider(this).get(ContaViewModel.class);

        Button btnAtualizar = findViewById(R.id.btnAtualizar);
        Button btnRemover = findViewById(R.id.btnRemover);
        EditText campoNome = findViewById(R.id.nome);
        EditText campoNumero = findViewById(R.id.numero);
        EditText campoCPF = findViewById(R.id.cpf);
        EditText campoSaldo = findViewById(R.id.saldo);

        btnAtualizar.setText("Inserir");
        btnRemover.setVisibility(View.GONE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        totalDeDinheiro = preferences.getInt(TOTAL, 0);

        btnAtualizar.setOnClickListener(
                v -> {
                    String nomeCliente = campoNome.getText().toString();
                    String cpfCliente = campoCPF.getText().toString();
                    String numeroConta = campoNumero.getText().toString();
                    String saldoConta = campoSaldo.getText().toString();
                    //TODO: Incluir validações aqui, antes de criar um objeto Conta (por exemplo, verificar que digitou um nome com pelo menos 5 caracteres, que o campo de saldo tem de fato um número, assim por diante). Se todas as validações passarem, aí sim cria a Conta conforme linha abaixo.
                    if (saldoConta.isEmpty() || cpfCliente.isEmpty() || nomeCliente.isEmpty()) {
                        Toast.makeText(this, "Valor para o(s) campos invalidos!! ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!isNumerico(saldoConta)) {
                            Toast.makeText(this, "Saldo Tem que ser um numero!!!", Toast.LENGTH_SHORT).show();
                        } else {

                            Conta c = new Conta(numeroConta, Double.valueOf(saldoConta), nomeCliente, cpfCliente);
                            //TODO: chamar o método que vai salvar a conta no Banco de Dados
                            viewModel.inserir(c);
                            totalDeDinheiro += c.saldo;
                            preferences
                                    .edit()
                                    .putInt(TOTAL, totalDeDinheiro)
                                    .apply();
                            finish();
                        }


                    }


                });
    }
    boolean isNumerico(String saldo) {
        return saldo.matches("[+-]?\\d*(\\.\\d+)?");
    }
}