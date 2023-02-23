package com.montaury.pokebagarre.metier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.montaury.pokebagarre.fixtures.ConstructeurDePokemon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class PokemonTest {
    private Pokemon pikachu;
    private Pokemon bulbizarre;
    private Pokemon carapuce;
    private Pokemon salameche;

    @Test
    void estVainqueurContrePremierGagne() {
        Pokemon p1 = new Pokemon("Pikachu","" ,new Stats(50, 40));
        Pokemon p2 = new Pokemon("Bulbasaur","" ,new Stats(30, 30));
        assertTrue(p1.estVainqueurContre(p2));
    }
    @Test
    void estVainqueurContreDeuxiemeGagne() {
        Pokemon p1 = new Pokemon("Pikachu","", new Stats(30, 30));
        Pokemon p2 = new Pokemon("Bulbasaur","", new Stats(50, 40));
        assertTrue(p2.estVainqueurContre(p1));
    }

    @Test
    void estVainqueurContrePremierGagnePriorite() {
        Pokemon p1 = new Pokemon("Pikachu","", new Stats(50, 40));
        Pokemon p2 = new Pokemon("Bulbasaur","",new Stats(50, 40));
        assertTrue(p1.estVainqueurContre(p2));
    }

    @Test
    void estVainqueurContreDeuxiemeGagnePriorite() {
        Pokemon p1 = new Pokemon("Pikachu","", new Stats(50, 40));
        Pokemon p2 = new Pokemon("Bulbasaur","", new Stats(50, 40));
        assertTrue(p2.estVainqueurContre(p1));
    }

    @Test
    void estVainqueurContreAucunGagne() {
        Pokemon p1 = new Pokemon("Pikachu","", new Stats(0, 0));
        Pokemon p2 = new Pokemon("Bulbasaur","", new Stats(0, 0));
        assertFalse(p1.estVainqueurContre(p2));
    }

    /*On a utilisé la méthode setUp pour initialiser les Pokémons avant chaque test et
     tearDown pour libérer leur mémoire après chaque test*/
    @BeforeEach
    void setUp() {
        pikachu = ConstructeurDePokemon.unPokemon().avecAttaque(50).avecDefense(40).construire();
        bulbizarre = ConstructeurDePokemon.unPokemon().avecAttaque(30).avecDefense(30).construire();
        carapuce = ConstructeurDePokemon.unPokemon().avecAttaque(35).avecDefense(45).construire();
        salameche = ConstructeurDePokemon.unPokemon().avecAttaque(50).avecDefense(40).construire();
    }

    @Test
    void estVainqueurContre_pokemon1Remporte() {
        assertTrue(pikachu.estVainqueurContre(bulbizarre));
    }

    @Test
    void estVainqueurContre_pokemon2Remporte() {
        assertTrue(bulbizarre.estVainqueurContre(carapuce));
    }

    @Test
    void estVainqueurContre_pokemon1RemporteAvecPriorite() {
        assertTrue(pikachu.estVainqueurContre(salameche));
    }

    @Test
    void estVainqueurContre_pokemon2RemporteAvecPriorite() {
        assertFalse(salameche.estVainqueurContre(pikachu));
    }

    @Test
    void estVainqueurContre_pokemonEgaux_pokemon1Remporte() {
        assertTrue(pikachu.estVainqueurContre(salameche));
    }

    @Test
    void estVainqueurContre_pokemonEgaux_pokemon2Remporte() {
        assertFalse(salameche.estVainqueurContre(pikachu));
    }

    @AfterEach
    void tearDown() {
        pikachu = null;
        bulbizarre = null;
        carapuce = null;
        salameche = null;
    }

}