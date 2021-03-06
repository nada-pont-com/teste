package interfacegrafica;

import javax.swing.JOptionPane;
import java.util.List;
import java.util.ArrayList;
import EstoqueLoja.Estoque;
import notas.NotasFiscais;
import produto.Produto;

public class Menu {

	public void menuOpcoes() {
		Estoque estoque = new Estoque();
		NotasFiscais notasF = new NotasFiscais();
		List<Produto> listaDeProdutos = new ArrayList<Produto>();
		List<Produto> listaDeNotas = new ArrayList<Produto>();
		while(true) {
			int opcao = Integer.parseInt(JOptionPane.showInputDialog("Escolha uma das op��es abaixo:\n"
					+ "1 - Cadastro de Produtos\n"
					+ "2 - Adicionar Produto j� Cadastrado\n"
					+ "3 - Lista de Produtos Cadastrados\n"
					+ "4 - Visualizar Estoque\n"
					+ "5 - Vendas\n"
					+ "6 - Notas Fiscais\n"
					+ "7 - Valor Total de Vendas\n"
					+ "8 - Sair\n")); 
			switch(opcao) {
			case 1:
			case 2:
			case 5:
				subMenu(listaDeProdutos,estoque,opcao, listaDeNotas,notasF);
				break;
			case 3:
				visualizarListDeProdutos(estoque);
				break;
			case 4:
				VisualizarEstoque(estoque);
				break;
			case 6:
				visualizarNotas(notasF,estoque);
				break;
			case 7:
				valorTotalVendas(notasF);
				break;
			case 8:
				System.exit(0);
				break;
			default:
				JOptionPane.showMessageDialog(null, "Op��o inexistente!","Erro",JOptionPane.ERROR_MESSAGE);
				break;
			}
		}
	}




	private void subMenu(List<Produto> listaDeProdutos,Estoque estoque,int opcao,List<Produto> listaDeNotas,NotasFiscais notasF){
		Produto produto = new Produto();
		if(opcao==1)
			listaDeProdutos.add(cadastro(produto,estoque));
		else if(opcao==2)
			estoque.adicionar();
		else if(opcao==5) { 
			listaDeNotas.add(vendaProduto(estoque));
			antiErro(listaDeNotas);
			notasF.setListaDeNotas(listaDeNotas);
		}

		estoque.setListaDeProdutos(listaDeProdutos);
	}
	
	private void antiErro(List<Produto> listaDeNotas) {
		int cont = 0;
		for(Produto produto : listaDeNotas) {
			if(produto.getQuant()==(-2)){
				listaDeNotas.remove(cont);
				break;
			}
			cont++;
		}
	}

	private Produto cadastro(Produto produto,Estoque estoque) {	
		int verifica=0,codigo;
		String nome = JOptionPane.showInputDialog("O nome do produto a ser cadastrado: ");
		do {
			codigo = Integer.parseInt(JOptionPane.showInputDialog("Codigo da(o) "+nome+": "));
			if(estoque.getListaDeProdutos()==null) {

			}else
				for (Produto prod : estoque.getListaDeProdutos()) {
					if(codigo==prod.getCodigo()) {
						verifica=2;
						JOptionPane.showMessageDialog(null, "J� exite produto usando esse codigo (Produto: "+prod.getNome()+")","Codigo j� esta sendo usado!",JOptionPane.WARNING_MESSAGE);
						break;
					}else
						verifica=1;
				}
		}while(verifica==2);
		double valor = Double.parseDouble(JOptionPane.showInputDialog("Insira o pre�o do(a) "+nome+": "));
		int quantidade = Integer.parseInt(JOptionPane.showInputDialog("Quantidade do produto "+nome+": "));
		String descricao = JOptionPane.showInputDialog("Descri��o do produto "+nome+": ");
		cadastroProduto(nome,codigo,valor,quantidade,descricao,produto);
		return produto;
	}

	
	private void VisualizarEstoque(Estoque estoque) {
		String tudo = "Disponivel no estoque:\n\n";
		if((estoque.getListaDeProdutos()==null)||(estoque.getListaDeProdutos().isEmpty())) 
			tudo = "N�o a nada disponivel no estoque.";
		else {
			for(Produto prod : estoque.getListaDeProdutos()) {
				tudo += "Nome do produto: "+prod.getNome()+"   Codigo:"+prod.getCodigo()+"   Valor: "+prod.getValor()+
						"   Quantidade: "+prod.getQuant()+"   Descri��o: "+prod.getDescricao()+"\n";
			}
		}
		JOptionPane.showMessageDialog(null, tudo);

	}

	private void visualizarListDeProdutos(Estoque estoque) {
		String tudo = "Produtos cadastrados:\n\n";
		int verifica = 0;
		if((estoque.getListaDeProdutos()==null)||(estoque.getListaDeProdutos().isEmpty())) { 
			tudo = "N�o a produtos cadastrados!";
			verifica = 2;
		}else {
			for(Produto prod : estoque.getListaDeProdutos()) {
				tudo += "Nome do produto: "+prod.getNome()+"   Codigo:"+prod.getCodigo()+"\n";
			}
		}
		if(verifica == 0) 
			JOptionPane.showMessageDialog(null, tudo);
		else
			JOptionPane.showMessageDialog(null, tudo,"N�o a produtos!",JOptionPane.WARNING_MESSAGE);
	}

