package it.project.profumeria_note.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import it.project.profumeria_note.entities.TipoNota;
import it.project.profumeria_note.repositories.TipoNotaRepository;
import jakarta.transaction.Transactional;

@Component
public class DatabaseSeed {
	@Autowired
	private TipoNotaRepository tipoNotaRepository;
	
    @Value("${seed.database}")
    private boolean seedDatabase;
	
	@EventListener
	@Transactional
	public void seed(ContextRefreshedEvent event) {
		if (seedDatabase == false) return;
		
		System.out.println("TRUE");
		seedTipoNoteTable();
	}
	
	
//	per popolare la tabella TipoNota sin da subito, viene usato il seguente metodo:
	private void seedTipoNoteTable() {
		List<TipoNota> tipoNote = tipoNotaRepository.findAll();
		if (tipoNote.size() > 0) {
			return;
		}
		
		TipoNota testa = new TipoNota("testa");
		TipoNota cuore = new TipoNota("cuore");
		TipoNota fondo = new TipoNota("fondo");
		
		tipoNotaRepository.save(testa);
		tipoNotaRepository.save(cuore);
		tipoNotaRepository.save(fondo);
	}
}
