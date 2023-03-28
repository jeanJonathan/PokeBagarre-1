package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import java.util.concurrent.CompletableFuture;

public class Bagarre {
  public final PokeBuildApi webApi;

  public Bagarre() {
    this(new PokeBuildApi());
  }

  public Bagarre(PokeBuildApi webApi) {
    this.webApi = webApi;
  }
  public CompletableFuture<Pokemon> demarrer(String nomPremierPokemon, String nomSecondPokemon) {validerNomPokemons(nomPremierPokemon, nomSecondPokemon);return webApi.recupererParNom(nomPremierPokemon).thenCombine(webApi.recupererParNom(nomSecondPokemon), Bagarre::determinerVainqueur);
  }

  private void validerNomPokemons(String nomPremierPokemon, String nomSecondPokemon) {
    if (estVide(nomPremierPokemon)) {
      throw new ErreurPokemonNonRenseigne("premier");
    }
    if (estVide(nomSecondPokemon)) {
      throw new ErreurPokemonNonRenseigne("second");
    }
    if (nomPremierPokemon.trim().equalsIgnoreCase(nomSecondPokemon.trim())) {
      throw new ErreurMemePokemon();
    }
  }

  private boolean estVide(String nomPokemon) {
    return nomPokemon == null || nomPokemon.trim().isEmpty();
  }

  private static Pokemon determinerVainqueur(Pokemon premierPokemon, Pokemon secondPokemon) {
    return premierPokemon.estVainqueurContre(secondPokemon) ? premierPokemon : secondPokemon;
  }

}
