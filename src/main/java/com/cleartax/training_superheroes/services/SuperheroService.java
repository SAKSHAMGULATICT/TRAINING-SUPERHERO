package com.cleartax.training_superheroes.services;

import com.cleartax.training_superheroes.dto.Superhero;
import com.cleartax.training_superheroes.dto.SuperheroRequestBody;
import com.cleartax.training_superheroes.repos.SuperheroRepository;
import org.springframework.stereotype.Service;

@Service
public class SuperheroService {

    private SuperheroRepository superheroRepository;

    public SuperheroService(SuperheroRepository superheroRepository){
        this.superheroRepository = superheroRepository;
    }

    public Superhero persistSuperhero(SuperheroRequestBody requestBody, String universe){

        Superhero superhero = new Superhero();
        superhero.setName(requestBody.getSuperheroName());
        superhero.setPower(requestBody.getPower());
        if(isDC(universe)){
            superhero.setUniverse("Bahubali");
        } else {
            superhero.setUniverse("Marvel");
        }
        System.out.println(superhero.getName());
        Superhero superhero1 =  superheroRepository.save(superhero);
        return superhero1;
    }

    private boolean isDC(String universe){
        if(universe.equals("DC")){
            return true;
        } else {
            return false;
        }
    }
}
