package br.ufpe.cin.residencia.banco.conta;

import static br.ufpe.cin.residencia.banco.MainActivity.TOTAL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ufpe.cin.residencia.banco.R;

//Ver anotações TODO no código
public class EditarContaActivity extends AppCompatActivity {

    public static final String KEY_NUMERO_CONTA = "numeroDaConta";
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
        campoNumero.setEnabled(false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        totalDeDinheiro = preferences.getInt(TOTAL, 0);

        Intent i = getIntent();
        String numeroConta = i.getStringExtra(KEY_NUMERO_CONTA);

        viewModel.buscarPeloNumero(numeroConta);

        viewModel.contaAtual.observe(this,
                conta -> {
                    campoNome.setText(conta.nomeCliente);
                    campoCPF.setText(conta.cpfCliente);
                    campoSaldo.setText(String.valueOf(conta.saldo));
                }
        );
        btnAtualizar.setText("Editar");
        btnAtualizar.setOnClickListener(
                v -> {
                    String nomeCliente = campoNome.getText().toString();
                    String cpfCliente = campoCPF.getText().toString();
                    String saldoConta = campoSaldo.getText().toString();

                    if(saldoConta.isEmpty() || cpfCliente.isEmpty() || nomeCliente.isEmpty()){ // se qualquer um dos dados for vazio
                        Toast.makeText(this, "Valor para o(s) campos invalidos!! ", Toast.LENGTH_SHORT).show(); // mostra a mensagem de valor invalido
                    } else {
                        if(isNumerico(saldoConta)) { // se não, verifica se o saldo é um numero, se sim, atualiza
                            Double saldo = Double.parseDouble(saldoConta);
                            Conta c = new Conta(numeroConta, saldo, nomeCliente, cpfCliente);
                            viewModel.atualizar(c);
                            totalDeDinheiro += c.saldo;
                            preferences
                                    .edit()
                                    .putInt(TOTAL, totalDeDinheiro)
                                    .apply();
                            finish();
                        } else{
                            Toast.makeText(this, "Saldo tem que ser numero", Toast.LENGTH_SHORT).show(); // se não mostra a mensagem de que saldo precisa ser um numero.
                        }


                    }
                }
        );

        btnRemover.setOnClickListener(v -> {
           // implementação de remoção da conta
            String nomeCliente = campoNome.getText().toString();
            String cpfCliente = campoCPF.getText().toString();
            String saldoConta = campoSaldo.getText().toString();
            if(saldoConta.isEmpty() || cpfCliente.isEmpty() || nomeCliente.isEmpty()){ // verifico se alguns dos campos são vazios
                Toast.makeText(this, "Valor para o(s) campos invalidos!! ", Toast.LENGTH_SHORT).show();
            } else {
                if (isNumerico(saldoConta)){ //  verifico se o saldo é numerico
                    Double saldo = Double.parseDouble(saldoConta);
                    Conta c = new Conta(numeroConta,saldo, cpfCliente, saldoConta);
                    viewModel.remover(c);
                    totalDeDinheiro -= c.saldo;
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
