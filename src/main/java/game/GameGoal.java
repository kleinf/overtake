package game;

/**
 * @author Administrator
 * 
 */
public enum GameGoal {

	/**
	 * Es wird gespielt, bis einer uebrig bleibt. Wer keine Felder mehr hat,
	 * scheidet aus.
	 */
	LAST_MAN_STANDING(),
	/**
	 * Es wird gespielt, bis einer die anteilige Mehrheit errungen hat. (Bei
	 * zwei Spielern > 50 % der Felder, bei drei Spielern > 33 % der Felder,
	 * usw.) Solange Felder frei sind, darf gesetzt werden, es kann aber auch so
	 * gespielt werden, dass Spieler die keine Felder mehr haben, nicht mehr
	 * mitspielen duerfen. Dann aendert sich aber das Kraefteverhaeltnis und die
	 * verbliebenen Spieler muessen Anteilig mehr Felder erringen. (Bsp. bei
	 * drei Spielern scheidet einer aus, dann muessen die beiden verbliebenen >
	 * 50 % der Felder erobern.)
	 */
	DIVIDE_ET_IMPERA(),
	/**
	 * Es wird gespielt, bis einer alle Felder besitzt. Solange Felder frei
	 * sind, darf gesetzt werden.
	 */
	DOMINATION();

	private GameGoal() {
	}
}
