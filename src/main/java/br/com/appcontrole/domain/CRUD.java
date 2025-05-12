package br.com.appcontrole.domain;

import java.util.List;
import java.util.UUID;

public interface CRUD<T, ID> {
	
	T insere(T t);
	
	T atualiza(T t);
	
	void remove(UUID id);
	
	List<T> buscaTodos();
	
	T buscaPorId(UUID id);

}
