package br.com.appcontrole.domain;

import java.util.List;

public interface CRUD<T, ID> {
	
	T insere(T t);
	
	T atualiza(T t);
	
	void remove(Long id);
	
	List<T> buscaTodos();
	
	T buscaPorId(Long id);
	
}
