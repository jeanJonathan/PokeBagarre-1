package com.montaury.pokebagarre.metier;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import com.montaury.pokebagarre.ui.PokeBagarreApp;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
@ExtendWith(ApplicationExtension.class)
class PokeBagarreAppTest {
    private static final String IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1 = "#nomPokemon1";
    private static final String IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2 = "#nomPokemon2";
    private static final String IDENTIFIANT_BOUTON_BAGARRE = ".button";
    @Start
    private void start(Stage stage) {
        new PokeBagarreApp().start(stage);
    }
    private static String getResultatBagarre(FxRobot robot) {
        return robot.lookup("#resultatBagarre").queryText().getText();
    }
    private static String getMessageErreur(FxRobot robot) {
        return robot.lookup("#resultatErreur").queryLabeled().getText();
    }
    /***********************TEST ***************************/
    //On test pour vérifier que l'application s'ouvre correctement.
    @Test
    void testOuvertureApplication(FxRobot robot) {
        //robot.clickOn(IDENTIFIANT);
        //robot.write("Text");
        //await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
        //assertThat(...).isEqualTo(...)
        // );
        assertThat(robot.lookup("#titreFenetre").queryLabeled().getText()).isEqualTo("PokeBagarre");
    }
    //On test pour vérifier que l'utilisateur peut saisir les noms de deux Pokémon différents dans les champs prévus.
    @Test
    void testSaisiePokemon(FxRobot robot) {
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1).write("Pikachu");
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2).write("Salameche");
        assertThat(robot.lookup(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1).queryTextInputControl().getText()).isEqualTo("Pikachu");
        assertThat(robot.lookup(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2).queryTextInputControl().getText()).isEqualTo("Salameche");
    }
    //On test pour vérifier que l'utilisateur peut cliquer sur le bouton "Bagarre" pour lancer la bagarre entre les deux Pokémon.
    @Test
    void testBagarre(FxRobot robot) {
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1).write("Pikachu");
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2).write("Salameche");
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(getResultatBagarre(robot)).isEqualTo("Pikachu a gagné!")
        );
    }
    //On teste pour vérifier que l'application affiche un message d'erreur si l'utilisateur saisit deux noms de Pokémon identiques.
    @Test
    void testErreurSaisiePokemonIdentiques(FxRobot robot) {
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1).write("Pikachu");
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2).write("Pikachu");
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(getMessageErreur(robot)).isEqualTo("Veuillez saisir deux noms de Pokémon différents.")
        );
    }
}
