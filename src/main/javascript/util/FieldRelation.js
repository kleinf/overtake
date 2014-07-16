/**
 * Border-Objekt instanziieren und mit default-Werten initialisieren.
 *
 * @param borderId
 *            ID dieses Feldrandes.
 * @param refBorderId
 *            ID des angrenzenden Feldrandes.
 * @param refFieldX
 *            Spalten-ID des referenzierten Nachbarfeldes.
 * @param refFieldY
 *            Zeilen-ID des referenzierten Nachbarfeldes.
 */
function FieldRelation(borderId, refBorderId, refFieldX, refFieldY) {
	/**
	 * @return int
	 */
	FieldRelation.prototype.getBorderId = function() {
		return this.borderId;
	}

	/**
	 * @return int
	 */
	FieldRelation.prototype.getRefBorderId = function() {
		return this.refBorderId;
	}

	/**
	 * @return int
	 */
	FieldRelation.prototype.getRefFieldX = function() {
		return this.refFieldX;
	}

	/**
	 * @return int
	 */
	FieldRelation.prototype.getRefFieldY = function() {
		return this.refFieldY;
	}

	/**
	 * @return int
	 */
	FieldRelation.prototype.getKey = function() {
		return this.borderId + "|" + this.refBorderId + "|" + this.refFieldX
				+ "|" + this.refFieldY;
	}

	this.borderId = borderId;
	this.refBorderId = refBorderId;
	this.refFieldX = refFieldX;
	this.refFieldY = refFieldY;
}