	private Produto vendaProduto(Estoque estoque) {
		Produto produto = new Produto();
		if((estoque.getListaDeProdutos()==null)||(estoque.getListaDeProdutos().isEmpty())) {
			JOptionPane.showMessageDialog(null, "N�o a nem um produto cadastrado!","N�o a produtos!",JOptionPane.WARNING_MESSAGE);
			produto.setQuant(-2);
		}else {


			int verifica = 2;
			do {
				int codigo= Integer.parseInt(JOptionPane.showInputDialog("Digote o codigo do produto vendido: "));
				int quantidade = 0,soma;
				double valor;

				for(Produto prod : estoque.getListaDeProdutos()) {

					if(codigo==prod.getCodigo()) {

						JOptionPane.showMessageDialog(null, "Produto encontado: "+prod.getNome());
						verifica = JOptionPane.showConfirmDialog(null,"Esse � o produto que deseja vender: "+prod.getNome()+"?","Verifica��o!",JOptionPane.YES_NO_OPTION);

						if((verifica==0)&&(prod.getQuant()!=0)) {

							do {
								quantidade = Integer.parseInt(JOptionPane.showInputDialog("Quantidade do produto a ser vendido: (disponiveis: "+prod.getQuant()+")."));
								int quantAnt = prod.getQuant();
								soma = quantAnt-quantidade;


								if(soma<0)

									JOptionPane.showMessageDialog(null, "N�o a produtos suficientes no estoque para realizar a venda!","N�o a produtos!",JOptionPane.WARNING_MESSAGE);
								else if(quantidade==0) 
									JOptionPane.showMessageDialog(null, "N�o � possivel vender \"0\" produtos realizar a venda!","N�o a produtos!",JOptionPane.WARNING_MESSAGE);
								else {
									verifica=3;
									prod.setQuant(soma);
									valor = prod.getValor();
									produto.setValor(quantidade*valor);
									produto.setQuant(quantidade);
									produto.setCodigo(codigo);
									produto.setNome(prod.getNome());
								}
							}while((soma<0)||(quantidade==0));

						}else if(prod.getQuant()==0) {
							JOptionPane.showMessageDialog(null, "N�o a mais esse produto no estoque para se realizar a venda!","N�o a produtos!",JOptionPane.WARNING_MESSAGE);
							produto.setQuant(-2);
						}else if(verifica==1) {
							JOptionPane.showMessageDialog(null, "Venda Canselada!","Canselada!",JOptionPane.WARNING_MESSAGE);
							produto.setQuant(-2);
						}
					}
					
				}
				if(verifica==2) {
					String tudo = "Produtos disponiveis no estoque!\n";
					JOptionPane.showMessageDialog(null, "N�o a nem um produto com esse codigo!","Codigo n�o encontrado!",JOptionPane.WARNING_MESSAGE);
					
					for(Produto produ : estoque.getListaDeProdutos()) {
						if(produ.getQuant()!=0) {
							tudo+="Nome:"+produ.getNome()+"   Codigo:"+produ.getCodigo()+"\n";
						}
						
					}
					JOptionPane.showMessageDialog(null,tudo,"Disponiveis!",JOptionPane.INFORMATION_MESSAGE);
				}
			}while(verifica==2);
		}
		return produto;
	}

	private void visualizarNotas(NotasFiscais notasF,Estoque estoque) {
		String tudo = "";
		if((notasF.getListaDeNotas()==null)||(notasF.getListaDeNotas().isEmpty())) {
			JOptionPane.showMessageDialog(null, "N�o ouve nem uma venda ent�o n�o ouve gera��o de nota fiscal!","N�o a Notas Fiscais!",JOptionPane.WARNING_MESSAGE);
		}else {
			for (Produto produto : notasF.getListaDeNotas()) {
				tudo += "Cogido do produto: "+produto.getCodigo()+"   Quantidade vendida: "+produto.getQuant()+"   Valor Total: "+produto.getValor()+"\n"+ "Produto: "+produto.getNome()+"   Valor(cada): "+produto.getValor()+"\n\n";
			}
			JOptionPane.showMessageDialog(null, tudo);
		}
	}

	private void valorTotalVendas(NotasFiscais notasF) {
		if((notasF.getListaDeNotas()==null)||(notasF.getListaDeNotas().isEmpty()))
			JOptionPane.showMessageDialog(null, "N�o ouve nem uma gera��o de nota fiscal!","N�o Ouve Vendas!",JOptionPane.WARNING_MESSAGE);
		else {
			int valorT = 0;
			for(Produto produto: notasF.getListaDeNotas()) {
				valorT+= produto.getValor();
			}
			JOptionPane.showMessageDialog(null, "O valor todal de vendas �: "+valorT);
		}
	}

	private void cadastroProduto(String nome, int codigo, double valor,int quant,String descricao,Produto produto) {
		produto.setNome(nome);
		produto.setValor(valor);
		produto.setCodigo(codigo);
		produto.setQuant(quant);
		produto.setDescricao(descricao);
	}


}
