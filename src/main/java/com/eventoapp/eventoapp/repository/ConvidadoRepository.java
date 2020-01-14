package com.eventoapp.eventoapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.eventoapp.eventoapp.model.Convidado;
import com.eventoapp.eventoapp.model.Evento;

public interface ConvidadoRepository extends CrudRepository<Convidado, String>{
	Iterable <Convidado> findByEvento(Evento evento);
	Convidado findByRg(long rg);
	

}
