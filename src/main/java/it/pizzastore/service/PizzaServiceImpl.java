package it.pizzastore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.pizzastore.model.Ingrediente;
import it.pizzastore.model.Pizza;
import it.pizzastore.repository.IngredienteRepository;
import it.pizzastore.repository.PizzaRepository;

@Service
public class PizzaServiceImpl implements PizzaService {

	@Autowired
	IngredienteRepository ingredienteRepository;
	
	@Autowired
	PizzaRepository pizzaRepository;

	@Transactional(readOnly = true)
	@Override
	public List<Pizza> listAll() {
		return (List<Pizza>) pizzaRepository.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Pizza caricaSingolo(Long id) {
		return pizzaRepository.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public void aggiorna(Pizza o) {
		pizzaRepository.save(o);
	}

	@Transactional
	@Override
	public void inserisciNuovo(Pizza o) {
		pizzaRepository.save(o);
	}

	@Transactional
	@Override
	public void rimuovi(Pizza o) {
		pizzaRepository.delete(o);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Pizza> findByExample(Pizza example) {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
		// Match string containing pattern .withIgnoreCase();
		return (List<Pizza>) pizzaRepository.findAll(Example.of(example, matcher));
	}

	@Transactional(readOnly = true)
	@Override
	public Pizza caricaSingolaPizzaConIngredienti(Long id) {
		return pizzaRepository.findByIdWithIngredients(id);
	}

	@Transactional
	@Override
	public void aggiornaConIngredienti(Pizza pizzaInstance) {
		List<Long> idIngredienti = new ArrayList<>();
		for(Ingrediente ingrediente: pizzaInstance.getIngredienti()) {
			idIngredienti.add(ingrediente.getId());
		}
		
		List<Ingrediente> ingredientiPersistList = (List<Ingrediente>) ingredienteRepository.findAllById(idIngredienti);
		Set<Ingrediente> ingredientiPersist = ingredientiPersistList.stream().collect(Collectors.toSet());
		
		Pizza pizzaPersist = pizzaRepository.findById(pizzaInstance.getId()).orElse(null);
		
		pizzaPersist.setIngredienti(ingredientiPersist);
	}

}
