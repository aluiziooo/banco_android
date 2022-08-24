package br.ufpe.cin.residencia.banco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.residencia.banco.conta.Conta;
import br.ufpe.cin.residencia.banco.conta.ContaAdapter;

public class PesquisarActivity extends AppCompatActivity {
    BancoViewModel viewModel;
    ContaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);
        viewModel = new ViewModelProvider(this).get(BancoViewModel.class);
        EditText aPesquisar = findViewById(R.id.pesquisa);
        Button btnPesquisar = findViewById(R.id.btn_Pesquisar);
        RadioGroup tipoPesquisa = findViewById(R.id.tipoPesquisa);
        RecyclerView rvResultado = findViewById(R.id.rvResultado);
        adapter = new ContaAdapter(getLayoutInflater());


        rvResultado.setLayoutManager(new LinearLayoutManager(this));
        rvResultado.setAdapter(adapter);

        RadioButton Nome = findViewById(R.id.peloNomeCliente);
        RadioButton CPF = findViewById(R.id.peloCPFcliente);
        RadioButton NumeroConta = findViewById(R.id.peloNumeroConta);

        btnPesquisar.setOnClickListener(
                v -> {
                    String oQueFoiDigitado = aPesquisar.getText().toString();
                   //a busca de acordo com o tipo de busca escolhido pelo usuário
                    if (oQueFoiDigitado.equals("")){
                        Toast.makeText(this, "Digite a informação a ser buscada", Toast.LENGTH_SHORT).show();
                    }
                    if (Nome.isChecked()){
                        viewModel.buscarPeloNome(oQueFoiDigitado);
                    }
                    if (CPF.isChecked()){
                        viewModel.buscarPeloCPF(oQueFoiDigitado);
                    }
                    if (NumeroConta.isChecked()){
                        viewModel.buscarPeloNumero(oQueFoiDigitado);
                    }


                }
        );

        //atualizando o RecyclerView com resultados da busca na medida que encontrar
        viewModel.contasBusca.observe(
                this,
                novaListaBusca -> {
                    if (novaListaBusca != null){
                        List<Conta> buscaAtualizada = new ArrayList<>(novaListaBusca);
                        adapter.submitList(buscaAtualizada);
                    }

                }
        );
        viewModel.contasBuscaAtual.observe(
                this,
                novaListaBuscaAtual -> {
                    if (novaListaBuscaAtual != null){
                        List<Conta> buscaAtualizadaAtual = new ArrayList<>();
                        buscaAtualizadaAtual.add(novaListaBuscaAtual);
                        adapter.submitList(buscaAtualizadaAtual);
                    }

                }
        );


    }
}