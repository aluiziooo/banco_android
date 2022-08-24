package br.ufpe.cin.residencia.banco;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;

import br.ufpe.cin.residencia.banco.conta.Conta;
import br.ufpe.cin.residencia.banco.conta.ContaRepository;

public class BancoViewModel extends AndroidViewModel {
    private ContaRepository repository;

    private MutableLiveData<List<Conta>> _contasBusca = new MutableLiveData<>();
    public LiveData<List<Conta>> contasBusca = _contasBusca;
    private MutableLiveData<Conta> _contasBuscaAtual = new MutableLiveData<>();
    public LiveData<Conta> contasBuscaAtual = _contasBuscaAtual;

    public BancoViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ContaRepository(BancoDB.getDB(application).contaDAO());
    }

    void transferir(String numeroContaOrigem, String numeroContaDestino, double valor) {
       /*
       * O metodo busca no banco de dados as contas de origem e destino para fazer a tranferencia,
       * Depois que encontra o metodo transferir defenido na classe conta faz o debito da conta
       * origem e credito da conta de destino. Apos isso chama o metodo para atualizar o objeto no banco
       * */
        new Thread(() -> {
            Conta c1 = repository.buscarPeloNumero(numeroContaOrigem);
            Conta c2 = repository.buscarPeloNumero(numeroContaDestino);
            c1.transferir(c2, valor);
            repository.atualizar(c1); // chamando o metodo de repository
            repository.atualizar(c2); // chamando o metodo de repository
        }).start();

    }

    void creditar(String numeroConta, double valor) {
       /*
       * pesquisa a conta pelo numero e faz a adição do valor passado pelo parametro, chama o
       * atualizar para atualizar o objeto no banco
       * */

        new Thread(() -> {
            Conta c = repository.buscarPeloNumero(numeroConta);
            c.creditar(valor);
            repository.atualizar(c); // chamando o metodo de repository
        }).start();
    }

    void debitar(String numeroConta, double valor) {
        /*
        * busca a conta pelo numero e salva no objeto c,
        * depois debita o valor utilizando o método debitar,
        * por fim atualiza o novo valor utilizando o metodo atualizar.
        * */
        new Thread(() -> {
            Conta c = repository.buscarPeloNumero(numeroConta);
            c.debitar(valor);
            repository.atualizar(c); // chamando o metodo de repository
        }).start();
    }

    void buscarPeloNome(String nomeCliente) {
        /*
         * pesquisa a conta pelo Nome e o põe no mutable
         * */
        new Thread(() -> {
            List<Conta> c = repository.buscarPeloNome(nomeCliente);
            _contasBusca.postValue(c); // Adicionando ao mutable para atualização do observable
        }).start();
    }

    void buscarPeloCPF(String cpfCliente) {
        /*
         * pesquisa a conta pelo CPF e o põe no mutable
         * */
        new Thread(() -> {
            List<Conta> c = repository.buscarPeloCPF(cpfCliente);
            _contasBusca.postValue(c); // Adicionando ao mutable para atualização do observable
        }).start();
    }

    void buscarPeloNumero(String numeroConta) {
        /*
         * pesquisa a conta pelo Numero e o põe no mutable
         * */
        new Thread(() -> {
            Conta c = repository.buscarPeloNumero(numeroConta);
            _contasBuscaAtual.postValue(c); // Adicionando ao mutable para atualização do observable
        }).start();
    }

}
