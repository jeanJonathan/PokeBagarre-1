package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.erreurs.ErreurRecuperationPokemon;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BagarreTest {

    private PokeBuildApi mockApi;
    private Bagarre bagarre;

    //est appelée avant chaque test et initialise les variables mockApi et bagarre.
    @BeforeEach
    void setUp() {
        mockApi = mock(PokeBuildApi.class);
        bagarre = new Bagarre(mockApi);
    }
    // est appelée après chaque test et réinitialise ces variables à null. Cela permet de s'assurer que chaque test est exécuté dans un contexte propre et évite que les tests ne s'influencent mutuellement.
    @AfterEach
    void tearDown() {
        mockApi = null;
        bagarre = null;
    }

//teste si la méthode demarrer() de Bagarre retourne le Pokemon gagnant lorsqu'on lui passe deux noms de Pokemon valides.
    @Test
    void demarrer_shouldReturnWinnerPokemon_whenValidPokemonNamesProvided() {
        // Given
        Pokemon pikachu = new Pokemon("pikachu", "url1", new Stats(1, 2));
        Pokemon mewtwo = new Pokemon("mewtwo", "url2", new Stats(3, 4));
//pour configurer les appels de l'API simulée pour retourner les Pokemon Pikachu et Mewtwo
        when(mockApi.recupererParNom("pikachu")).thenReturn(CompletableFuture.completedFuture(pikachu));
        when(mockApi.recupererParNom("mewtwo")).thenReturn(CompletableFuture.completedFuture(mewtwo));

        // When
        CompletableFuture<Pokemon> futurVainqueur = bagarre.demarrer("pikachu", "mewtwo");

        // Then
        //utilisée pour s'assurer que le Pokemon gagnant est bien Mewtwo, en vérifiant son nom, son URL d'image et ses statistiques d'attaque et de défense
        assertThat(futurVainqueur)
                .succeedsWithin(Duration.ofSeconds(2))
                .satisfies(pokemon -> {
                    assertThat(pokemon.getNom()).isEqualTo("mewtwo");
                    assertThat(pokemon.getUrlImage()).isEqualTo("url2");
                    assertThat(pokemon.getStats().getAttaque()).isEqualTo(3);
                    assertThat(pokemon.getStats().getDefense()).isEqualTo(4);
                });
        /*La méthode succeedsWithin() est utilisée pour s'assurer que le test réussit dans les 2 secondes, sinon le test échoue.*/
    }
//teste si la méthode demarrer() de Bagarre lance une exception de type ErreurMemePokemon lorsque deux noms de Pokemon identiques sont passés en paramètre. La méthode
    @Test
    void demarrer_shouldThrowError_whenSamePokemonNameProvided() {
        // When / Then
        //est utilisée pour s'assurer que l'exception est bien lancée.
        assertThatThrownBy(() -> bagarre.demarrer("pikachu", "pikachu"))
                .isInstanceOf(ErreurMemePokemon.class);
    }
////este si la méthode demarrer() de Bagarre lance une exception de type ErreurPokemonNonRenseigne lorsque l'un ou les deux noms de Pokemon ne sont pas renseignés en paramètre.
    @Test
    void demarrer_shouldThrowError_whenOneOrBothPokemonNamesNotProvided() {
        // When / Then
        assertThatThrownBy(() -> bagarre.demarrer("", "mewtwo"))
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessageContaining("premier");

        assertThatThrownBy(() -> bagarre.demarrer("pikachu", null))
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessageContaining("second");

        assertThatThrownBy(() -> bagarre.demarrer("", ""))
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessageContaining("premier");
    }
    //teste si la méthode demarrer() de Bagarre lance une exception de type ErreurRecuperationPokemon lorsque l'API ne parvient pas à récupérer le Pokemon demandé.
    @Test
    void demarrer_shouldThrowError_whenPokemonNotFound() {
        // Given
        var fausseApi = Mockito.mock(PokeBuildApi.class);
        var bagarre = new Bagarre(fausseApi);

        var pokemonIntrouvable = "pokemonIntrouvable";
        Mockito.when(fausseApi.recupererParNom(pokemonIntrouvable))
                .thenReturn(CompletableFuture.failedFuture(new ErreurRecuperationPokemon(pokemonIntrouvable)));

        // When
        var futurVainqueur = bagarre.demarrer("pikachu", pokemonIntrouvable);

        // Then
        assertThat(futurVainqueur)
                .failsWithin(Duration.ofSeconds(2))
                .withThrowableOfType(ErreurRecuperationPokemon.class);
        Mockito.verify(fausseApi, Mockito.times(1)).recupererParNom(pokemonIntrouvable);
        Mockito.verify(fausseApi, Mockito.never()).recupererParNom("pikachu");
    }

}